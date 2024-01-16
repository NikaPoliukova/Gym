Feature: Update Trainee

  Scenario: Successful update trainee
    Given the valid trainee credentials for update
    When user submits a request to update the trainee's profile
    Then the response return update information about the trainee
    Then we get status response code 200

  Scenario: Failed attempt to update trainee
    Given the invalid trainee credentials for update
    When user submits a request to update the trainee's profile
    Then we get status response code 400
