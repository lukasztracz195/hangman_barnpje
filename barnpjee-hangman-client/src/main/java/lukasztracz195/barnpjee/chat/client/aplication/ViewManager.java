package lukasztracz195.barnpjee.chat.client.aplication;

import javafx.stage.Stage;

public interface ViewManager extends ApplicationManger {

    void switchView(final String nameOfView);

    Stage getCurrentStage();
}
