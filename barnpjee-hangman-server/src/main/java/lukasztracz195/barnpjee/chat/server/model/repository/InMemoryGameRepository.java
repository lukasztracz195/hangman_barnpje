package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.common.constant.StateOfGame;
import lukasztracz195.barnpjee.chat.server.model.entity.Game;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGameRepository implements GameRepository {

    private final Map<UUID, Game> games;

    public InMemoryGameRepository(){
        games = new ConcurrentHashMap<>();
    }

    @Override
    public void save(Game game) {
        games.putIfAbsent(game.getUuid(), game.toBuilder().build());
        if(games.containsKey(game.getUuid())){
            games.replace(game.getUuid(), game);
        }
    }

    @Override
    public Optional<Game> get(UUID uuid) {
        return Optional.ofNullable(games.get(uuid))
                .map(game -> game.toBuilder().build());
    }

    @Override
    public Collection<Game> getAllGames() {
        return games.values();
    }

    @Override
    public Optional<Game> getGameInStateWithUserAssigned(StateOfGame stateOfGame, String nickOfUser) {
        return games.values().stream()
                .filter(f->f.getStateOfGame() == stateOfGame)
                .filter(f->f.getPlayers().stream().anyMatch(p->p.getNick().equals(nickOfUser)))
                .findFirst();
    }

    @Override
    public Optional<Game> getGameInState(StateOfGame stateOfGame) {
        return games.values().stream().filter(f->f.getStateOfGame() == stateOfGame).findFirst();
    }

    @Override
    public Optional<Game> getGameWithAssignedUser(String nickOfUser) {
        return games.values().stream()
                .filter(f->f.getPlayers().stream().anyMatch(p->p.getNick().equals(nickOfUser)))
                .findFirst();
    }
}
