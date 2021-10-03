package lukasztracz195.barnpjee.chat.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lukasztracz195.barnpjee.chat.client.constans.FxmlViews;
import org.springframework.stereotype.Component;


@Component
public class RegistrationLoginController extends BasicController {

    @FXML
    Button signInButton;

    @FXML
    Button logInButton;

    @Override
    public void initializeServices() {
        super.initializeServices();
    }

    @FXML
    public void switchToRegistration() {
        switchView(FxmlViews.REGISTRATION_VIEW);
    }


    @FXML
    public void switchToLogin() {
        switchView(FxmlViews.LOGIN_VIEW);
    }
}
