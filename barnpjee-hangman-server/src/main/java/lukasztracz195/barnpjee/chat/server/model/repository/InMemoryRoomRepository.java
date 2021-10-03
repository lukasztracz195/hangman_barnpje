package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.server.model.entity.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRoomRepository implements RoomRepository {

    private final UUID uuidForPublicRoom;
    private final Map<UUID, Room> rooms;

    public InMemoryRoomRepository(){
        uuidForPublicRoom = UUID.randomUUID();
        rooms = new ConcurrentHashMap<UUID, Room>() {{
            put(uuidForPublicRoom, Room.builder()
                    .uuid(uuidForPublicRoom)
                    .name("Public")
                    .messages(new ArrayList<>())
                    .build());
        }};
    }


    @Override
    public void save(Room room) {
        rooms.put(room.getUuid(), room.toBuilder().build());
    }

    @Override
    public Optional<Room> get(UUID uuid) {
        return Optional.ofNullable(rooms.get(uuid))
                .map(room -> room.toBuilder().build());
    }

    @Override
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    @Override
    public UUID getUuidToPublicRoom() {
        return uuidForPublicRoom;
    }
}
