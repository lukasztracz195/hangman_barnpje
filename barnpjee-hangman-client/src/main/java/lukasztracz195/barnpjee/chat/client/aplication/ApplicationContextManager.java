package lukasztracz195.barnpjee.chat.client.aplication;

import org.springframework.context.ConfigurableApplicationContext;

public interface ApplicationContextManager {

    void setApplicationContext(ConfigurableApplicationContext context);

    void setViewManager(ViewManager viewManager);
}
