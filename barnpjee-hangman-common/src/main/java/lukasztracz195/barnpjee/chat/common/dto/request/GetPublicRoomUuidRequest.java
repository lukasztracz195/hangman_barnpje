package lukasztracz195.barnpjee.chat.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class GetPublicRoomUuidRequest implements Request {
    private String nick;

    @Override
    public Result validate() {
        if (nick == null || nick.isEmpty()) {
            return Result.failure(Status.BLANK_NICK);
        }
        return Result.ok();
    }
}
