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
public class Result {

    private boolean success;
    private Status status;

    public static Result ok() {
        return new Result(true, Status.OK);
    }

    public static Result failure(Status status) {
        return new Result(false, status);
    }
}
