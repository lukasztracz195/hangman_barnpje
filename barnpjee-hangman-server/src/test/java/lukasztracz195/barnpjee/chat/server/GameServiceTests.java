package lukasztracz195.barnpjee.chat.server;

import lukasztracz195.barnpjee.chat.common.constant.MoveOfPlayer;
import lukasztracz195.barnpjee.chat.common.constant.StateOfGame;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.game.EliminateCategoryRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.game.NotifyThatUserIsReadyToStartGameRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.login.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.registration.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.game.EliminateCategoryResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.game.UserIsReadyToGameResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.GameService;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import lukasztracz195.barnpjee.chat.server.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
public class GameServiceTests {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LogInService logInService;

    @Autowired
    private GameService gameService;

    @Autowired
    private JsonObjectConverter converter;

    final User userA = User.builder()
            .nick("Adam427")
            .password("12345")
            .build();

    final User userB = User.builder()
            .nick("Gosia251")
            .password("12345")
            .build();

    private final static String nameOfCreatedRoom = "Private";
    private final static String message = "Hello";

    @Test
    void shouldUserNotifiedThatIsReadyToStarGame() {
        //given
        registerUser(userA);
        logInUser(userA);
        //when
        UserIsReadyToGameResponse userIsReadyToGameResponse = notifyThatUserIsReadyAndGetResponse(userA);
        //then
        Assertions.assertEquals(Status.OK, userIsReadyToGameResponse.getStatus(), "Status is not ok");
        Assertions.assertEquals(userIsReadyToGameResponse.getStateOfGame(), StateOfGame.WAITING_ON_PLAYERS);
    }

    @Test
    void shouldUsersNotifiedThatAreReadyToStartGameAndShouldReceiveThatGameIsStarted() {
        //given
        registerUsers(Arrays.asList(userA, userB));
        logInUsers(Arrays.asList(userA, userB));
        //when
        UserIsReadyToGameResponse firstResponseForUserA = notifyThatUserIsReadyAndGetResponse(userA);
        UserIsReadyToGameResponse firstResponseForUserB = notifyThatUserIsReadyAndGetResponse(userB);
        UserIsReadyToGameResponse secondResponseForUserA = notifyThatUserIsReadyAndGetResponse(userA);
        //then
        Arrays.asList(firstResponseForUserA, firstResponseForUserB,
                        secondResponseForUserA)
                .forEach(response -> Assertions.assertEquals(Status.OK, response.getStatus(),
                        "Status is not ok"));
        Assertions.assertEquals(StateOfGame.WAITING_ON_PLAYERS, firstResponseForUserA.getStateOfGame());
        Assertions.assertEquals(StateOfGame.CHOOSE_CATEGORY, firstResponseForUserB.getStateOfGame());
        Assertions.assertEquals(StateOfGame.CHOOSE_CATEGORY, secondResponseForUserA.getStateOfGame());
        final UUID uuidOfGame = firstResponseForUserB.getUuidOfGame();
        Assertions.assertEquals(uuidOfGame, secondResponseForUserA.getUuidOfGame());
        Assertions.assertFalse(firstResponseForUserB.getCategory().isEmpty());
        Assertions.assertFalse(secondResponseForUserA.getCategory().isEmpty());
        Assertions.assertEquals(firstResponseForUserB.getCategory(), secondResponseForUserA.getCategory());
    }

    @Test
    public void shouldUsersStartGameAndAndOneShouldEliminateCategory() {
        //given
        final List<User> users = Arrays.asList(userA, userB);
        registerUsers(users);
        logInUsers(users);
        Map<User, UserIsReadyToGameResponse> responsesMap = startGameByUsersAndGetResponses(userA, userB);
        List<String> categoryToElimination = responsesMap.get(userA).getCategory();
        final UUID uuidOfGame = responsesMap.get(userA).getUuidOfGame();
        final User userWitchShouldDoMove = getUserWitchIsAvailableToMove(responsesMap);

        //when
        EliminateCategoryResponse response = eliminateCategory(uuidOfGame, categoryToElimination, userWitchShouldDoMove);
        //then
        Assertions.assertEquals(Status.OK,response.getStatus());
        Assertions.assertEquals(categoryToElimination.size()-1, response.getCategoryToElimination().size());
        Assertions.assertFalse(response.isCanMove());
        Assertions.assertFalse(response.isEliminationIsFinished());

    }

    private Map<User, UserIsReadyToGameResponse> startGameByUsersAndGetResponses(final User userA, final User userB) {
        final Map<User, UserIsReadyToGameResponse> responsesMap = new HashMap<>();
        notifyThatUserIsReadyAndGetResponse(userA);
        responsesMap.put(userB, notifyThatUserIsReadyAndGetResponse(userB));
        responsesMap.put(userA, notifyThatUserIsReadyAndGetResponse(userA));
        return responsesMap;
    }

    private void registerUser(User user) {
        registerService.register(converter.convertPOJOToJson(
                RegisterRequest.builder()
                        .nick(user.getNick())
                        .password(user.getPassword())
                        .build()));
    }

    private void registerUsers(final List<User> usersToRegistration) {
        usersToRegistration.forEach(this::registerUser);
    }

    private void logInUsers(final List<User> usersToLogIn) {
        usersToLogIn.forEach(this::logInUser);
    }

    private void logInUser(User user) {
        logInService.logIn(converter.convertPOJOToJson(LogInRequest.builder()
                .nick(user.getNick())
                .password(user.getPassword()).build()));
    }

    private UserIsReadyToGameResponse notifyThatUserIsReadyAndGetResponse(User user) {
        final String requestAsJson = converter.convertPOJOToJson(
                NotifyThatUserIsReadyToStartGameRequest.builder()
                        .nick(user.getNick())
                        .build());
        return converter
                .convertJsonToPOJO(gameService.markThatUserIsReadyToGame(requestAsJson),
                        UserIsReadyToGameResponse.class);
    }

    private String randomCategoryToElimination(final List<String> category) {
        Random rand = new Random();
        return category.get(rand.nextInt(category.size()));
    }

    private EliminateCategoryResponse eliminateCategory(final UUID uuidOfGame, final List<String> categoryList, final User user) {
        final String categoryToEliminate = randomCategoryToElimination(categoryList);
        return converter.convertJsonToPOJO(gameService.eliminateCategory(converter.convertPOJOToJson(EliminateCategoryRequest.builder()
                .nameCategoryToElimination(categoryToEliminate)
                .nick(user.getNick())
                .uuidOfGame(uuidOfGame)
                .build())), EliminateCategoryResponse.class);
    }

    private User getUserWitchIsAvailableToMove(Map<User, UserIsReadyToGameResponse> responses) {
        final AtomicReference<User> userAtomicReference = new AtomicReference<>();
        responses.forEach((user, response) -> {
            if (response.isCanMove()) {
                userAtomicReference.set(user);
            }
        });
        return userAtomicReference.get();
    }
}
