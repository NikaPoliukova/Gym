Feature: Delete Trainee API


  Scenario: Delete Trainee by username
    Given the user enter username for delete trainee
      | username      |
      | OLA.TRAINEE16 |
    And prepare token for request for delete trainee
    When the user send a DELETE request to delete the trainee
    Then we should to get response with status code 204

  Scenario: Failed attempt to delete trainee by username
    Given the user enter username for delete trainee
      | username          |
      | Incorrect.TRAINEE |
    And prepare token for request for delete trainee
    When the user send a DELETE request to delete the trainee
    Then we should to get response with status code 404
