package lukasztracz195.barnpjee.chat.client.model.service.interfaces;

import lukasztracz195.barnpjee.chat.common.dto.Status;
import lukasztracz195.barnpjee.chat.common.dto.request.CreateRoomRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetAllRoomsRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetMessagesRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.GetPublicRoomUuidRequest;
import lukasztracz195.barnpjee.chat.common.dto.request.SendMessageRequest;
import lukasztracz195.barnpjee.chat.common.dto.response.CreateRoomResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetAllRoomsResponse;
import lukasztracz195.barnpjee.chat.common.dto.response.GetMessagesResponse;

import java.util.UUID;

public interface RoomClientService {

    UUID getUuidForPublicRoom(GetPublicRoomUuidRequest getPublicRoomUuidRequest);

    Status sendMessage(SendMessageRequest sendMessageRequest);

    GetMessagesResponse getMessages(GetMessagesRequest getMessagesRequest);

    CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest);

    GetAllRoomsResponse getAllRooms(GetAllRoomsRequest getAllRoomsRequest);
}
