package lukasztracz195.barnpjee.chat.common.dto.response;

import lombok.*;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class RegisterResponse {
    private Status status;
}
