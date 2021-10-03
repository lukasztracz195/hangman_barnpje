package lukasztracz195.barnpjee.chat.client.model.entity;

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
    private String uuid;
    private LocalDateTime timeOfCreation;
    private LocalDateTime timeOfReceiving;
    private String content;
    private User owner;

    @Override
    public int compareTo(Message o) {
        return timeOfCreation.compareTo(o.timeOfCreation);
    }
}
