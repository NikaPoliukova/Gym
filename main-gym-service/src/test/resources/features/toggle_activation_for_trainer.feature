Feature: Toggle Trainer's Profile Activation

  Scenario: Toggle Trainer's profile activation
    Given the user enter params for toggling Trainer's profile activation
      | username        | isActive |
      | Trainer.Trainer | false    |
    And prepare token for request for toggling Trainer's profile activation
    When the user send a PATCH request to toggle activation trainer
    Then we get the response status code 200