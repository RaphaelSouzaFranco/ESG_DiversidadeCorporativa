Feature: Employee Management
  As an HR Administrator
  I want to be able to manage employees
  So that I can keep track of company staff and their details

  Scenario: Successfully create a new employee
    Given the application is running
    When I request to create a new employee with the following details:
      | employeeId  | EMP001            |
      | name        | John Doe          |
      | email       | john.doe@esg.com  |
      | gender      | M                 |
      | departmentId| DEP001            |
    Then the response status should be 200
    And the response body should contain the created employee details

  Scenario: Fail to get a nonexistent employee
    Given the application is running
    When I request to get the employee with ID "NON_EXISTENT_ID"
    Then the response status should be 404
