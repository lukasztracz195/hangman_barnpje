package lukasztracz195.barnpjee.chat.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
public class Room {
    private String name;
    private UUID uuid;

    @Override
    public String toString() {
        return name;
    }
}
