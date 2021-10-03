package lukasztracz195.barnpjee.chat.client.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
public class Chat {

    private User owner;
    private List<Message> messageSortedMap = new ArrayList<>();
    private List<User> users;

}
