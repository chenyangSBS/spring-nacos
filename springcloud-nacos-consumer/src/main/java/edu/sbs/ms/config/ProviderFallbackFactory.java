package edu.sbs.ms.config;

import edu.sbs.ms.dto.ChallengeAttemptDTO;
import edu.sbs.ms.service.RemoteProviderService;
import edu.sbs.ms.vo.ChallengeAttemptVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


// execute after an exception occurs when calling the RemoteProviderService
// @param throwable for exceptions
@Component
public class ProviderFallbackFactory implements FallbackFactory<RemoteProviderService> {

    @Override
    public RemoteProviderService create(Throwable throwable) {
        return new RemoteProviderService() {
            @Override
            public int echoRandom(int max) {
                return -1;
            }

            @Override
            public ChallengeAttemptVO echoAttempts(ChallengeAttemptDTO challengeAttemptDTO) {
                return null;
            }
        };
    }
}
