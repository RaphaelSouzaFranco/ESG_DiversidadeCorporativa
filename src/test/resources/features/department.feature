Feature: Department Listing
  As any employee
  I want to be able to view departments
  So that I can know the organizational structure

  Scenario: Successfully list departments
    Given the application is running
    When I request to get all departments
    Then the response status should be 200
    And the response should be a list
