package lukasztracz195.barnpjee.chat.client;

import lombok.SneakyThrows;
import lukasztracz195.barnpjee.chat.client.model.service.impl.RegisterClientServiceImpl;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.RegisterResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
class SpringBootClientApplicationTests {

    @MockBean
    private RegisterService registerService;

    @Autowired
    private RegisterClientServiceImpl registerClientServiceImpl;

    @Autowired
    private JsonObjectConverter converter;

    @SneakyThrows
    @BeforeEach
    public void before() {
        Field registerServiceField = RegisterClientServiceImpl.class.getDeclaredField("registerService");
        registerServiceField.setAccessible(true);
        registerServiceField.set(registerClientServiceImpl, this.registerService);
    }

    @Test
    void registerUser() {
        //given
        final String nick = UUID.randomUUID().toString();
        final RegisterRequest registerRequest = RegisterRequest.builder()
                .nick(nick)
                .build();
        when(registerService.register(converter.convertPOJOToJson(registerRequest)))
                .thenReturn(converter.convertPOJOToJson(RegisterResponse.builder()
                        .status(Status.OK).build()));
        //when
        RegisterResponse registerResponseOne = registerClientServiceImpl.register(registerRequest);
        //then
        Assertions.assertEquals(Status.OK, registerResponseOne.getStatus());
    }
}
