package lukasztracz195.barnpjee.chat.client.config;

import lukasztracz195.barnpjee.chat.common.constant.UrlService;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

@Configuration
public class AppConfig {


    @Bean
    public HessianProxyFactoryBean registerService() {
        return getHessianBean(UrlService.REGISTRATION_SERVICE_URL, RegisterService.class);
    }

    @Bean
    public HessianProxyFactoryBean loginService() {
        return getHessianBean(UrlService.LOGIN_SERVICE_URL, LogInService.class);
    }

//    @Bean
//    public HessianProxyFactoryBean roomService() {
//        return getHessianBean(UrlService.ROOM_SERVICE_URL, RoomService.class);
//    }

    private HessianProxyFactoryBean getHessianBean(String serviceUrl, Class<?> serviceClass) {
        HessianProxyFactoryBean bean = new HessianProxyFactoryBean();
        bean.setServiceUrl(UrlService.BASE_URL + serviceUrl);
        bean.setServiceInterface(serviceClass);
        return bean;
    }
}

