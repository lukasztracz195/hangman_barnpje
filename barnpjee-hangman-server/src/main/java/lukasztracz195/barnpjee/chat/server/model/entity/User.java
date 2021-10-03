package lukasztracz195.barnpjee.chat.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User implements Serializable {

    private String nick;

    private String password;

    private List<UUID> rooms;

    public void addChat(UUID chat) {
        rooms.add(chat);
    }

}
