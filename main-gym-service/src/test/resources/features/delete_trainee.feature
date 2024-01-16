Feature: Delete Trainee

  Scenario: Successful delete trainee
    Given the valid trainee credentials for delete trainee
    When the user send a DELETE request to delete the trainee
    Then we get status response code after delete 204

  Scenario: Failed attempt to delete trainee
    Given the invalid trainee credentials for delete trainee
    When the user send a DELETE request to delete the trainee
    Then we get status response code after delete 400
