package lukasztracz195.barnpjee.chat.client.model.service.impl;

import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lukasztracz195.barnpjee.chat.client.data.LoggedUserData;
import lukasztracz195.barnpjee.chat.client.data.Message;
import lukasztracz195.barnpjee.chat.client.data.Room;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor()
@Getter
@Builder
public class RefresherRoomListScheduledService extends ScheduledService<Integer> {

    private Map<Room, List<Message>> mapRoomsAndMessages;
    private ObservableList<Room> roomObservableList;
    private Integer numberOfCall = 0;
    private final LoggedUserData loggedUserData = LoggedUserData.getInstance();

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {

            @Override
            protected Integer call() throws Exception {
                return 1;
            }
        };
    }
}
