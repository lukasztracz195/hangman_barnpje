package lukasztracz195.barnpjee.chat.client.model.service.impl;

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
import lukasztracz195.barnpjee.chat.common.dto.response.RoomResponse;
import lukasztracz195.barnpjee.chat.common.json.LocalDataTimeConverter;

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
public class RefresherMessageListScheduledService extends ScheduledService<Integer> {

    private Map<Room, List<Message>> mapRoomsAndMessages;
    private ObservableList<Message> messagesObservableList;
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


    private Map<UUID, Long> prepareRoomTimeMap(final Map<Room, List<Message>> mapOfRoomsAndMessages) {
        final Map<UUID, Long> map = new HashMap<>();
        mapOfRoomsAndMessages.forEach((room, messages) -> messages.stream()
                .sorted()
                .findFirst().ifPresent(lastMessage -> map.put(room.getUuid(), LocalDataTimeConverter
                        .convertLocalDateTimeToEpochMilliAsLong(lastMessage.getTimeOfCreation()))));
        return map;
    }

    private List<Message> prepareMessagesFromRoomResponse(RoomResponse roomResponse) {
        return roomResponse.getMessages().stream()
                .map(f -> Message.builder()
                        .content(f.getMessage())
                        .nickOfCreator(f.getNick())
                        .timeOfCreation(f.getTime())
                        .build()).collect(Collectors.toList());
    }
}
