package lukasztracz195.barnpjee.chat.server;

import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.response.LogInResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.LogOutResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LogInServiceTests {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LogInService logInService;

    @Autowired
    private JsonObjectConverter converter;


    @Test
    void shouldLogInWithSuccess() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);
        testUser.register();

        //when
        final LogInResponse logInResponse = testUser.login();

        //then
        Assertions.assertEquals(Status.OK, logInResponse.getStatus());
    }

    @Test
    void shouldLogInWithEmptyCredentials() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);
        testUser.register();

        //when
        final LogInResponse logInResponse = testUser.login(null, null);

        //then
        Assertions.assertEquals(Status.BLANK_NICK, logInResponse.getStatus());
    }

    @Test
    void shouldLogInWithCredentialsNotExistedUser() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);
        testUser.register();

        //when
        final LogInResponse logInResponse = testUser.login("nick", "password");

        //then
        Assertions.assertEquals(Status.WRONG_CREDENTIALS, logInResponse.getStatus());
    }

    @Test
    void shouldNotLogOutWithNullNickTest() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);
        testUser.register();

        //when
        final LogOutResponse logOutResponse = testUser.logout(null);

        //then
        Assertions.assertEquals(Status.BLANK_NICK, logOutResponse.getStatus());
    }

    @Test
    void shouldLogOutWithSuccess() {
        //given
        final TestUser testUser = new TestUser(registerService, logInService, converter);
        testUser.register();

        //when
        final LogOutResponse logOutResponse = testUser.logout();

        //then
        Assertions.assertEquals(Status.OK, logOutResponse.getStatus());
    }
}
