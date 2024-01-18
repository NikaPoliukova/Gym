Feature: Find Not Assigned Active Trainers for a Trainee API

  Scenario: Find Not Assigned Active Trainers
    Given the user enter username for get not active trainers for trainee
      | username        |
      | Trainee.Trainee |
    And prepare token for request for get not active trainers for trainee
    When send a GET request to find not active trainers for trainee
    Then the response contains list of trainers
    And should be response status code 200
