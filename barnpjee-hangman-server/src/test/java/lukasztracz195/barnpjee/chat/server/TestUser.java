package lukasztracz195.barnpjee.chat.server;

import lukasztracz195.barnpjee.chat.common.dto.request.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.LogOutRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.LogInResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.LogOutResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.RegisterResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;

import java.util.UUID;

public class TestUser {

    private final RegisterService registerService;
    private final LogInService logInService;
    private final JsonObjectConverter converter;

    private String nick;
    private String password;

    public TestUser(RegisterService registerService, LogInService logInService, JsonObjectConverter converter) {
        this.registerService = registerService;
        this.logInService = logInService;
        this.converter = converter;
        this.nick = UUID.randomUUID().toString();
        this.password = UUID.randomUUID().toString();
    }

    public RegisterResponse register() {
        return register(nick, password);
    }

    public RegisterResponse register(String nick, String password) {
        final RegisterRequest registerRequest = RegisterRequest.builder()
                .nick(nick)
                .password(password)
                .build();
        final String registerRequestAsJson = converter.convertPOJOToJson(registerRequest);
        return converter.convertJsonToPOJO(registerService.register(registerRequestAsJson), RegisterResponse.class);
    }

    public LogInResponse login() {
        return login(nick, password);
    }

    public LogInResponse login(String nick, String password) {
        final LogInRequest logInRequest = LogInRequest.builder()
                .nick(nick)
                .password(password)
                .build();
        final String logInRequestAsJson = converter.convertPOJOToJson(logInRequest);
        return converter.convertJsonToPOJO(logInService.logIn(logInRequestAsJson), LogInResponse.class);
    }

    public LogOutResponse logout() {
        return logout(nick);
    }

    public LogOutResponse logout(String nick) {
        final LogOutRequest logOutRequest = LogOutRequest.builder()
                .nick(nick)
                .build();
        final String logOutRequestAsJson = converter.convertPOJOToJson(logOutRequest);
        return converter.convertJsonToPOJO(logInService.logOut(logOutRequestAsJson), LogOutResponse.class);
    }
}
