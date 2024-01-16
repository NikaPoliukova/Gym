Feature: Registration API

  Scenario: Successful trainee registration
    Given the trainee registration request with valid data
    When the trainee registration API saved with valid data
    Then the response status code should be 201
    And the response should contain the trainee's details

  Scenario: Trainee registration with invalid data
    Given the trainee registration request with invalid data
    When I submit the trainee registration request
    Then the response should have status code 403

  Scenario: Successful trainer registration
    Given the trainer registration request with valid data
    When the trainer registration API saved with valid data
    Then the response status code should be 201
    And the response should contain the trainer's details

  Scenario: Trainer registration with invalid data
    Given the trainer registration request with invalid data
    When I submit the trainer registration request
    Then the response should have status code 403


