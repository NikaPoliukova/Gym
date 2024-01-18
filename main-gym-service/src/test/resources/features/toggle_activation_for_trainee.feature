Feature: Toggle Trainee's Profile Activation

  Scenario: Toggle Trainee's profile activation
    Given the user provides the following parameters for toggling Trainee's profile activation
      | username        | isActive |
      | Trainee.Trainee | false    |
    And prepare token for request for toggling Trainee's profile activation
    When the user send a PATCH request to toggle activation
    Then response status should be return code 200

