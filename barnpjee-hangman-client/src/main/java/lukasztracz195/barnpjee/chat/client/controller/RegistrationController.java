package lukasztracz195.barnpjee.chat.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lukasztracz195.barnpjee.chat.client.constans.FxmlViews;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.RegisterClientService;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.Result;
import lukasztracz195.barnpjee.chat.common.dto.response.RegisterResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistrationController extends BasicController {

    private final Map<Status, String> mapStatusPerMessage = new HashMap<Status, String>() {{
        put(Status.BLANK_NICK, "Username empty!");
        put(Status.BLANK_PASSWORD, "Password empty!");
        put(Status.WRONG_USERS, "User about this nick already exist!");
    }};
    @FXML
    Button signInButton;
    @FXML
    Button backButton;
    @FXML
    TextField nickTextField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    private RegisterClientService registerClientServiceImpl;

    @Override
    public void initializeServices() {
        super.initializeServices();
        registerClientServiceImpl = applicationContext.getBean(RegisterClientService.class);
    }

    @FXML
    public void signIn() {
        final Result resultOfRequestValidation = RegisterRequest.builder()
                .nick(nickTextField.getText())
                .password(passwordField.getText())
                .build().validate();
        if (resultOfRequestValidation.isSuccess()) {
            final RegisterResponse registerResponse = registerClientServiceImpl.register(RegisterRequest.builder()
                    .nick(nickTextField.getText())
                    .password(
                            passwordField.getText())
                    .build());
            if (registerResponse.getStatus() == Status.OK) {
                switchView(FxmlViews.LOGIN_VIEW);
            } else {
                if (mapStatusPerMessage.containsKey(registerResponse.getStatus())) {
                    errorLabel.setText(mapStatusPerMessage.get(registerResponse.getStatus()));
                }
            }
        }else{
            errorLabel.setText(mapStatusPerMessage.get(resultOfRequestValidation.getStatus()));
        }
    }

    @FXML
    public void back() {
        switchView(FxmlViews.MAIN_VIEW);
    }

}
