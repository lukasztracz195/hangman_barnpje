package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.common.constant.StateOfGame;
import lukasztracz195.barnpjee.chat.server.model.entity.Game;
import lukasztracz195.barnpjee.chat.server.model.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository {

    void save(Game game);

    Optional<Game> get(UUID uuid);

    Collection<Game> getAllGames();

    Optional<Game> getGameInStateWithUserAssigned(final StateOfGame stateOfGame, final String nickOfUser);

    Optional<Game> getGameInState(final StateOfGame stateOfGame);

    Optional<Game> getGameWithAssignedUser(final String nickOfUser);
}
