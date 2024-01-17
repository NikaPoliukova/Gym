Feature: Login API

  Scenario: Successful login
    Given The user enters login information
      | username      | password   |
      | DIMA.TRAINEE2 | NMo7a0j6XL |
    When the user makes a POST request for login
    Then the user get status 200
    And generated access token and set to header


  Scenario: User login with incorrect credentials
    Given The user enters login information
      | username       | password   |
      | PPPPP.TRAINEE2 | NMo7a0j6XL |
    When the user makes a POST request for login
    Then the user get status 302


