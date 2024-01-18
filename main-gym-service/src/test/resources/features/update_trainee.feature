Feature: Trainee Profile Update API

  Scenario:Successful update Trainee Profile
    Given the user enter params for update trainee profile
      | username        | firstName | lastName | dateOfBirth | address         | isActive |
      | Test2.Trainee29 | JON       | SMITH    | 1990-01-01  | 123 Main Street | true     |
    And prepare token for request for update trainee
    When the user send a PUT request to update information about the trainee
    Then the response should return status code 200
    And the response body should contain TraineeUpdateResponse object

  Scenario: Failed attempt to update Trainee Profile
    Given the user enter params for update trainee profile
      | username          | firstName | lastName | dateOfBirth | address         | isActive |
      | Incorrect.Trainee | JON       | SMITH    | 1990-01-01  | 123 Main Street | true     |
    And prepare token for request for update trainee
    When the user send a PUT request to update information about the trainee
    Then the response should return status code 404
