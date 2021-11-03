package lukasztracz195.barnpjee.chat.client.model.service.interfaces;

import lukasztracz195.barnpjee.chat.common.dto.request.registration.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.registration.RegisterResponse;

public interface RegisterClientService {

    RegisterResponse register(RegisterRequest registerRequestAsJson);
}
