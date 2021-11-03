package lukasztracz195.barnpjee.chat.client.model.service.interfaces;

import lukasztracz195.barnpjee.chat.common.dto.request.login.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.login.LogOutRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.login.LogInResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.login.LogOutResponse;

public interface LogInClientService {


    LogInResponse logIn(LogInRequest logInRequest);

    LogOutResponse logOut(LogOutRequest logOutRequest);
}
