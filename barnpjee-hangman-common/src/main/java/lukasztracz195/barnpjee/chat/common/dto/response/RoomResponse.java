package lukasztracz195.barnpjee.chat.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RoomResponse {
    private UUID uuid;
    private String nameOfRoom;
    private List<MessageResponse> messages;
    private Status status;
}
