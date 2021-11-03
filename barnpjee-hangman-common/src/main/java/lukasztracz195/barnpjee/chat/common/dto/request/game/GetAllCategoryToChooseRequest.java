package lukasztracz195.barnpjee.chat.common.dto.request.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class GetAllCategoryToChooseRequest {
    private String nick;
    private UUID uuidOfGame;
}
