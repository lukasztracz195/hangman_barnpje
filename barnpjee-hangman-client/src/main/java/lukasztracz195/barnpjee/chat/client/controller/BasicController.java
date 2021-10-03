package lukasztracz195.barnpjee.chat.client.controller;

import lukasztracz195.barnpjee.chat.client.aplication.ApplicationContextManager;
import lukasztracz195.barnpjee.chat.client.aplication.ViewManager;
import lukasztracz195.barnpjee.chat.client.model.entity.User;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.ServiceInitialize;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class BasicController implements ApplicationContextManager, ServiceInitialize {

    protected ConfigurableApplicationContext applicationContext;
    protected ViewManager viewManager;
    protected User loggedUser;

    @Override
    public void setApplicationContext(ConfigurableApplicationContext context) {
        this.applicationContext = context;
        initializeServices();
    }

    @Override
    public void initializeServices() {
    }


    @Override
    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
        setResolutionOfView();
    }

    protected void switchView(String nameOfView) {
        viewManager.switchView(nameOfView);
    }

    private void setResolutionOfView(){
        viewManager.getCurrentStage().setResizable(false);
    }
}
