package lukasztracz195.barnpjee.chat.server;

import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.CreateRoomRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetAllRoomsRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetMessagesRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetPublicRoomUuidRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.LogInRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.RegisterRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.SendMessageRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.CreateRoomResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetAllRoomsResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetMessagesResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetPublicUuidResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.SendMessageResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.LogInService;
import lukasztracz195.barnpjee.chat.common.interfaces.RegisterService;
import lukasztracz195.barnpjee.chat.common.interfaces.RoomService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import lukasztracz195.barnpjee.chat.server.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
public class RoomServiceTests {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LogInService logInService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private JsonObjectConverter converter;

    final User userA = User.builder()
            .nick("Adam427")
            .password("12345")
            .build();

    final User userB = User.builder()
            .nick("Gosia251")
            .password("12345")
            .build();

    private final static String nameOfCreatedRoom = "Private";
    private final static String message = "Hello";

    @Test
    void shouldCreateRoomByUser() {
        //given
        registerUser(userA);
        logInUser(userA);
        //when
        final String createRoomJsonRequest = converter.convertPOJOToJson(CreateRoomRequest.builder()
                .nameOfRoom("room")
                .nameOfCreator(userA.getNick())
                .build());
        CreateRoomResponse createRoomResponse = converter.convertJsonToPOJO(roomService.createRoom(createRoomJsonRequest),
                CreateRoomResponse.class);
        //then
        Assertions.assertEquals(Status.OK, createRoomResponse.getStatus(), "Status is not ok");
        Assertions.assertNotNull(createRoomResponse.getRoomUuid());
    }

    @Test
    void shouldReceiveUuidForPublicRoom() {
        //when
        registerUser(userA);
        logInUser(userA);
        //then
        final String getUuidForPublicRoomRequest = converter.convertPOJOToJson(GetPublicRoomUuidRequest.builder()
                .nick(userA.getNick())
                .build());

        final GetPublicUuidResponse getPublicUuidResponse =
                converter.convertJsonToPOJO(roomService.getPublicRoomUuid(getUuidForPublicRoomRequest),
                        GetPublicUuidResponse.class);

        Assertions.assertEquals(Status.OK, getPublicUuidResponse.getStatus(), "Status is not ok");
        Assertions.assertNotNull(getPublicUuidResponse.getPublicRoomUuid());
    }

    @Test
    void shouldSendingMessageByUserForGetPublicRoomUuid() {
        //given
        registerUser(userA);
        logInUser(userA);
        final UUID uuidForPublicRoom = getUuidForPublicRoomByUser(userA);

        //when
        final String sendingMessageRequest = converter.convertPOJOToJson(SendMessageRequest.builder()
                .nick(userA.getNick())
                .message(message)
                .roomUuid(uuidForPublicRoom)
                .build());

        final String sendingMessageResponseAsJson = roomService.sendMessage(sendingMessageRequest);
        final SendMessageResponse sendMessageResponse = converter.convertJsonToPOJO(sendingMessageResponseAsJson,
                SendMessageResponse.class);
        //then
        Assertions.assertEquals(Status.OK, sendMessageResponse.getStatus(), "Status is not ok");
    }

    @Test
    void shouldSendingMessageByUserForNewCreatedRoom() {
        //given
        registerUser(userA);
        logInUser(userA);
        final UUID uuidOfCreatedRoom = createRoomByUserAndGetItUuid(userA);
        //when
        final String sendingMessageRequest = converter.convertPOJOToJson(SendMessageRequest.builder()
                .nick(userA.getNick())
                .message(message)
                .roomUuid(uuidOfCreatedRoom)
                .build());

        final String sendingMessageResponseAsJson = roomService.sendMessage(sendingMessageRequest);
        final SendMessageResponse sendMessageResponse = converter.convertJsonToPOJO(sendingMessageResponseAsJson,
                SendMessageResponse.class);
        //then
        Assertions.assertEquals(Status.OK, sendMessageResponse.getStatus(), "Status is not ok");
    }

