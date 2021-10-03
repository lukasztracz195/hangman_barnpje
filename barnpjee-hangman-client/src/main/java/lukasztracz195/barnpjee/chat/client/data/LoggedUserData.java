package lukasztracz195.barnpjee.chat.client.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class LoggedUserData {
    private String nickOfLoggedUser;
    private boolean userIsLogged;
    private static LoggedUserData instance;
    private Room currentRoom;

    public static LoggedUserData getInstance(){
        if(instance == null){
            instance = new LoggedUserData();
        }
        return instance;
    }
}
