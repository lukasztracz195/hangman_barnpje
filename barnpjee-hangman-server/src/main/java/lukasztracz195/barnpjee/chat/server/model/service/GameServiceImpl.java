package lukasztracz195.barnpjee.chat.server.model.service;

import lombok.extern.slf4j.Slf4j;
import lukasztracz195.barnpjee.chat.common.constant.MoveOfPlayer;
import lukasztracz195.barnpjee.chat.common.constant.StateOfGame;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Result;
import lukasztracz195.barnpjee.chat.common.dto.request.game.EliminateCategoryRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.game.NotifyThatUserIsReadyToStartGameRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.game.EliminateCategoryResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.game.UserIsReadyToGameResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.GameService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import lukasztracz195.barnpjee.chat.server.model.entity.Category;
import lukasztracz195.barnpjee.chat.server.model.entity.Game;
import lukasztracz195.barnpjee.chat.server.model.entity.User;
import lukasztracz195.barnpjee.chat.server.model.repository.CategoryRepository;
import lukasztracz195.barnpjee.chat.server.model.repository.GameRepository;
import lukasztracz195.barnpjee.chat.server.model.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameServiceImpl implements GameService {

    private final UserRepository userRepository;

    private final GameRepository gameRepository;

    private final JsonObjectConverter converter;

    private final CategoryRepository categoryRepository;

    private final int minimalAndMaxUsersForStartGame = 2;
    private final int numberOfCategoryForUsers = 5;

    public GameServiceImpl(UserRepository userRepository,
                           GameRepository roomRepository,
                           JsonObjectConverter converter, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.gameRepository = roomRepository;
        this.converter = converter;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public String sendAnswer(String sendAnswerRequestAsJson) {
        return null;
    }

    @Override
    public String exitFromGame(String exitFromGameRequestASJson) {
        return null;
    }

    @Override
    public String getStateOfGame(String getStateOfGameRequestAsJson) {
        return null;
    }

    @Override
    public String eliminateCategory(String eliminateCategoryRequestAsJson) {
       final EliminateCategoryRequest eliminateCategoryRequest = converter
               .convertJsonToPOJO(eliminateCategoryRequestAsJson, EliminateCategoryRequest.class);
       final Result result = eliminateCategoryRequest.validate();
       if(!result.isSuccess()){
           return converter.convertPOJOToJson(EliminateCategoryResponse.builder()
                   .status(result.getStatus())
                   .build());
       }
       if(!userIsExist(eliminateCategoryRequest.getNick())){
           return converter.convertPOJOToJson(EliminateCategoryResponse.builder()
                   .status(Status.USER_ABOUT_NICK_NOT_EXIST)
                   .build());
       }
       return converter.convertPOJOToJson(prepareResponseWithEliminated(eliminateCategoryRequest.getNick(),
               eliminateCategoryRequest.getNameCategoryToElimination()));
    }

    @Override
    public boolean isYourMoveInGame(String isYourMoveInGameRequestAsJson) {
        return false;
    }

    @Override
    public String markThatUserIsReadyToGame(String informThatUserIsReadyToGameRequestAsJson) {

        NotifyThatUserIsReadyToStartGameRequest notificationRequest =
                converter.convertJsonToPOJO(informThatUserIsReadyToGameRequestAsJson,
                        NotifyThatUserIsReadyToStartGameRequest.class);
        final Result result = notificationRequest.validate();

        if (!result.isSuccess()) {
            return converter.convertPOJOToJson(UserIsReadyToGameResponse.builder()
                    .status(result.getStatus())
                    .build());
        }
        final boolean userIsExist = userIsExist(notificationRequest.getNick());
        if (!userIsExist) {
            return converter.convertPOJOToJson(UserIsReadyToGameResponse.builder()
                    .status(Status.USER_ABOUT_NICK_NOT_EXIST)
                    .build());
        }
        final boolean ifUserIsAssignedToAnyGame = ifUserIsAssignedToAnyGame(notificationRequest.getNick());
        if (!ifUserIsAssignedToAnyGame) {
            final Game game = gameRepository.getGameInState(StateOfGame.WAITING_ON_PLAYERS).orElse(createNewGame());
            userRepository.get(notificationRequest.getNick()).ifPresent(player -> {
                game.getPlayers().add(player);
                gameRepository.save(game);
            });
        }
        final boolean gameIsReadyToStart = isGameReadyToStart(notificationRequest.getNick());
        if (gameIsReadyToStart) {
            return converter.convertPOJOToJson(prepareResponseForGameReadyToStart(notificationRequest.getNick()));
        }
        return converter.convertPOJOToJson(UserIsReadyToGameResponse.builder()
                .stateOfGame(StateOfGame.WAITING_ON_PLAYERS)
                .status(Status.OK)
                .build());
    }

    @Override
    public String sendAnswerAndGetGameState(String answerRequestAsJson) {
        return null;
    }

    @Override
    public String getPasswordWithAnyGapsFilled(String getPasswordWithAnyGapsFilledRequestASJson) {
        return null;
    }

    private boolean userIsExist(final String userName) {
        return userRepository.exists(userName);
    }

    private Game createNewGame() {
        final UUID uuidOfGame = UUID.randomUUID();
        return Game.builder()
                .uuid(uuidOfGame)
                .answers(Collections.emptyList())
                .stateOfGame(StateOfGame.WAITING_ON_PLAYERS)
                .category(categoryRepository.get(numberOfCategoryForUsers))
                .players(new ArrayList<>())
                .build();
    }

    private boolean isGameReadyToStart(final String nickOfUser) {
        final AtomicBoolean atomicBooleanUsersAreReadyToStartGame = new AtomicBoolean(false);
        gameRepository.getGameInStateWithUserAssigned(StateOfGame.WAITING_ON_PLAYERS, nickOfUser)
                .ifPresent(game -> {
                    if (game.getPlayers().size() >= minimalAndMaxUsersForStartGame) {
                        atomicBooleanUsersAreReadyToStartGame.set(true);
                    }
                });
        return atomicBooleanUsersAreReadyToStartGame.get();
    }

    private boolean ifUserIsAssignedToAnyGame(final String nickOfUser) {
        return gameRepository.getGameWithAssignedUser(nickOfUser).isPresent();
    }

    private UserIsReadyToGameResponse prepareResponseForGameReadyToStart(final String nickOfUser) {
        final AtomicReference<Game> gameAtomicReference = new AtomicReference<>();
        gameRepository.getGameWithAssignedUser(nickOfUser).ifPresent(gameAtomicReference::set);
        if (Optional.ofNullable(gameAtomicReference.get()).isPresent()) {
            final Game game = gameAtomicReference.get();
            game.setNumberOfJoinedPlayers(game.getNumberOfJoinedPlayers() + 1);
            if (game.getNumberOfJoinedPlayers() >= minimalAndMaxUsersForStartGame) {
                game.setStateOfGame(StateOfGame.CHOOSE_CATEGORY);
            }
            if (game.getMoveOfPlayer() == MoveOfPlayer.NO_MOVE) {
                game.setMoveOfPlayer(randomPlayer());
            }
            gameRepository.save(game);
            final User user = userRepository.get(nickOfUser).get();
            final boolean currentUserCanMove = user.equals(playerWitchCanMove(game.getPlayers(), game.getMoveOfPlayer()));
            return UserIsReadyToGameResponse.builder()
                    .uuidOfGame(game.getUuid())
                    .stateOfGame(StateOfGame.CHOOSE_CATEGORY)
                    .category(game.getCategory().stream()
                            .map(Category::getName).collect(Collectors.toList()))
                    .canMove(currentUserCanMove)
                    .status(Status.OK)
                    .build();
        }
        return UserIsReadyToGameResponse.builder()
                .stateOfGame(StateOfGame.WAITING_ON_PLAYERS)
                .status(Status.OK)
                .build();
    }

    private MoveOfPlayer randomPlayer() {
        final Random random = new Random();
        if (random.nextDouble() > 0.5) {
            return MoveOfPlayer.MOVE_PLAYER_2;
        }
        return MoveOfPlayer.MOVE_PLAYER_1;
    }

    private User playerWitchCanMove(final List<User> players, MoveOfPlayer moveOfPlayer) {
        if (moveOfPlayer == MoveOfPlayer.MOVE_PLAYER_1) {
            return players.get(0);
        }
        return players.get(1);
    }

    private boolean ifCurrentUserCanMove(final List<User> players, MoveOfPlayer moveOfPlayer, final String nickOfCurrentUser){
        if(moveOfPlayer == MoveOfPlayer.MOVE_PLAYER_1 && nickOfCurrentUser.equals(players.get(0).getNick())){
            return true;
        }
        return moveOfPlayer == MoveOfPlayer.MOVE_PLAYER_2 && nickOfCurrentUser.equals(players.get(1).getNick());
    }

    private MoveOfPlayer getNextMove(MoveOfPlayer currentMove){
        if(currentMove == MoveOfPlayer.MOVE_PLAYER_1){
            return MoveOfPlayer.MOVE_PLAYER_2;
        }
        return MoveOfPlayer.MOVE_PLAYER_1;
    }

    private boolean eliminationIsFinished(final Game game){
        return game.getCategory().size() == 1;
    }
    private EliminateCategoryResponse prepareResponseWithEliminated(final String nickOfUser, final String nameCategoryToEliminate){
        final AtomicReference<EliminateCategoryResponse> atomicReference = new AtomicReference<>(EliminateCategoryResponse.builder()
                .status(Status.CANNOT_ELIMINATE_CATEGORY)
                .build());
        gameRepository.getGameWithAssignedUser(nickOfUser)
                .ifPresent(game->{
                    if(!eliminationIsFinished(game)){
                        game.getCategory().remove(Category.builder()
                                .name(nameCategoryToEliminate)
                                .build());
                        game.setMoveOfPlayer(getNextMove(game.getMoveOfPlayer()));
                        gameRepository.save(game);
                    }
                    atomicReference.set(EliminateCategoryResponse.builder()
                            .categoryToElimination(game.getCategory().stream()
                                    .map(Category::getName)
                                    .collect(Collectors.toList()))
                            .eliminationIsFinished(eliminationIsFinished(game))
                            .canMove(ifCurrentUserCanMove(game.getPlayers(), game.getMoveOfPlayer(), nickOfUser))
                            .status(Status.OK)
                            .build());
                });
        return atomicReference.get();
    }



}

