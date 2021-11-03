package lukasztracz195.barnpjee.chat.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.server.constant.AnswerType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Answer {
    private User owner;
    private String content;
    private UUID uuidOfGame;
    private AnswerType verdict;
}
