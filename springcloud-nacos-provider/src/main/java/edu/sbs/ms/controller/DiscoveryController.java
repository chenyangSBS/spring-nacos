package edu.sbs.ms.controller;

import com.alibaba.nacos.api.exception.NacosException;
import edu.sbs.ms.dto.ChallengeAttemptDTO;
import edu.sbs.ms.vo.ChallengeAttemptVO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Random;

@RestController
public class DiscoveryController {

    @RequestMapping(value = "/welcome")
    @ResponseBody
    public String producer(@RequestParam String name) throws NacosException {
        return "Welcome: " + name + " !";
    }

    @RequestMapping(value = "/challenge/random/{max}")
    @ResponseBody
    public int challengeRandom(@PathVariable int max) throws NacosException {
        Random random = new Random();
        System.out.println("Current max value is " + String.valueOf(max));
        return random.nextInt(max + 1);
    }

    @PostMapping("/attempts")
    ResponseEntity<ChallengeAttemptVO> challenge(
            @RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO) throws NacosException {
        int guess = challengeAttemptDTO.getFactorA() + challengeAttemptDTO.getFactorB();
        if (guess == challengeAttemptDTO.getGuess()) {
            ChallengeAttemptVO rst = new ChallengeAttemptVO(challengeAttemptDTO.getFactorA(), challengeAttemptDTO.getFactorB(), challengeAttemptDTO.getGuess());
            return ResponseEntity.ok(rst);
        } else {
            ChallengeAttemptVO err = new ChallengeAttemptVO(challengeAttemptDTO.getFactorA(), challengeAttemptDTO.getFactorB(), -1);
            return ResponseEntity.ofNullable(err);
        }
    }
}
