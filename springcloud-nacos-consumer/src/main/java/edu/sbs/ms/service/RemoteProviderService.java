package edu.sbs.ms.service;


import edu.sbs.ms.config.CustomFeignConfiguration;
import edu.sbs.ms.config.ProviderFallbackFactory;
import edu.sbs.ms.dto.ChallengeAttemptDTO;
import edu.sbs.ms.vo.ChallengeAttemptVO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//@FeignClient(url = "http://localhost:6004/provider", fallbackFactory = ProviderFallbackFactory.class)
@FeignClient(name = "provider", configuration = CustomFeignConfiguration.class, fallbackFactory = ProviderFallbackFactory.class)
//@FeignClient(name = "provider", fallbackFactory = ProviderFallbackFactory.class)
public interface RemoteProviderService {

    @GetMapping("/provider/challenge/random/{max}")
    int echoRandom(@PathVariable("max") int max);

    @PostMapping("/provider/attempts")
    ChallengeAttemptVO echoAttempts(@RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO);
}
