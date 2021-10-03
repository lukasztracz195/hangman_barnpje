package lukasztracz195.barnpjee.chat.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class Message implements Comparable<Message> {

    private LocalDateTime timeOfCreation;
    private String content;
    private String nick;

    @Override
    public int compareTo(Message o) {
        return timeOfCreation.compareTo(o.timeOfCreation);
    }
}
