package lukasztracz195.barnpjee.chat.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class GetMessagesRequest implements Request {
    private String nick;
    private Map<UUID, Long> chatTimeMap;
    private boolean getAllMessages;

    @Override
    public Result validate() {
        if (nick == null || nick.isEmpty()) {
            return Result.failure(Status.BLANK_NICK);
        }

        return chatTimeMap.entrySet().stream()
                .filter(uuidLocalDateTimeEntry -> uuidLocalDateTimeEntry.getValue() == null)
                .findFirst()
                .map(uuidLocalDateTimeEntry -> Result.failure(Status.WRONG_DATE))
                .orElse(Result.ok());
    }
}
