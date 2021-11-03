package lukasztracz195.barnpjee.chat.server.config;


import lukasztracz195.barnpjee.chat.common.constant.UrlService;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.interfaces.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.remoting.support.RemoteExporter;

@Configuration
public class AppConfig {

    private final RegisterService registerService;
    private final LogInService logInService;
    private final GameService roomService;

    public AppConfig(RegisterService registerService, LogInService logInService, GameService roomService) {
        this.registerService = registerService;
        this.logInService = logInService;
        this.roomService = roomService;
    }

    @Bean(name = UrlService.LOGIN_SERVICE_URL)
    RemoteExporter getLogInService() {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(logInService);
        exporter.setServiceInterface(LogInService.class);
        return exporter;
    }

    @Bean(name = UrlService.REGISTRATION_SERVICE_URL)
    RemoteExporter getRegistrationService() {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(registerService);
        exporter.setServiceInterface(RegisterService.class);
        return exporter;
    }

    @Bean(name = UrlService.ROOM_SERVICE_URL)
    RemoteExporter getRoomService() {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(roomService);
        exporter.setServiceInterface(GameService.class);
        return exporter;
    }
}

