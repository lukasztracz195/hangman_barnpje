package lukasztracz195.barnpjee.chat.server.model.repository;

import java.util.List;

public interface LoggedRepository {

    void add(String nick);

    void remove(String nick);

    List<String> getAll();
}
