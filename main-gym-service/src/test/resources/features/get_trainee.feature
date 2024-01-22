Feature: Get trainee API

  Scenario: Successful get Trainee
    Given the user enter username for get trainee
      | username         |
      | Test2.Trainee212 |
    And prepare token for request for trainee
    When The user send a GET request to get information about the trainee
    Then the response contains information about the trainee
    Then response status code should be 200

  Scenario: Try to find Trainee with incorrect username
    Given the user enter username for get trainee
      | username           |
      | 84541451.Trainee29 |
    And prepare token for request for trainee
    When The user send a GET request to get information about the trainee
    Then response status code should be 404