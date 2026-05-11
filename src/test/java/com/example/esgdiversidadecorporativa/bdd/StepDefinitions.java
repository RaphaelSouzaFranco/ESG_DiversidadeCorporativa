package com.example.esgdiversidadecorporativa.bdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Integração fundamental do Cucumber com o Spring Boot Context
// (A configuração do contexto está centralizada no CucumberSpringConfiguration.java)
public class StepDefinitions {

    @LocalServerPort
    private int port;

    private Response response;

    @Given("the application is running")
    public void the_application_is_running() {
        assertTrue(port > 0);

        Response res = RestAssured.given()
                .port(port) // Usando a porta explicitamente
                .get("/departments/DEP001");

        if (res.getStatusCode() == 404) {
            RestAssured.given()
                    .port(port)
                    .contentType("application/json")
                    .body("{\"departmentId\": \"DEP001\", \"name\": \"Human Resources\"}")
                    .post("/departments");
        }
    }

    @When("I request to create a new employee with the following details:")
    public void i_request_to_create_a_new_employee_with_the_following_details(Map<String, String> employeeDetails) {
        response = RestAssured.given()
                .port(port) // Usando a porta explicitamente
                .contentType("application/json")
                .body(employeeDetails)
                .when()
                .post("/employees");
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        // LOG SALVA-VIDAS: Imprime o corpo do erro caso o status não seja o esperado
        if (response.getStatusCode() != expectedStatus) {
            System.err.println("============== ERRO NA API ================");
            System.err.println("Status Esperado: " + expectedStatus);
            System.err.println("Status Retornado: " + response.getStatusCode());
            System.err.println("Corpo da Resposta: " + response.getBody().asPrettyString());
            System.err.println("Teste");
            System.err.println("=========================================");
        }

        assertEquals(expectedStatus, response.getStatusCode());
    }

    @And("the response body should contain the created employee details")
    public void the_response_body_should_contain_the_created_employee_details() {
        assertNotNull(response.jsonPath().getString("employeeId"));
        assertNotNull(response.jsonPath().getString("name"));
    }

    @When("I request to get the employee with ID {string}")
    public void i_request_to_get_the_employee_with_id(String id) {
        response = RestAssured.given()
                .port(port)
                .when()
                .get("/employees/" + id);
    }

    @When("I request to get all departments")
    public void i_request_to_get_all_departments() {
        response = RestAssured.given()
                .port(port)
                .when()
                .get("/departments");
    }

    @And("the response should be a list")
    public void the_response_should_be_a_list() {
        assertTrue(response.jsonPath().getList("").size() >= 0);
    }
}