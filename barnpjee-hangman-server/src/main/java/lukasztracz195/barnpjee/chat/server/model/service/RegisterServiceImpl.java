package lukasztracz195.barnpjee.chat.server.model.service;

import lombok.extern.slf4j.Slf4j;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.registration.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Result;
import lukasztracz195.barnpjee.chat.common.dto.response.registration.RegisterResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import lukasztracz195.barnpjee.chat.server.model.entity.User;
import lukasztracz195.barnpjee.chat.server.model.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    private final Logger LOG = LoggerFactory.getLogger(RegisterServiceImpl.class);
    private final UserRepository userRepository;
    private final JsonObjectConverter converter;

    public RegisterServiceImpl(UserRepository userRepository, JsonObjectConverter converter) {
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    public final String register(final String registerRequestAsJson) {
        final RegisterRequest registerRequest =
                converter.convertJsonToPOJO(registerRequestAsJson, RegisterRequest.class);
        final Result result = registerRequest.validate();
        if (!result.isSuccess()) {
            final RegisterResponse registerFailedResponse = RegisterResponse.builder()
                    .status(result.getStatus())
                    .build();
            return converter.convertPOJOToJson(registerFailedResponse);
        }
        if (userRepository.exists(registerRequest.getNick())) {
            final RegisterResponse registerFailedResponse = RegisterResponse.builder()
                    .status(Status.WRONG_USERS)
                    .build();
            return converter.convertPOJOToJson(registerFailedResponse);
        }
        userRepository.save(User.builder()
                .nick(registerRequest.getNick())
                .password(registerRequest.getPassword())
                .build());
        return converter.convertPOJOToJson(RegisterResponse.builder()
                .status(Status.OK)
                .build());
    }
}
