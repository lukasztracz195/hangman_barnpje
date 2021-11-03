package lukasztracz195.barnpjee.chat.client.model.service.impl;


import lukasztracz195.barnpjee.chat.client.model.service.interfaces.RegisterClientService;
import lukasztracz195.barnpjee.chat.common.dto.request.registration.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.registration.RegisterResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterClientServiceImpl implements RegisterClientService {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private JsonObjectConverter converter;

    public RegisterResponse register(RegisterRequest registerRequestAsJson) {
        final String jsonOutput = registerService
                .register(converter.convertPOJOToJson(registerRequestAsJson));
        return converter.convertJsonToPOJO(jsonOutput, RegisterResponse.class);
    }
}
