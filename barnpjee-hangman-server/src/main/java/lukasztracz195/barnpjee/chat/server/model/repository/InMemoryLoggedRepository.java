package lukasztracz195.barnpjee.chat.server.model.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryLoggedRepository implements LoggedRepository {

    private final List<String> loggedInUsersNicks = new CopyOnWriteArrayList<>();

    @Override
    public void add(String nick) {
        loggedInUsersNicks.add(nick);
    }

    @Override
    public void remove(String nick) {
        loggedInUsersNicks.remove(nick);
    }

    @Override
    public List<String> getAll() {
        return new ArrayList<>(loggedInUsersNicks);
    }
}
