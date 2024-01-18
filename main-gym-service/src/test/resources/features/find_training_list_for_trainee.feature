Feature: Finding Trainee's Trainings API

  Scenario: Find trainings list with specified criteria
    Given the user provides the following parameters for finding trainings
      | username        | periodFrom | periodTo   | trainerName       | trainingType |
      | Trainee.Trainee | 2000-07-01 | 2022-07-01 | TrainerG.TrainerG | YOGA         |
    And prepare token for request for get trainers list for trainee
    When send a GET request to find trainers list for trainee
    Then the response contains list of training for trainee
    Then after should be response status code 200

  Scenario: Failed to find trainings list for trainee,trainee not found
    Given the user provides the following parameters for finding trainings
      | username          | periodFrom | periodTo   | trainerName       | trainingType |
      | Incorrect.Trainee | 2000-07-01 | 2022-07-01 | TrainerG.TrainerG | YOGA         |
    And prepare token for request for get trainers list for trainee
    When send a GET request to find trainers list for trainee
    Then after should be response status code 400
