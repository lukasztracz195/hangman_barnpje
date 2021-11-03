package lukasztracz195.barnpjee.chat.common.dto.request.login;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Request;
import lukasztracz195.barnpjee.chat.common.dto.request.basic.Result;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LogOutRequest implements Request {
    private String nick;

    @Override
    public Result validate() {
        if (nick == null || nick.isEmpty()) {
            return Result.failure(Status.BLANK_NICK);
        }
        return Result.ok();
    }
}
