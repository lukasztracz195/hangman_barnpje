package lukasztracz195.barnpjee.chat.server.model.repository;


import lukasztracz195.barnpjee.chat.server.model.entity.User;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    boolean exists(String nick);

    Optional<User> get(String nick);
}
