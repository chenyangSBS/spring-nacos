# Nacos 微服务服务注册与发现

本项目基于 Spring Boot 3 与 Spring Cloud Alibaba，演示如何在微服务多模块项目中集成 Nacos 进行服务注册与发现，以及通过 OpenFeign 与 `RestTemplate` 进行服务调用。仓库包含三个模块：

- `springcloud-nacos-provider`：提供者服务，注册到 Nacos，并暴露接口
- `springcloud-nacos-consumer`：消费者服务，从 Nacos 发现 `provider` 并调用其接口
- `springcloud-nacos-config`：演示 Nacos 配置中心的动态配置与刷新

## 环境准备

- JDK 17，Maven 3.8+
- Nacos Server 2.x（推荐本地启动或容器化）

示例启动（Docker）：

```bash
docker run -d --name nacos -p 8848:8848 -e MODE=standalone nacos/nacos-server:latest
```

访问控制台：`http://localhost:8848/nacos`（默认账号密码：`nacos`/`nacos`）

## 关键配置

- Provider 与 Consumer 通过 `bootstrap.yml` 指定 Nacos 服务发现地址、命名空间与分组：

```yaml
# springcloud-nacos-provider/src/main/resources/bootstrap.yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.52.244:8848
        namespace: 0e1a07dd-085b-4412-b87b-4644c665fdbc
        group: DEFAULT_GROUP
    inetutils:
      ignored-interfaces: 'VMware Virtual Ethernet Adapter for VMnet1,VMware Virtual Ethernet Adapter for VMnet8'
```

```yaml
# springcloud-nacos-consumer/src/main/resources/bootstrap.yml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.52.244:8848
        namespace: 0e1a07dd-085b-4412-b87b-4644c665fdbc
        group: DEFAULT_GROUP
    inetutils:
      ignored-interfaces: 'VMware Virtual Ethernet Adapter for VMnet1,VMware Virtual Ethernet Adapter for VMnet8'
```

- Nacos 配置中心模块独立使用另一命名空间：

```yaml
# springcloud-nacos-config/src/main/resources/bootstrap.yml
spring:
  application:
    name: springcloud-nacos-config
  cloud:
    nacos:
      config:
        server-addr: 192.168.52.244:8848
        namespace: 0e1a07dd-085b-4412-b87b-4644c665fdbc
        group: DEFAULT_GROUP
        file-extension: yml
        refresh-enabled: true
        import-check:
          enabled: false
```

提示：将 `server-addr` 与 `namespace` 替换为你自己的 Nacos 地址与命名空间 ID；开发环境可使用 `public` 命名空间（留空或删除 `namespace`）。

## 服务注册

- Provider 开启注册并声明应用名：

  - `springcloud-nacos-provider/src/main/java/edu/sbs/ms/Application.java:7–12`
  - `springcloud-nacos-provider/src/main/resources/application.yml:5–7`

  注册时附加元数据（启动时间与应用名），便于观测与治理：

  - `springcloud-nacos-provider/src/main/java/edu/sbs/ms/config/NacosMetadataConfig.java:33–36`

## 服务发现与调用

- Consumer 允许注册发现与 Feign：

  - `springcloud-nacos-consumer/src/main/java/edu/sbs/ms/Application.java:11–19,21–26`

- 通过 `LoadBalancerClient` + `RestTemplate` 按服务名选择并调用：

  - `springcloud-nacos-consumer/src/main/java/edu/sbs/ms/controller/ConsumerController.java:25–33`

- 通过 OpenFeign 声明式调用 Provider，并设置容错 Fallback：

  - `springcloud-nacos-consumer/src/main/java/edu/sbs/ms/service/RemoteProviderService.java:17–25`
  - `springcloud-nacos-consumer/src/main/java/edu/sbs/ms/config/ProviderFallbackFactory.java:15–27`

- Provider 暴露的示例接口：

  - `GET /provider/welcome?name=...`
    - `springcloud-nacos-provider/src/main/java/edu/sbs/ms/controller/DiscoveryController.java:14–18`
  - `GET /provider/challenge/random/{max}`
    - `springcloud-nacos-provider/src/main/java/edu/sbs/ms/controller/DiscoveryController.java:20–26`
  - `POST /provider/attempts`（JSON 请求体）
    - `springcloud-nacos-provider/src/main/java/edu/sbs/ms/controller/DiscoveryController.java:28–38`

