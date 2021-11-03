package lukasztracz195.barnpjee.chat.common.dto.request.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Request;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Result;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class EliminateCategoryRequest implements Request {
    private String nick;
    private UUID uuidOfGame;
    private String nameCategoryToElimination;

    @Override
    public Result validate() {
        if(nick != null && !nick.isEmpty()){
            if(uuidOfGame != null && nameCategoryToElimination != null && !nameCategoryToElimination.isEmpty()){
                return Result.ok();
            }
            return Result.failure(Status.DETECTED_WRONG_DATA);
        }
        return Result.failure(Status.BLANK_NICK);
    }
}
