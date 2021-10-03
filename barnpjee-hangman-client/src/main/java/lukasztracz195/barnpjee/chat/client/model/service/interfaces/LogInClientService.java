package lukasztracz195.barnpjee.chat.client.model.service.interfaces;

import lukasztracz195.barnpjee.chat.common.dto.request.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.LogOutRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.LogInResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.LogOutResponse;

public interface LogInClientService {


    LogInResponse logIn(LogInRequest logInRequest);

    LogOutResponse logOut(LogOutRequest logOutRequest);
}
