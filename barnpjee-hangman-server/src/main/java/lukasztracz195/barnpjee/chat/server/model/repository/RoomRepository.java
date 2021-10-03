package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.server.model.entity.Room;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    void save(Room room);

    Optional<Room> get(UUID uuid);

    Collection<Room> getAllRooms();

    UUID getUuidToPublicRoom();
}
