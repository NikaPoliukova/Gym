Feature: Save Training API

  Scenario: Successfully save a new training
    Given the user provides the following training details:
      | traineeUsername | trainerUsername | trainingName | trainingDate | trainingType | trainingDuration |
      | Trainee.Trainee | Trainer.Trainer | Yoga YES     | 2023-02-16   | PILATES      | 60               |
    And prepare token for request for save training
    When the user send a POST request for save training and send request to Secondary microservice
    Then the response status code should be return 201
    And the response body should contain the saved Training object

  Scenario: Failed attempt to save a new training
    Given the user provides the following training details:
      | traineeUsername   | trainerUsername | trainingName  | trainingDate | trainingType | trainingDuration |
      | Trainee.Incorrect | Trainer.Trainer | Yoga Workshop | 2023-02-15   | PILATES      | 60               |
    And prepare token for request for save training
    When the user send a POST request for save training and send request to Secondary microservice
    Then the response status code should be return 404
