Feature: Registration API

  Scenario: Successfully register a new trainee
    Given the user provides the following trainee registration data
      | firstName | lastName | address    | dateOfBirth |
      | My        | Trainee  | Street 125 | 2000-01-01  |
      | Dima      | Trainee  | Street 126 | 1998-05-15  |
    When the user makes a POST request for save trainee
    Then the response status code should be 201
    And the response should contain Principal object

  Scenario: Successfully register a new trainer
    Given the user provides the following trainer registration data
      | firstName | lastName | specialization |
      | Test      | Trainer  | YOGA           |
      | Test2     | Trainer  | SWIMMING       |
    When the user makes a POST request for save trainer
    Then the response status code should be 201
    And the response should contain Principal object

  Scenario: Registration trainee with invalid data
    Given the user provides the following trainee registration data
      | firstName | lastName | address    | dateOfBirth |
      |           | Trainee1 | Street 125 | 2000-01-01  |
    When the user makes a POST request for save trainee
    Then the response status code should be 400

  Scenario: Registration trainer with invalid data
    Given the user provides the following trainer registration data
      | firstName | lastName | specialization |
      |           | Trainer  | GYM            |
    When the user makes a POST request for save trainer
    Then the response status code should be 400


