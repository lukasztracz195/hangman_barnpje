package lukasztracz195.barnpjee.chat.client.aplication;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lukasztracz195.barnpjee.chat.client.ClientChatApplication;
import lukasztracz195.barnpjee.chat.client.constans.FxmlViews;
import lukasztracz195.barnpjee.chat.client.controller.BasicController;
import lukasztracz195.barnpjee.chat.client.data.LoggedUserData;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.LogInClientService;
import lukasztracz195.barnpjee.chat.common.dto.request.login.LogOutRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

public class JavaFxApplication extends Application implements ViewManager {

    private static final String viewPathPattern = "/views/%s";
    private ConfigurableApplicationContext applicationContext;
    private Stage stage;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        this.applicationContext = new SpringApplicationBuilder()
                .sources(ClientChatApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        switchView(FxmlViews.MAIN_VIEW);
    }

    @Override
    public void stop() {
        final LogInClientService loginService = applicationContext.getBean(LogInClientService.class);
        final LoggedUserData loggedUserData = LoggedUserData.getInstance();
        if(loggedUserData.isUserIsLogged()){
            loginService.logOut(LogOutRequest.builder()
                    .nick(loggedUserData.getNickOfLoggedUser())
                    .build());
        }
        Platform.exit();
        SpringApplication.exit(this.applicationContext);
    }

    @Override
    public void switchView(String nameOfView) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource(String.format(viewPathPattern,nameOfView));
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            BasicController controller = loader.getController();
            controller.setApplicationContext(applicationContext);
            controller.setViewManager(this);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Stage getCurrentStage() {
        return stage;
    }
}
