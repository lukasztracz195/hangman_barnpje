package lukasztracz195.barnpjee.chat.common.dto.response.login;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
public class LogOutResponse {
    private Status status;
}
