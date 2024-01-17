Feature: Update Trainer list

  Scenario: Successfully update Trainee's trainers list
    Given the params for update trainer list for trainee
    When the user makes a PUT request for update list
    Then the response should contain a list of TrainerDtoForTrainee
    And  the response status code should be 200

