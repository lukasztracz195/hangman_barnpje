package lukasztracz195.barnpjee.chat.server.model.repository;

import lukasztracz195.barnpjee.chat.server.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public boolean exists(String nick) {
        return users.containsKey(nick);
    }

    @Override
    public void save(User user) {
        users.put(user.getNick(), user.toBuilder().build());
    }

    @Override
    public Optional<User> get(String nick) {
        return Optional.ofNullable(users.get(nick))
                .map(user -> user.toBuilder().build());
    }

}
