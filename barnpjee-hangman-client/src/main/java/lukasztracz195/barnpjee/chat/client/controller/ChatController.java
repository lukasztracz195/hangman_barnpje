package lukasztracz195.barnpjee.chat.client.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;
import lukasztracz195.barnpjee.chat.client.constans.FxmlViews;
import lukasztracz195.barnpjee.chat.client.data.LoggedUserData;
import lukasztracz195.barnpjee.chat.client.data.Message;
import lukasztracz195.barnpjee.chat.client.data.Room;
import lukasztracz195.barnpjee.chat.client.model.service.impl.RefresherMessageListScheduledService;
import lukasztracz195.barnpjee.chat.client.model.service.impl.RefresherRoomListScheduledService;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.LogInClientService;
import lukasztracz195.barnpjee.chat.client.model.service.interfaces.RoomClientService;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.CreateRoomRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetAllRoomsRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.LogOutRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.Result;
import lukasztracz195.barnpjee.chat.common.dto.request.SendMessageRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.CreateRoomResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetAllRoomsResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.LogOutResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ChatController extends BasicController {

    @FXML
    ListView<Message> listOfMessages;

    @FXML
    ListView<Room> listOfRooms;

    @FXML
    TextField textFieldOnMessage;

    @FXML
    Button sendMessageButton;

    private RoomClientService roomClientService;
    private LogInClientService loginService;
    private LoggedUserData loggedUserData;

    private Map<Room, List<Message>> mapRoomsAndMessages = new ConcurrentHashMap<>();
    private ObservableList<Room> roomObservableList = FXCollections.observableArrayList();
    private ObservableList<Message> messagesObservableList = FXCollections.observableArrayList();
    private RefresherMessageListScheduledService serviceForDownloadMessages;
    private RefresherRoomListScheduledService serviceForDownloadRooms;
    @Override
    public void initializeServices() {
        super.initializeServices();
        loginService = applicationContext.getBean(LogInClientService.class);
        roomClientService = applicationContext.getBean(RoomClientService.class);
        loggedUserData = LoggedUserData.getInstance();
        setObservableListForView();
        roomObservableList.addAll(getAllRoomsToRoomList());
        loggedUserData.setCurrentRoom(roomObservableList.get(0));

        serviceForDownloadMessages = RefresherMessageListScheduledService.builder()
                .roomClientService(roomClientService)
                .messagesObservableList(messagesObservableList)
                .mapRoomsAndMessages(mapRoomsAndMessages)
                .build();

        serviceForDownloadRooms = RefresherRoomListScheduledService.builder()
                .roomClientService(roomClientService)
                .roomObservableList(roomObservableList)
                .mapRoomsAndMessages(mapRoomsAndMessages)
                .build();

        serviceForDownloadRooms.setDelay(Duration.seconds(3));
        serviceForDownloadRooms.setPeriod(Duration.seconds(6));
        serviceForDownloadRooms.setMaximumFailureCount(100);
        serviceForDownloadRooms.setRestartOnFailure(true);

        serviceForDownloadMessages.setDelay(Duration.seconds(3));
        serviceForDownloadMessages.setPeriod(Duration.seconds(8));
        serviceForDownloadMessages.setMaximumFailureCount(100);
        serviceForDownloadMessages.setRestartOnFailure(true);
        serviceForDownloadMessages.start();
        serviceForDownloadRooms.start();

        ReadOnlyObjectProperty<Room> property = listOfRooms.getSelectionModel().selectedItemProperty();
        property.addListener(new ChangeListener<Room>() {
            @Override
            public synchronized void changed(ObservableValue<? extends Room> observable, Room oldValue, Room newValue) {
                if (oldValue == null) {
                    oldValue = loggedUserData.getCurrentRoom();
                    final List<Message> messagesForOldSelectedRoom = new ArrayList<>(Collections.emptyList());
                    messagesForOldSelectedRoom.addAll(messagesObservableList);
                    mapRoomsAndMessages.put(oldValue, messagesForOldSelectedRoom);
                }
                if (!oldValue.equals(newValue)) {
                    loggedUserData.setCurrentRoom(newValue);
                    if (mapRoomsAndMessages.containsKey(oldValue)) {
                        mapRoomsAndMessages.get(oldValue).forEach(message -> {
                            if (messagesObservableList.stream().noneMatch(f -> f.equals(message))) {
                                messagesObservableList.add(message);
                            }
                        });
                    }
                    messagesObservableList.clear();
                    if (mapRoomsAndMessages.containsKey(newValue)) {
                        messagesObservableList.addAll(mapRoomsAndMessages.get(newValue));
                    } else {
                        mapRoomsAndMessages.put(newValue, new ArrayList());
                    }
                }
            }
        });
    }

    @FXML
    public void logOut() {
        final Result logOutResultOfValidationRequest = LogOutRequest.builder()
                .nick(loggedUserData.getNickOfLoggedUser())
                .build().validate();
        if (logOutResultOfValidationRequest.isSuccess()) {
            final LogOutResponse logOutResponse = loginService.logOut(LogOutRequest.builder()
                    .nick(loggedUserData.getNickOfLoggedUser())
                    .build());
            serviceForDownloadRooms.cancel();
            serviceForDownloadMessages.cancel();
            if (logOutResponse.getStatus() == Status.OK) {
                loggedUserData.setNickOfLoggedUser(null);
                loggedUserData.setUserIsLogged(false);
                switchView(FxmlViews.MAIN_VIEW);
            }
        }
    }

    @FXML
    public void sendMessage() {
        final String contentOfMessage = textFieldOnMessage.getText();
        if (!contentOfMessage.isEmpty() && loggedUserData.getCurrentRoom() != null) {
            final Status status = roomClientService.sendMessage(SendMessageRequest.builder()
                    .nick(loggedUserData.getNickOfLoggedUser())
                    .message(contentOfMessage)
                    .roomUuid(loggedUserData.getCurrentRoom().getUuid())
                    .build());
            if (status.equals(Status.OK)) {
                messagesObservableList.add(Message.builder()
                        .content(contentOfMessage)
                        .nickOfCreator(loggedUserData.getNickOfLoggedUser())
                        .timeOfCreation(LocalDateTime.now())
                        .build());
            } else {
                prepareAlert(status);
            }
            textFieldOnMessage.clear();
        }
    }

    @FXML
    public void addRoom() {
        final TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Createn new room by name");
        dialog.setHeaderText("Get name of room:");
        dialog.initOwner(viewManager.getCurrentStage());
        Platform.runLater(() -> {
            Optional<String> response = dialog.showAndWait();
            response.ifPresent(roomName -> {
                final CreateRoomResponse createRoomResponse = roomClientService.createRoom(CreateRoomRequest.builder()
                        .nameOfCreator(loggedUserData.getNickOfLoggedUser())
                        .nameOfRoom(roomName)
                        .build());
                if (createRoomResponse.getStatus().equals(Status.OK)) {
                    roomObservableList.add(Room.builder()
                            .uuid(createRoomResponse.getRoomUuid())
                            .name(createRoomResponse.getName())
                            .build());
                }
            });

        });
    }

    private void setObservableListForView() {
        listOfMessages.setItems(messagesObservableList);
        listOfRooms.setItems(roomObservableList);
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

    private Alert prepareAlert(Status status) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Internal Error");
        alert.setHeaderText("Detected issue with sending message");
        alert.setContentText(String.format("Description issue: %s", status.name()));
        return alert;
    }
}
