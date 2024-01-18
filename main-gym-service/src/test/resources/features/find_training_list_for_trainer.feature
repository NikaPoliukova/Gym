Feature: Finding Trainer's Trainings API

  Scenario: Find trainings list with params for trainer
    Given the user enter params for finding trainings for trainer
      | username        | periodFrom | periodTo   | traineeName     |
      | Trainer.Trainer | 2000-07-01 | 2022-07-01 | Trainee.Trainee |
    And prepare token for request for get trainers list for trainer
    When send a GET request to find training list for trainer
    Then the response contains list of training for trainer
    Then after we should be response status code 200

  Scenario: Failed to find trainings list with params for trainer,trainer not found
    Given the user enter params for finding trainings for trainer
      | username          | periodFrom | periodTo   | traineeName      |
      | Incorrect.Trainee | 2000-07-01 | 2022-07-01 | Trainee.TraineeG |
    And prepare token for request for get trainers list for trainer
    When send a GET request to find training list for trainer
    Then after we should be response status code 404