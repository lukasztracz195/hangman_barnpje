package lukasztracz195.barnpjee.chat.common.json;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public final class LocalDataTimeConverter {

    public static LocalDateTime convertEpochMilliToLocalDateTime(final Long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    public static Long convertLocalDateTimeToEpochMilliAsLong(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
