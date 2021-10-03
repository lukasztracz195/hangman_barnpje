package lukasztracz195.barnpjee.chat.server.model.service;

import lombok.extern.slf4j.Slf4j;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.LogOutRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.Result;
import lukasztracz195.barnpjee.chat.common.dto.response.LogInResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.LogOutResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import lukasztracz195.barnpjee.chat.server.model.repository.LoggedRepository;
import lukasztracz195.barnpjee.chat.server.model.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogInServiceImpl implements LogInService {

    private final UserRepository userRepository;

    private final LoggedRepository loggedRepository;

    private final JsonObjectConverter converter;

    public LogInServiceImpl(UserRepository userRepository,
                            LoggedRepository loggedRepository,
                            JsonObjectConverter converter) {
        this.userRepository = userRepository;
        this.loggedRepository = loggedRepository;
        this.converter = converter;
    }

    @Override
    public String logIn(String logInRequestAsJson) {
        final LogInRequest logInRequest = converter.convertJsonToPOJO(logInRequestAsJson, LogInRequest.class);
        final Result result = logInRequest.validate();
        if (!result.isSuccess()) {
            return converter.convertPOJOToJson(LogInResponse.builder()
                    .status(result.getStatus())
                    .build());
        }
        return converter.convertPOJOToJson(
                userRepository.get(logInRequest.getNick())
                        .filter(user -> user.getPassword().equals(logInRequest.getPassword()))
                        .map(user -> LogInResponse.builder()
                                .status(Status.OK)
                                .build())
                        .orElseGet(() -> LogInResponse.builder()
                                .status(Status.WRONG_CREDENTIALS)
                                .build()));
    }

    @Override
    public final String logOut(final String logOutRequestAsJson) {
        final LogOutRequest logOutRequest = converter.convertJsonToPOJO(logOutRequestAsJson, LogOutRequest.class);
        final Result result = logOutRequest.validate();

        if (!result.isSuccess()) {
            final LogOutResponse logOutWithForbiddenDataResponse = LogOutResponse.builder()
                    .status(result.getStatus())
                    .build();
            return converter.convertPOJOToJson(logOutWithForbiddenDataResponse);
        }

        return userRepository.get(logOutRequest.getNick()).map(user -> {
            loggedRepository.remove(logOutRequest.getNick());
            return converter.convertPOJOToJson(
                    LogOutResponse.builder()
                            .status(Status.OK)
                            .build());
        }).orElseGet(() -> converter.convertPOJOToJson(
                LogOutResponse.builder()
                        .status(Status.WRONG_USERS)
                        .build()));
    }
}
