Feature: Change password API

  Scenario: Successful change password
    Given the user enter params for update password
      | username        | oldPassword | newPassword |
      | Test2.Trainee29 | LJ4yoa5qRn  | 1234567890  |
    And prepare token for request
    When The user send a PUT request to update
    Then The response status code should be 200

  Scenario: Failed change password
    Given the user enter params for update password
      | username            | oldPassword | newPassword |
      | Incorrect.Trainee29 | LJ4yoa5qRn  | 1234567890  |
    And prepare token for request
    When The user send a PUT request to update
    Then The response status code should be 404
