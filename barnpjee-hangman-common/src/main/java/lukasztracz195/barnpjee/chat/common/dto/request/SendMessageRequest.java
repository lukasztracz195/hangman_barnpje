package lukasztracz195.barnpjee.chat.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SendMessageRequest implements Request {
    private String nick;
    private UUID roomUuid;
    private String message;

    @Override
    public Result validate() {
        if (nick == null || nick.isEmpty()) {
            return Result.failure(Status.BLANK_NICK);
        }
        if (roomUuid == null) {
            return Result.failure(Status.WRONG_ROOM_ID);
        }
        if (message == null || message.isEmpty()) {
            return Result.failure(Status.BLANK_MESSAGE);
        }
        return Result.ok();
    }
}
