package lukasztracz195.barnpjee.chat.client.model.service.interfaces;

import lukasztracz195.barnpjee.chat.common.dto.request.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.RegisterResponse;

public interface RegisterClientService {

    RegisterResponse register(RegisterRequest registerRequestAsJson);
}
