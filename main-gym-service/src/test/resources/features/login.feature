Feature: Login

  Scenario: Successful login
    When the user enters their correct credentials
    When user click login button
    Then the user is successfully authenticated


  Scenario: User login with invalid credentials
    When the user enters their incorrect credentials
    When user click login button with invalid data
    Then the user failed authenticate

