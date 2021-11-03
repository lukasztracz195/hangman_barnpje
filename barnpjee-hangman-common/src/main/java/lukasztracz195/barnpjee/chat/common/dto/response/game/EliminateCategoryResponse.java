package lukasztracz195.barnpjee.chat.common.dto.response.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class EliminateCategoryResponse {
    private Status status;
    private List<String> categoryToElimination;
    private boolean eliminationIsFinished;
    private boolean canMove;
}
