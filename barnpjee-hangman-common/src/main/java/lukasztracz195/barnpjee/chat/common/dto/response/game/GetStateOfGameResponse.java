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
public class GetStateOfGameResponse {
    private Status status;
    private UUID uuidOfGame;
    private List<String> availableCategory;
    private StateOfGame stateOfGame;
    private boolean canMove;
    private String passwordWithGaps;
}
