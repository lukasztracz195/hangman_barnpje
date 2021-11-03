package lukasztracz195.barnpjee.chat.common.dto.response.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.constant.StateOfGame;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.util.List;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserIsReadyToGameResponse {
    private StateOfGame stateOfGame;
    private UUID uuidOfGame;
    private Status status;
    private List<String> category;
    private boolean canMove;
}
