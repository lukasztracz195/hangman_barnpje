package lukasztracz195.barnpjee.chat.client.model.service.impl;

import lukasztracz195.barnpjee.chat.client.model.service.interfaces.LogInClientService;
import lukasztracz195.barnpjee.chat.common.dto.request.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.LogOutRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.LogInResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.LogOutResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogInClientServiceImpl implements LogInClientService {

    @Autowired
    private LogInService logInService;
    @Autowired
    private JsonObjectConverter converter;

    @Override
    public LogInResponse logIn(LogInRequest logInRequest) {
        final String jsonOutput = logInService
                .logIn(converter.convertPOJOToJson(logInRequest));
        return converter.convertJsonToPOJO(jsonOutput, LogInResponse.class);
    }

    @Override
    public LogOutResponse logOut(LogOutRequest logOutRequest) {
        final String jsonOutput = logInService
                .logOut(converter.convertPOJOToJson(logOutRequest));
        return converter.convertJsonToPOJO(jsonOutput, LogOutResponse.class);
    }
}
