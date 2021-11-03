package lukasztracz195.barnpjee.chat.common.dto.request.registration;

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
public class RegisterRequest implements Request {
    private String nick;
    private String password;

    @Override
    public Result validate() {
        if (nick == null || nick.isEmpty()) {
            return Result.failure(Status.BLANK_NICK);
        }
        if (password == null || password.isEmpty()) {
            return Result.failure(Status.BLANK_PASSWORD);
        }
        return Result.ok();
    }
}
