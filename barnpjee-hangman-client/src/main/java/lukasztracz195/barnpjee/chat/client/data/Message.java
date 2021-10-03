package lukasztracz195.barnpjee.chat.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Message implements Comparable<Message>{
    private String nickOfCreator;
    private String content;
    private LocalDateTime timeOfCreation;


    @Override
    public int compareTo(Message o) {
        return timeOfCreation.compareTo(o.timeOfCreation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return nickOfCreator.equals(message.nickOfCreator) && content.equals(message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickOfCreator, content);
    }

    @Override
    public String toString() {
        return String.format("[%s]<%s>: %s", timeOfCreation, nickOfCreator, content);
    }
}
