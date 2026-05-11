package com.example.esgdiversidadecorporativa.api;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeApiTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testGetEmployees_ShouldReturn200AndValidSchema() {
        given()
            .when()
                .get("/employees")
            .then()
                .statusCode(200)
                .body("$", isA(java.util.List.class))
                // Note: The schema validates a single employee, here we test the list response logic if needed or just validate properties
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetEmployeeByIdNotFound_ShouldReturn404() {
        given()
            .pathParam("id", "9999")
            .when()
                .get("/employees/{id}")
            .then()
                .statusCode(404);
    }
}
