Feature: Finding inactive trainers for trainee

  Scenario: Finding inactive trainers for trainee
    Given the trainee's username for finding trainers
    When sends a GET request to find inactive trainers for the trainee
    Then the response contains list inactive trainers
    Then the expected status response code 200
