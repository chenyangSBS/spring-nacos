package com.yx.nacos;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("        ヾ(◍°∇°◍)ﾉﾞ    Application启动成功      ヾ(◍°∇°◍)ﾉﾞ\n" +
                "                    _\n" +
                "            "
                + "      _(_)_                           wWWWw   _\n" +
                "      @@@@       (_)@(_)   vVVVv     _     @@@@  (___) _(_)_\n" +
                "     @@()@@ wWWWw  (_)\\    (___)   _(_)_  @@()@@   Y  (_)@(_)\n" +
                "      @@@@  (___)     `|/    Y    (_)@(_)  @@@@   \\|/   (_)\\\n" +
                "       /      Y       \\|    \\|/    /(_)    \\|      |/      |\n" +
                "    \\ |     \\ |/       | / \\ | /  \\|/       |/    \\|      \\|/\n" +
                "    \\\\|//   \\\\|///  \\\\\\|//\\\\\\|/// \\|///  \\\\\\|//  \\\\|//  \\\\\\|// \n" +
                "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }
}
