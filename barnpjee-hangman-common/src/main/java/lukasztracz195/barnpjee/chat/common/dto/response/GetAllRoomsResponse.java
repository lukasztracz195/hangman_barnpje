package lukasztracz195.barnpjee.chat.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class GetAllRoomsResponse {
    private List<RoomResponseWithoutMessages> roomsResponses;
    private Status status;
}
