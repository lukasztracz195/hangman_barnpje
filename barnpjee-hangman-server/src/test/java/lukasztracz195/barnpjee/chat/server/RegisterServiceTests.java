package lukasztracz195.barnpjee.chat.server;

import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.response.registration.RegisterResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegisterServiceTests {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LogInService logInService;

    @Autowired
    private JsonObjectConverter converter;

    @Test
    void shouldRegisterUserCorrectOneTimeWithTheSameCredentials() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);

        //when
        final RegisterResponse registerResponseOne = testUser.register();
        final RegisterResponse registerResponseTwo = testUser.register();

        //then
        Assertions.assertEquals(Status.OK, registerResponseOne.getStatus());
        Assertions.assertEquals(Status.WRONG_USERS, registerResponseTwo.getStatus());
    }

    @Test
    void shouldNotAllowOnCreateNewUserWithEmptyNick() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);

        //when
        final RegisterResponse registerResponseOne = testUser.register(null, "password");

        //then
        Assertions.assertEquals(Status.BLANK_NICK, registerResponseOne.getStatus());
    }

    @Test
    void shouldNotAllowOnCreateNewUserWithEmptyPassword() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);

        //when
        final RegisterResponse registerResponseOne = testUser.register("nick", null);

        //then
        Assertions.assertEquals(Status.BLANK_PASSWORD, registerResponseOne.getStatus());
    }

    @Test
    void shouldNotAllowOnCreateNewUserWithEmptyCredentials() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);

        //when
        final RegisterResponse registerResponseOne = testUser.register(null, null);

        //then
        Assertions.assertEquals(Status.BLANK_NICK, registerResponseOne.getStatus());
    }
}
