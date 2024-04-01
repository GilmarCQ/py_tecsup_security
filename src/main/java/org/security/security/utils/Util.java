package org.security.security.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@Getter
@Setter
public class Util {
    public static Timestamp getTimestamp() {
        long currenTime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(currenTime);
        return timestamp;
    }
}
