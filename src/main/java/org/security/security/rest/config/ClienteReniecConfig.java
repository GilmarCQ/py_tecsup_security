package org.security.security.rest.config;

import lombok.RequiredArgsConstructor;
import org.security.security.aggregates.response.ReniecResponse;
import org.security.security.rest.ClienteReniec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClienteReniecConfig {

    @Autowired
    private ClienteReniec reniec;

    @Value("${token.api}")
    private String tokenApi;
    public ReniecResponse getInfoReniec(String numDoc) {
        String authorization = "Bearer " + tokenApi;
        ReniecResponse reniecResponse = reniec.getInfo(numDoc, authorization);
        return reniecResponse;
    }

}
