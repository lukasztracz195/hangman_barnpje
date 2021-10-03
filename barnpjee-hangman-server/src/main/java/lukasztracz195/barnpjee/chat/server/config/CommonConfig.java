package lukasztracz195.barnpjee.chat.server.config;

import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    JsonObjectConverter converter() {
        return new JsonObjectConverter();
    }
}
