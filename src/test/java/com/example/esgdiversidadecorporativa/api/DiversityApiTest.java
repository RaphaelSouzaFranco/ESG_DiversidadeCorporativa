package com.example.esgdiversidadecorporativa.api;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.isA;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiversityApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testGetDiversityMetrics_ShouldReturn200() {
        given()
            .when()
                .get("/diversity")
            .then()
                .statusCode(200)
                .body("$", isA(java.util.List.class))
                .body("size()", greaterThanOrEqualTo(0));
    }
}
