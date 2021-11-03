package lukasztracz195.barnpjee.chat.common.dto.request.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Request;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Result;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class NotifyThatUserIsReadyToStartGameRequest implements Request {
    private String nick;

    @Override
    public Result validate() {
        if(nick == null){
            return Result.failure(Status.DETECTED_NULL_VALUES);
        }
        if(nick.isEmpty()){
            return Result.failure(Status.BLANK_NICK);
        }
        return Result.ok();
    }
}
