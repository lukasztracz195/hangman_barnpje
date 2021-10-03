package lukasztracz195.barnpjee.chat.common.interfaces;


public interface RoomService {

    String createRoom(String createChatRequestAsJson);

    String getMessages(String messagesAsJson);

    String sendMessage(String sendMessageRequestAsJson);

    String getPublicRoomUuid(String getPublicUuidRequestAsJson);

    String getAllRooms(String getAllRoomsRequestAsJson);
}
