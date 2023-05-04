package com.vault.hometask.integration;

import com.vault.hometask._config.VaultHomeprojectApplication;
import com.vault.hometask.controller.models.FundsLoadPayload;
import com.vault.hometask.controller.models.FundsLoadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.vault.hometask.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = VaultHomeprojectApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VaultHomeprojectApplicationTests {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void loadFundRequest_acceptedScenario() throws ParseException {
        long userId = 1; Date loadDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-01T00:00:00Z");
        FundsLoadPayload payload = new FundsLoadPayload(1, userId, "200", loadDate);

        ResponseEntity<FundsLoadResponse> response = restTemplate.postForEntity(getLoadFundsUrl(), payload, FundsLoadResponse.class);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isAccepted());
        assertEquals(1, response.getBody().getId());
        assertEquals(userId, response.getBody().getCustomerId());
    }

    @Test
    void loadFundRequest_multipleAcceptedScenario() throws ParseException {
        long userId = 2; Date loadDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-01T00:00:05Z");

        for(int i=0; i < DAILY_COUNT_LIMIT; i++){
            FundsLoadPayload payload = new FundsLoadPayload(i, userId, "200", loadDate);
            var response = restTemplate.postForEntity(getLoadFundsUrl(), payload, FundsLoadResponse.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isAccepted());
            assertEquals(userId, response.getBody().getCustomerId());
        }
    }

    @Test
    void loadFundRequest_failedCountScenario() throws ParseException {
        long userId = 3; Date loadDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-01T00:00:05Z");

        for(int i=0; i <= DAILY_COUNT_LIMIT; i++){
            FundsLoadPayload payload = new FundsLoadPayload(i, userId, "200", loadDate);
            var response = restTemplate.postForEntity(getLoadFundsUrl(), payload, FundsLoadResponse.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(i<DAILY_COUNT_LIMIT, response.getBody().isAccepted());
            assertEquals(userId, response.getBody().getCustomerId());
        }
    }

    @Test
    void loadFundRequest_failedAmountScenario() throws ParseException {
        long userId = 4; Date loadDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2018-01-01T00:00:05Z");

        FundsLoadPayload payload = new FundsLoadPayload(0, userId, "5001", loadDate);
        var response = restTemplate.postForEntity(getLoadFundsUrl(), payload, FundsLoadResponse.class);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAccepted());
        assertEquals(userId, response.getBody().getCustomerId());
    }

    @Test
    void loadFundRequest_failedWeeklyAmountScenario() throws ParseException {
        long userId = 5;
        Calendar loadDate = Calendar.getInstance();
        loadDate.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2023-03-01T00:00:00Z"));

        for (int i=0; i <= WEEKLY_AMOUNT_LIMIT/DAILY_AMOUNT_LIMIT; i++) {
            FundsLoadPayload payload = new FundsLoadPayload(i, userId, String.valueOf(DAILY_AMOUNT_LIMIT), loadDate.getTime());
            var response = restTemplate.postForEntity(getLoadFundsUrl(), payload, FundsLoadResponse.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(userId, response.getBody().getCustomerId());
            assertEquals(i < WEEKLY_AMOUNT_LIMIT/DAILY_AMOUNT_LIMIT, response.getBody().isAccepted());

            loadDate.add(Calendar.DATE, 1);
        }
    }

    @Test
    void loadFundRequest_successfulWeeklyOverflowAmountScenario() throws ParseException {
        long userId = 5;
        Calendar loadDate = Calendar.getInstance();
        loadDate.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse("2023-03-02T00:00:00Z"));

        for (int i=0; i <= WEEKLY_AMOUNT_LIMIT/DAILY_AMOUNT_LIMIT; i++) {
            FundsLoadPayload payload = new FundsLoadPayload(i, userId, String.valueOf(DAILY_AMOUNT_LIMIT), loadDate.getTime());
            var response = restTemplate.postForEntity(getLoadFundsUrl(), payload, FundsLoadResponse.class);
            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(userId, response.getBody().getCustomerId());
            assertTrue(response.getBody().isAccepted());

            loadDate.add(Calendar.DATE, 1);
        }
    }


    private String getLoadFundsUrl(){
        return "http://localhost:%d/funds/load".formatted(port);
    }
}
