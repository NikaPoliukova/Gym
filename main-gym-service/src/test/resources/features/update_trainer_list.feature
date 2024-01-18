Feature: Update Trainee's Trainers API

  Scenario: Update Trainee's Trainers
    Given  the user enter params for update list of trainers
      | username        | trainingDate | trainingName  | trainersList                        |
      | Trainee.Trainee | 2023-12-13   | GOOD TRaining | [{"username": "TrainerG.TrainerG"}] |
    And prepare token for request for update trainers list for trainee
    When the user send a PUT request to update list of trainers for trainee
    Then response should return status code 200

  Scenario: Failed attempt to Update Trainee's Trainers
    Given  the user enter params for update list of trainers
      | username        | trainingDate | trainingName  | trainersList                        |
      | Trainee.Trainee | 2000-12-13   | GOOD TRaining | [{"username": "TrainerG.TrainerG"}] |
    And prepare token for request for update trainers list for trainee
    When the user send a PUT request to update list of trainers for trainee
    Then response should return status code 400


