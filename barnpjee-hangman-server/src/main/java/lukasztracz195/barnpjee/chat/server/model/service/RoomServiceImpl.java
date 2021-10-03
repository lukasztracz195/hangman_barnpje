package lukasztracz195.barnpjee.chat.server.model.service;

import lombok.extern.slf4j.Slf4j;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.CreateRoomRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetAllRoomsRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetMessagesRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetPublicRoomUuidRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.SendMessageRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.GetAllRoomsResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetPublicUuidResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.RoomResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.CreateRoomResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetMessagesResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.MessageResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.RoomResponseWithoutMessages;
import lukasztracz195.barnpjee.chat.common.dto.response.SendMessageResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.RoomService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import lukasztracz195.barnpjee.chat.server.model.entity.Room;
import lukasztracz195.barnpjee.chat.server.model.entity.Message;
import lukasztracz195.barnpjee.chat.server.model.entity.User;
import lukasztracz195.barnpjee.chat.server.model.repository.RoomRepository;
import lukasztracz195.barnpjee.chat.server.model.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;

    private final JsonObjectConverter converter;

    public RoomServiceImpl(UserRepository userRepository,
                           RoomRepository roomRepository,
                           JsonObjectConverter converter) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.converter = converter;
    }


    @Override
    public String createRoom(String creatRoomRequestAsJson) {
        final CreateRoomRequest createRoomRequest =
                converter.convertJsonToPOJO(creatRoomRequestAsJson, CreateRoomRequest.class);
        if (canChatBeCreated(createRoomRequest)) {
            final UUID roomUuid = UUID.randomUUID();
            roomRepository.save(Room.builder()
                    .uuid(roomUuid)
                    .name(createRoomRequest.getNameOfRoom())
                    .messages(new ArrayList<>())
                    .build());
            return converter.convertPOJOToJson(
                    CreateRoomResponse.builder()
                            .roomUuid(roomUuid)
                            .name(createRoomRequest.getNameOfRoom())
                            .status(Status.OK)
                            .build());
        } else {
            return converter.convertPOJOToJson(
                    CreateRoomResponse.builder()
                            .status(Status.USER_ABOUT_NICK_NOT_EXIST)
                            .build());
        }
    }

    private boolean canChatBeCreated(CreateRoomRequest createRoomRequest) {
        return userRepository.exists(createRoomRequest.getNameOfCreator());
    }

    @Override
    public String getMessages(String getMessageRequestAsJson) {
        final GetMessagesRequest getMessageRequest =
                converter.convertJsonToPOJO(getMessageRequestAsJson, GetMessagesRequest.class);
        final Optional<User> user = userRepository.get(getMessageRequest.getNick());
        if (!user.isPresent()) {
            return converter.convertPOJOToJson(
                    GetMessagesResponse.builder()
                            .status(Status.USER_ABOUT_NICK_NOT_EXIST)
                            .build());
        }
        final List<Room> rooms = new ArrayList<>();
        if(getMessageRequest.isGetAllMessages()){
            rooms.addAll(roomRepository.getAllRooms());
        }
        if(!getMessageRequest.isGetAllMessages()) {
            getMessageRequest.getChatTimeMap().forEach((roomUuid, time) -> roomRepository.get(roomUuid)
                    .ifPresent(room -> rooms.add(room.getChatAfter(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())))));
            roomRepository.getAllRooms().forEach(room -> {
                if (rooms.stream().unordered().noneMatch(f -> f.getUuid().equals(room.getUuid()))) {
                    rooms.add(room);
                }
            });
        }
        final List<RoomResponse> roomResponses = rooms.stream()
                .map(room -> RoomResponse.builder()
                        .messages(mapMessages(room.getMessages()))
                        .uuid(room.getUuid())
                        .nameOfRoom(room.getName())
                        .status(Status.OK)
                        .build()).collect(Collectors.toList());

        return converter.convertPOJOToJson(
                GetMessagesResponse.builder()
                        .status(Status.OK)
                        .roomsResponses(roomResponses)
                        .build());
    }

    @Override
    public String sendMessage(String sendMessageRequestAsJson) {
        final SendMessageRequest sendMessageRequest =
                converter.convertJsonToPOJO(sendMessageRequestAsJson, SendMessageRequest.class);
        if (canSendMessageWithThisForm(sendMessageRequest)) {
            if (canSendMessageThisUser(sendMessageRequest)) {

                if (canSendMessageForThisRoom(sendMessageRequest)) {
                    return converter.convertPOJOToJson(
                            roomRepository.get(sendMessageRequest.getRoomUuid())
                                    .map(room -> {
                                        room.addMessage(sendMessageRequest.getNick(), sendMessageRequest.getMessage());
                                        roomRepository.save(room);
                                        return SendMessageResponse.builder()
                                                .status(Status.OK)
                                                .build();
                                    }).get());
                } else {
                    return converter.convertPOJOToJson(
                            SendMessageResponse.builder()
                                    .status(Status.WRONG_ROOM_ID)
                                    .build());
                }
            } else {
                return converter.convertPOJOToJson(
                        SendMessageResponse.builder()
                                .status(Status.USER_ABOUT_NICK_NOT_EXIST)
                                .build());
            }
        }
        return converter.convertPOJOToJson(
                SendMessageResponse.builder()
                        .status(sendMessageRequest.validate().getStatus())
                        .build());
    }

    @Override
    public String getPublicRoomUuid(String getPublicUuidRequestAsJson) {
        final GetPublicRoomUuidRequest getPublicUuidRequest =
                converter.convertJsonToPOJO(getPublicUuidRequestAsJson, GetPublicRoomUuidRequest.class);
        if (!canSendUuidToPublicRoom(getPublicUuidRequest)) {
            return converter.convertPOJOToJson(
                    GetPublicUuidResponse.builder()
                            .status(Status.USER_ABOUT_NICK_NOT_EXIST)
                            .build());
        }
        final UUID uuidOfPublicRoom = roomRepository.getUuidToPublicRoom();
        final AtomicReference<GetPublicUuidResponse> atomicReference = new AtomicReference<>();
        roomRepository.get(uuidOfPublicRoom).ifPresent(publicRoom -> atomicReference.set(GetPublicUuidResponse.builder()
                .publicRoomUuid(publicRoom.getUuid())
                .nameOfRoom(publicRoom.getName())
                        .status(Status.OK)
                .build()));
        return converter.convertPOJOToJson(atomicReference.get());
    }

    @Override
    public String getAllRooms(String getAllRoomsRequestAsJson) {
        final GetAllRoomsRequest getAllRoomsRequest =
                converter.convertJsonToPOJO(getAllRoomsRequestAsJson, GetAllRoomsRequest.class);
        if(caneSendInformationAboutAllRooms(getAllRoomsRequest)){
            if(userRepository.exists(getAllRoomsRequest.getNick())) {
                final GetAllRoomsResponse getAllRoomsResponse = GetAllRoomsResponse.builder()
                        .roomsResponses(roomRepository.getAllRooms().stream().map(f -> RoomResponseWithoutMessages.builder()
                                .nameOfRoom(f.getName())
                                .uuid(f.getUuid())
                                .status(Status.OK)
                                .build()).collect(toList()))
                        .status(Status.OK).build();
                return converter.convertPOJOToJson(getAllRoomsResponse);
            }
            return converter.convertPOJOToJson(GetAllRoomsResponse.builder()
                    .roomsResponses(Collections.EMPTY_LIST)
                    .status(Status.USER_ABOUT_NICK_NOT_EXIST));
        }
        return converter.convertPOJOToJson(GetAllRoomsResponse.builder()
                .roomsResponses(Collections.EMPTY_LIST)
                .status(getAllRoomsRequest.validate().getStatus()));
    }

    private boolean caneSendInformationAboutAllRooms(GetAllRoomsRequest getAllRoomsRequest){
        return getAllRoomsRequest.validate().isSuccess();
    }
    private boolean canSendMessageThisUser(SendMessageRequest sendMessageRequest) {
        return userRepository.exists(sendMessageRequest.getNick());
    }

    private boolean canSendMessageForThisRoom(SendMessageRequest sendMessageRequest) {
        return roomRepository.get(sendMessageRequest.getRoomUuid()).isPresent();
    }

    private boolean canSendMessageWithThisForm(SendMessageRequest sendMessageRequest) {
        return sendMessageRequest.validate().isSuccess();
    }

    private Boolean canSendUuidToPublicRoom(GetPublicRoomUuidRequest getPublicUuidRequest) {
        return userRepository.exists(getPublicUuidRequest.getNick());
    }

    private List<MessageResponse> mapMessages(List<Message> messages) {
        return messages.stream()
                .map(this::mapMessage)
                .collect(toList());
    }

    private MessageResponse mapMessage(Message message) {
        return MessageResponse.builder()
                .nick(message.getNick())
                .message(message.getContent())
                .time(message.getTimeOfCreation())
                .build();
    }
}
