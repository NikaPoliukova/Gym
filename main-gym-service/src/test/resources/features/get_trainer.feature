Feature: Get trainer API

  Scenario: Successful get Trainer
    Given the user enter username for get trainer
      | username        |
      | Trainer.Trainer |
    And prepare token for request for trainer
    When The user send a GET request to get information about the trainer
    Then the response contains information about the trainer
    Then we get response status code 200

  Scenario: Try to find Trainer with incorrect username
    Given the user enter username for get trainer
      | username   |
      | No.Trainer |
    And prepare token for request for trainer
    When The user send a GET request to get information about the trainer
    Then we get response status code 404