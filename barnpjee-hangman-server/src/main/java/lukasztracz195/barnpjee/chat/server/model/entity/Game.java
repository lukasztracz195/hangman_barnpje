package lukasztracz195.barnpjee.chat.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.constant.MoveOfPlayer;
import lukasztracz195.barnpjee.chat.common.constant.StateOfGame;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Game {

    private UUID uuid;
    private List<Category> category;
    private List<User> players;
    private String password;
    private String passwordWithGaps;
    private List<Answer> answers;
    private StateOfGame stateOfGame;
    private MoveOfPlayer moveOfPlayer = MoveOfPlayer.NO_MOVE;
    private boolean allPlayersJoined;
    private int numberOfJoinedPlayers;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return uuid.equals(game.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
