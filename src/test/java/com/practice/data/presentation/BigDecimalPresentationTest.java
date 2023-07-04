package com.practice.data.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BigDecimalPresentationTest {
    @Test
    void serializeManually() throws JsonProcessingException {
        BigDecimal a = new BigDecimal("1.456");
        ObjectMapper mapper = new ObjectMapper();

        var result = mapper.writeValueAsString(a.toString());
        assertEquals("\"1.456\"", result);
    }

    @Test
    void serializeByWeb() {
        config = config().jsonConfig(jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE));
        get("/big-decimal/1.456")
                .then().log().all()
                .statusCode(200)
                .body("num", is(closeTo(1.456, 0.001)));
    }
}
