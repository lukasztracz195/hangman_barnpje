package lukasztracz195.barnpjee.chat.client;


import javafx.application.Application;
import lukasztracz195.barnpjee.chat.client.aplication.JavaFxApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientChatApplication {

    public static void main(String[] args)
    {
        Application.launch(JavaFxApplication.class, args);
    }

}
