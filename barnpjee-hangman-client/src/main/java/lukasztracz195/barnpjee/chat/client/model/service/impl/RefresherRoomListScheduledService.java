package lukasztracz195.barnpjee.chat.client.model.service.impl;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lukasztracz195.barnpjee.chat.client.data.LoggedUserData;
import lukasztracz195.barnpjee.chat.client.data.Message;
import lukasztracz195.barnpjee.chat.client.data.Room;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.RoomClientService;
import lukasztracz195.barnpjee.chat.common.dto.request.GetAllRoomsRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetMessagesRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.GetAllRoomsResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetMessagesResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.RoomResponse;
import lukasztracz195.barnpjee.chat.common.json.LocalDataTimeConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor()
@Getter
@Setter
@Builder
public class RefresherRoomListScheduledService extends ScheduledService<Integer> {

    private Map<Room, List<Message>> mapRoomsAndMessages;
    private ObservableList<Room> roomObservableList;
    private RoomClientService roomClientService;
    private Integer numberOfCall = 0;
    private final LoggedUserData loggedUserData = LoggedUserData.getInstance();

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>() {

            @Override
            protected Integer call() throws Exception {
                System.out.println("Start Room tread");
                final Room currentRoom = loggedUserData.getCurrentRoom();
                Platform.runLater(() -> {
                    System.out.println("Start Room tread run Later");
                    getAllRoomsToRoomList().forEach(room -> {
                        if (roomObservableList.stream().noneMatch(f -> f.equals(room))) {
                            roomObservableList.add(room);
                        }
                        System.out.println("Stop Room tread run Later");
                    });
                });
                numberOfCall++;
                System.out.println("Stop Room tread");
                return numberOfCall;
            }
        };
    }

    private List<Room> getAllRoomsToRoomList() {
        final GetAllRoomsResponse getAllRoomsResponse = roomClientService.getAllRooms(GetAllRoomsRequest.builder()
                .nick(loggedUserData.getNickOfLoggedUser())
                .build());

        return getAllRoomsResponse.getRoomsResponses().stream().map(f -> Room.builder()
                .uuid(f.getUuid())
                .name(f.getNameOfRoom())
                .build()).collect(Collectors.toList());
    }
}
