package lukasztracz195.barnpjee.chat.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Request;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Result;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SendAnswerRequest implements Request {
    private String nameOfCreator;
    private UUID gameId;
    private String content;

    @Override
    public Result validate() {
        if(content.isEmpty()){
            return Result.failure(Status.BLANK_ANSWER);
        }
        return Result.ok();
    }
}
