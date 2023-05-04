package com.vault.hometask.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vault.hometask._config.VaultHomeprojectApplication;
import com.vault.hometask.controller.models.FundsLoadPayload;
import com.vault.hometask.controller.models.FundsLoadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = VaultHomeprojectApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VaultHomeprojectProvidedInputTest {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void loadFundRequest_acceptedScenario() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<String> inputLines = Files.readAllLines(new ClassPathResource("input.txt").getFile().toPath());

        for(String line: inputLines){
            var response = restTemplate.postForEntity(getLoadFundsUrl(), mapper.readValue(line, FundsLoadPayload.class), FundsLoadResponse.class);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            if (response.getBody() == null)
                System.err.printf("Body was null for input string %s%n", line);

//            sendOutputToLog(mapper.writeValueAsString(response.getBody()));
            sendOutputToFile(new ClassPathResource("output.txt").getPath(), mapper.writeValueAsString(response.getBody())+"\n");
        }
    }

    private void sendOutputToLog(String output){
        System.out.println(output);
    }

    private void sendOutputToFile(String filepath, String output) throws IOException {
        Path path = Path.of(filepath);
        if(!Files.exists(path)) Files.createFile(path);
        Files.write(path, output.getBytes(), StandardOpenOption.APPEND);
    }

    private String getLoadFundsUrl(){
        return "http://localhost:%d/funds/load".formatted(port);
    }
}
