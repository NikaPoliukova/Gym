Feature: Change login

  Scenario: Successful change password
    When the user enter correct credentials for update password
    When user click update password button
    Then the user is successfully change password

  Scenario: Failed change password
    When the user enter incorrect credentials for update password
    When user click update password button
    Then the user failed updating password