## 运行步骤

1. 启动 Nacos Server 并确认控制台可用。
2. 修改三个模块的 `bootstrap.yml` 中 `server-addr` 与 `namespace`，确保连接正确。
3. 构建项目：

   ```bash
   mvn -q -DskipTests package
   ```

4. 启动 Provider 与 Consumer（任选其一方式）：

   - 开发模式：在两个独立终端中执行模块的 Spring Boot 运行插件：

     ```bash
     mvn -q -pl springcloud-nacos-provider spring-boot:run
     mvn -q -pl springcloud-nacos-consumer spring-boot:run
     ```

   - 或者运行打包后的可执行 Jar：

     ```bash
     java -jar springcloud-nacos-provider/target/springcloud-nacos-provider-0.0.1-SNAPSHOT.jar
     java -jar springcloud-nacos-consumer/target/springcloud-nacos-consumer-0.0.1-SNAPSHOT.jar
     ```

5. 在 Nacos 控制台查看服务注册：`provider` 与 `consumer` 应显示在服务列表中（分组 `DEFAULT_GROUP`）。

## 验证服务发现与调用

- 通过 Consumer 使用负载均衡客户端调用 Provider：

  ```bash
  curl "http://localhost:6005/consumer/test?name=Ada"
  # 返回示例：Invoke : http://127.0.0.1:6004/provider/welcome?name=Ada, return : Welcome: Ada !
  ```

- 查看从 Nacos 注入的服务元数据：

  ```bash
  curl "http://localhost:6005/consumer/metadata"
  # 返回示例：metadata : {startup.time=2025-11-28 10:00:00, applicationName=provider}
  ```

- 通过 Feign 声明式调用 Provider：

  ```bash
  # GET 随机数（Consumer 内部调用 Provider 的 /provider/challenge/random/{max}）
  curl "http://localhost:6005/consumer/random/10"

  # POST 校验（Consumer 内部调用 Provider 的 /provider/attempts）
  curl -X POST "http://localhost:6005/consumer/attempts" \
       -H "Content-Type: application/json" \
       -d '{"factorA":1,"factorB":2,"guess":3}'
  ```

- 容错演示：停止 Provider 后再访问 `random/10`，Fallback 返回 `-1`。

## 配置中心（扩展）

`springcloud-nacos-config` 模块用于演示 Nacos 配置中心：

- 控制器读取数据源配置并支持动态刷新：
  - `springcloud-nacos-config/src/main/java/edu/sbs/ms/controller/ConfigController.java:34–38`
  - `springcloud-nacos-config/src/main/java/edu/sbs/ms/config/DataSourceConfig.java:14–21`

- 启动方式：

  ```bash
  mvn -q -pl springcloud-nacos-config spring-boot:run
  ```

- 访问示例：

  ```bash
  curl "http://localhost:6003/springcloud-nacos-config/config/get"
  curl "http://localhost:6003/springcloud-nacos-config/config/length"
  ```

- 在 Nacos 控制台创建 Data ID：`springcloud-nacos-config.yml`（或与 `file-extension` 保持一致），填入 `spring.datasource.*` 与 `config.length` 等配置，变更后通过 `@RefreshScope` 自动生效。

## 负载均衡与日志

- Feign 客户端自定义负载均衡策略为轮询，并开启完整日志：
  - `springcloud-nacos-consumer/src/main/java/edu/sbs/ms/config/CustomFeignConfiguration.java:15–22,29–32`

## 端口与上下文

- Provider：`server.port=6004`，上下文路径：`/provider`
- Consumer：`server.port=6005`，上下文路径：`/consumer`
- Config：`server.port=6003`，上下文路径：`/springcloud-nacos-config`

## 常见问题

- 服务未注册到 Nacos：检查 `bootstrap.yml` 是否启用（项目中通过 `System.setProperty("spring.cloud.bootstrap.enabled", "true")`），以及 `server-addr` 与网络可达性。
- 命名空间不匹配：确保 Provider 与 Consumer 使用相同 `namespace` 与 `group`，否则无法在同一命名空间中发现服务。
- 端口冲突：修改各模块 `application.yml` 的 `server.port`。

---

通过以上步骤，你可以在本仓库中完整演示 Nacos 的服务注册与发现能力，并对比 `LoadBalancerClient + RestTemplate` 与 OpenFeign 两种调用方式的差异与适用场景。