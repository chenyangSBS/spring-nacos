package edu.sbs.ms.controller;

import edu.sbs.ms.config.ProviderFallbackFactory;
import edu.sbs.ms.dto.ChallengeAttemptDTO;
import edu.sbs.ms.service.RemoteProviderService;
import edu.sbs.ms.vo.ChallengeAttemptVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// rest request via feign
@RestController
public class FeignConsumerController {

    @Autowired
    private RemoteProviderService remoteProviderService;


    @GetMapping("/random/{max}")
    public int doRestEcho(@PathVariable("max") int max){
        // remote request via feign
        return remoteProviderService.echoRandom(max + 100);
    }

    @GetMapping("/attempts")
    public ChallengeAttemptVO doRestEcho(@RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO){
        // remote request via feign
        return remoteProviderService.echoAttempts(challengeAttemptDTO);
    }
}
