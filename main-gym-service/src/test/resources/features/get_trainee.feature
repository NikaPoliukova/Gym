Feature: get trainee

  Scenario: Successful get Trainee
    Given user with username
    When the user sends a GET request to obtain information about the trainee
    Then the response contains information about the trainee
    And we get the status response code 200

  Scenario: Try to find Trainee, but he not found
    Given user with incorrect username
    When the user sends a GET request to obtain information about the trainee
    And we get the status response code 400