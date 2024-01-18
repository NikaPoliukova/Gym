Feature: Trainer Profile Update API

  Scenario:Successful update Trainer Profile
    Given the user enter params for update trainer profile
      | username        | firstName | lastName | specialization | isActive |
      | Trainer.Trainer | JON       | SMITH    | YOGA           | false    |
    And prepare token for request for update trainer
    When the user send a PUT request to update information about the trainer
    Then we get response which should return status code 200
    And the response body should contain TrainerUpdateResponse object

  Scenario: Failed attempt to update Trainer Profile
    Given the user enter params for update trainer profile
      | username      | firstName | lastName | specialization | isActive |
      | Test2.Trainer | JON       | SMITH    | PILATES        | false     |
    And prepare token for request for update trainer
    When the user send a PUT request to update information about the trainer
    Then we get response which should return status code 404
