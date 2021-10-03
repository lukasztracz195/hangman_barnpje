package lukasztracz195.barnpjee.chat.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.common.dto.Status;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateRoomRequest implements Request {
    private String nameOfCreator;
    private String nameOfRoom;
    @Override
    public Result validate() {
        return Result.ok();
    }
}
