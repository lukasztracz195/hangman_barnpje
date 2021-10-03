package lukasztracz195.barnpjee.chat.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lukasztracz195.barnpjee.chat.client.constans.FxmlViews;
import lukasztracz195.barnpjee.chat.client.data.LoggedUserData;
import lukasztracz195.barnpjee.chat.client.model.entity.User;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.LogInClientService;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.Result;
import lukasztracz195.barnpjee.chat.common.dto.response.LogInResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginController extends BasicController {


    private final Map<Status, String> mapStatusPerMessage = new HashMap<Status, String>() {{
        put(Status.BLANK_NICK, "Username empty!");
        put(Status.BLANK_PASSWORD, "Password empty!");
    }};
    @FXML
    Button logInButton;
    @FXML
    Button backButton;
    @FXML
    TextField nickTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    private LogInClientService loginService;
    private LoggedUserData loggedUserData;
    @Override
    public void initializeServices() {
        super.initializeServices();
        loginService = applicationContext.getBean(LogInClientService.class);
        loggedUserData = LoggedUserData.getInstance();
    }

    @FXML
    public void logIn() {
        final Result resultOfValidationRequest = LogInRequest.builder()
                .nick(nickTextField.getText())
                .password(passwordField.getText())
                .build().validate();
        if(resultOfValidationRequest.isSuccess()){
            final LogInResponse logInResponse = loginService.logIn(LogInRequest.builder()
                    .nick(nickTextField.getText())
                    .password(passwordField.getText())
                    .build());
            if (logInResponse.getStatus() == Status.OK) {
                loggedUserData.setNickOfLoggedUser(nickTextField.getText());
                loggedUserData.setUserIsLogged(true);
                switchView(FxmlViews.CHAT_VIEW);
            } else {
                if (mapStatusPerMessage.containsKey(logInResponse.getStatus())) {
                    errorLabel.setText(mapStatusPerMessage.get(logInResponse.getStatus()));
                }
            }
        }else{
            errorLabel.setText(mapStatusPerMessage.get(resultOfValidationRequest.getStatus()));
        }
    }

    @FXML
    public void back() {
        switchView(FxmlViews.MAIN_VIEW);
    }
}
