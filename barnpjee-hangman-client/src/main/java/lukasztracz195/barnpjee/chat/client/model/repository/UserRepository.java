package lukasztracz195.barnpjee.chat.client.model.repository;

import lukasztracz195.barnpjee.chat.client.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class UserRepository {

    private Set<User> users;
    private Set<String> nicks;
    private Map<String, User> mapUsersByUuid;
    private static UserRepository instance;

    private UserRepository() {
        users = new HashSet<>();
        mapUsersByUuid = new HashMap<>();
        nicks = new HashSet<>();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean userExists(User user){
        return users.contains(user);
    }

    public boolean nickIsFree(String nick){
        return nicks.contains(nick);
    }

    public void addUser(User user){
        users.add(user);
        mapUsersByUuid.put(user.getUuid(), user);
    }

    public Optional<User> getUserByUuid(String uuid){
        return Optional.ofNullable(mapUsersByUuid.get(uuid));
    }

    public List<User> getAllLoggedUsers(){
        return new ArrayList<>(users);
    }

    public boolean removeUserByUuid(String uuid){
        try{
            User user = mapUsersByUuid.get(uuid);
            nicks.remove(user.getNick());
            users.remove(user);
            mapUsersByUuid.remove(uuid);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
