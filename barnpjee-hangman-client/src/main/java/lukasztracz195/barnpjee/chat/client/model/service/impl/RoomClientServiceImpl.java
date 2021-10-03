package lukasztracz195.barnpjee.chat.client.model.service.impl;

import lukasztracz195.barnpjee.chat.client.model.service.interfaces.RoomClientService;
import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.CreateRoomRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetAllRoomsRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetMessagesRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetPublicRoomUuidRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.SendMessageRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.CreateRoomResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetAllRoomsResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetMessagesResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetPublicUuidResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.SendMessageResponse;
import lukasztracz195.barnpjee.chat.common.interfaces.RoomService;
import lukasztracz195.barnpjee.chat.common.json.JsonObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomClientServiceImpl implements RoomClientService {
    @Autowired
    private RoomService roomService;

    @Autowired
    private JsonObjectConverter converter;

    @Override
    public UUID getUuidForPublicRoom(GetPublicRoomUuidRequest getPublicRoomUuidRequest) {
        final String jsonOutput = roomService.getPublicRoomUuid(converter.convertPOJOToJson(getPublicRoomUuidRequest));
        final GetPublicUuidResponse getPublicUuidResponse = converter.convertJsonToPOJO(jsonOutput, GetPublicUuidResponse.class);
        return getPublicUuidResponse.getPublicRoomUuid();
    }

    @Override
    public Status sendMessage(SendMessageRequest sendMessageRequest) {
        final String jsonOutput = roomService.sendMessage(converter.convertPOJOToJson(sendMessageRequest));
        final SendMessageResponse sendMessageResponse = converter.convertJsonToPOJO(jsonOutput, SendMessageResponse.class);
        return sendMessageResponse.getStatus();
    }

    @Override
    public GetMessagesResponse getMessages(GetMessagesRequest getMessagesRequest) {
        final String jsonOutput = roomService.getMessages(converter.convertPOJOToJson(getMessagesRequest));
        return converter.convertJsonToPOJO(jsonOutput, GetMessagesResponse.class);
    }

    @Override
    public CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest) {
        final String jsonOutput = roomService.createRoom(converter.convertPOJOToJson(createRoomRequest));
        return converter.convertJsonToPOJO(jsonOutput, CreateRoomResponse.class);
    }

    @Override
    public GetAllRoomsResponse getAllRooms(GetAllRoomsRequest getAllRoomsRequest) {
        final String jsonOutput = roomService.getAllRooms(converter.convertPOJOToJson(getAllRoomsRequest));
        return converter.convertJsonToPOJO(jsonOutput, GetAllRoomsResponse.class);
    }
}
