package lukasztracz195.barnpjee.chat.common.interfaces;

public interface LogInService {

    String logIn(String logInRequestAsJson);

    String logOut(String logOutRequestAsJson);
}
