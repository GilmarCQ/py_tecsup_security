package org.security.security.aggregates.response;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class HttpResBody {
    private String message;
    private Optional data;
}
