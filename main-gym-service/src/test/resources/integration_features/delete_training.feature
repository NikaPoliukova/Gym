Feature: Delete Training API

  Scenario: Successfully delete training with valid data
    Given the user enter params for delete training
      | trainerUsername | trainingName | trainingDate | trainingType | duration |
      | Trainer.Trainer | Yoga YES     | 2023-02-16   | PILATES      | 60       |
    And prepare token for request for delete training
    When the user send a DELETE request for delete training and send request to Secondary microservice
    Then the response code should be return 204


  Scenario: Failed attempt to delete training with invalid data
    Given the user enter params for delete training
      | trainerUsername | trainingName | trainingDate | trainingType | duration |
      | Incorrect.Trainer | Yoga YES     | 2023-02-16   | PILATES      | 60       |
    And prepare token for request for delete training
    When the user send a DELETE request for delete training and send request to Secondary microservice
    Then the response code should be return 404