    @Test
    void shouldRegisterAndLoginTwoUsersAndOneWriteMessageForPublicRoomAndSecondShouldReceiveIt() {
        //given
        registerUser(userA);
        logInUser(userA);
        registerUser(userB);
        logInUser(userB);
        final UUID uuidOfPublicRoom = getUuidForPublicRoomByUser(userA);
        sendMessageByUserToRoomByUuid(userA, message, uuidOfPublicRoom);
        final Map<UUID, Long> chatTimeMap = new HashMap<UUID, Long>() {{
            put(uuidOfPublicRoom, LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        }};
        //when

        final String getMessagesRequestASJson = converter.convertPOJOToJson(GetMessagesRequest.builder()
                .nick(userB.getNick())
                .chatTimeMap(chatTimeMap)
                .build());
        final GetMessagesResponse getMessagesResponse = converter.convertJsonToPOJO(
                roomService.getMessages(getMessagesRequestASJson),
                GetMessagesResponse.class);
        //then
        Assertions.assertEquals(Status.OK, getMessagesResponse.getStatus(), "Status is not ok");
        Assertions.assertTrue(getMessagesResponse.getRoomsResponses().stream()
                        .anyMatch(f -> f.getUuid().equals(uuidOfPublicRoom)),
                "Not found UUID of public room");
        Assertions.assertTrue(getMessagesResponse.getRoomsResponses().stream()
                        .anyMatch(f -> f.getUuid().equals(uuidOfPublicRoom)),
                "Not found messages from public room");

        Assertions.assertTrue(getMessagesResponse.getRoomsResponses().stream()
                .filter(f -> f.getUuid().equals(uuidOfPublicRoom))
                .anyMatch(f -> f.getStatus().equals(Status.OK)));
        Assertions.assertTrue(
                getMessagesResponse.getRoomsResponses().stream()
                        .filter(f -> f.getUuid().equals(uuidOfPublicRoom))
                        .anyMatch(f -> f.getMessages().stream()
                                .filter(f2 -> f2.getNick().equals(userA.getNick()))
                                .anyMatch(f3 -> f3.getMessage().equals(message))),
                "Not found in received messages by userB message sent by userA");


    }

    @Test
    void shouldNotCanSendMessageUnknownUserWithWrongUuidOfPublicRoom(){
        //given
        final User unknownUser = User.builder()
                .nick("Ala355")
                .password("12345")
                .build();
        //when
        final String sendingMessageRequest = converter.convertPOJOToJson(SendMessageRequest.builder()
                .nick(unknownUser.getNick())
                .message(message)
                .roomUuid(UUID.randomUUID())
                .build());
        //then
        final String sendingMessageResponseAsJson = roomService.sendMessage(sendingMessageRequest);
        final SendMessageResponse sendMessageResponse = converter.convertJsonToPOJO(sendingMessageResponseAsJson,
                SendMessageResponse.class);
        Assertions.assertEquals(Status.USER_ABOUT_NICK_NOT_EXIST, sendMessageResponse.getStatus(), "Status is wrong");
    }

    @Test
    void shouldNotCanSendMessageWithWrongUuidOfPublicRoom(){
        //given
        registerUser(userA);
        logInUser(userA);
        //when
        final String sendingMessageRequest = converter.convertPOJOToJson(SendMessageRequest.builder()
                .nick(userA.getNick())
                .message(message)
                .roomUuid(UUID.randomUUID())
                .build());

        final String sendingMessageResponseAsJson = roomService.sendMessage(sendingMessageRequest);
        final SendMessageResponse sendMessageResponse = converter.convertJsonToPOJO(sendingMessageResponseAsJson,
                SendMessageResponse.class);
        //then
        Assertions.assertEquals(Status.WRONG_ROOM_ID, sendMessageResponse.getStatus(), "Status is wrong");
    }

    @Test
    void shouldGetAllRooms(){
        //given
        registerUser(userA);
        logInUser(userA);
        //when
        final String getAllRoomsRequestAsJson = converter.convertPOJOToJson(GetAllRoomsRequest.builder()
                .nick(userA.getNick())
                .build());
        final GetAllRoomsResponse getAllRoomsResponse = converter.convertJsonToPOJO(roomService.getAllRooms(getAllRoomsRequestAsJson), GetAllRoomsResponse.class);

        //then
        Assertions.assertEquals(Status.OK, getAllRoomsResponse.getStatus(), "Status is wrong");
    }

    @Test
    void shouldNotCanReceiveMessagesByUnknownUser(){
        //given
        final User unknownUser = User.builder()
                .nick("Ala355325")
                .password("123235245")
                .build();
        final Map<UUID, Long> chatTimeMap = new HashMap<>();
        //when
        final String getMessagesRequestASJson = converter.convertPOJOToJson(GetMessagesRequest.builder()
                .nick(userB.getNick())
                .chatTimeMap(chatTimeMap)
                .build());
        final GetMessagesResponse getMessagesResponse = converter.convertJsonToPOJO(
                roomService.getMessages(getMessagesRequestASJson),
                GetMessagesResponse.class);
        //then
        Assertions.assertEquals(Status.USER_ABOUT_NICK_NOT_EXIST, getMessagesResponse.getStatus(), "Status is wrong");
    }

    @Test
    void shouldNotCanCreateChatByUnknownUser(){
        //given
        final User unknownUser = User.builder()
                .nick("Ala355325")
                .password("123235245")
                .build();
        //when
        final String createRoomJsonRequest = converter.convertPOJOToJson(CreateRoomRequest.builder()
                .nameOfRoom("room")
                .nameOfCreator(unknownUser.getNick())
                .build());
        CreateRoomResponse createRoomResponse = converter.convertJsonToPOJO(roomService.createRoom(createRoomJsonRequest),
                CreateRoomResponse.class);
        //then
        Assertions.assertEquals(Status.USER_ABOUT_NICK_NOT_EXIST, createRoomResponse.getStatus(), "Status is wrong");
    }

    private void sendMessageByUserToRoomByUuid(final User user, final String message, final UUID roomUuid) {
        final String sendingMessageRequest = converter.convertPOJOToJson(SendMessageRequest.builder()
                .nick(user.getNick())
                .message(message)
                .roomUuid(roomUuid)
                .build());
        final String sendingMessageResponseAsJson = roomService.sendMessage(sendingMessageRequest);
        final SendMessageResponse sendMessageResponse = converter.convertJsonToPOJO(sendingMessageResponseAsJson,
                SendMessageResponse.class);
    }

    private UUID createRoomByUserAndGetItUuid(User user) {
        final String createRoomJsonRequest = converter.convertPOJOToJson(CreateRoomRequest.builder()
                .nameOfRoom(nameOfCreatedRoom)
                .nameOfCreator(user.getNick())
                .build());
        CreateRoomResponse createRoomResponse = converter.convertJsonToPOJO(roomService.createRoom(createRoomJsonRequest),
                CreateRoomResponse.class);
        return createRoomResponse.getRoomUuid();
    }

    private void registerUser(User user) {
        registerService.register(converter.convertPOJOToJson(
                RegisterRequest.builder()
                        .nick(user.getNick())
                        .password(user.getPassword())
                        .build()));
    }

    private void logInUser(User user) {
        logInService.logIn(converter.convertPOJOToJson(LogInRequest.builder()
                .nick(user.getNick())
                .password(user.getPassword()).build()));
    }

    private UUID getUuidForPublicRoomByUser(User user) {
        final String getUuidForPublicRoomRequest = converter.convertPOJOToJson(GetPublicRoomUuidRequest.builder()
                .nick(user.getNick())
                .build());
        final GetPublicUuidResponse getPublicUuidResponse =
                converter.convertJsonToPOJO(roomService.getPublicRoomUuid(getUuidForPublicRoomRequest),
                        GetPublicUuidResponse.class);
        return getPublicUuidResponse.getPublicRoomUuid();
    }
}
