package lukasztracz195.barnpjee.chat.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Room {

    private UUID uuid;
    private List<Message> messages;
    private String name;

    public void addMessage(String nick, String message) {
        messages.add(Message.builder()
                .nick(nick)
                .content(message)
                .timeOfCreation(LocalDateTime.now())
                .build());
        messages.sort(Message::compareTo);
    }

    public Room getChatAfter(LocalDateTime localDateTime) {
        return this.toBuilder()
                .messages(
                        messages.stream()
                                .filter(message -> message.getTimeOfCreation().isAfter(localDateTime))
                                .collect(Collectors.toList()))
                .build();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return uuid.equals(room.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
