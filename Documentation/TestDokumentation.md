# Test Documentation
The two most important parts of the Application have been thoroughly tested. Those two parts being the Log in system and the serious game mechanic i.e. the quiz. Those have been chosen since they are explicitely specified in the mandatory requirements.

## Unit Tests
For the Unit tests we chose to add tests for the UserFactory as well as the Quiz Factory since all factories are pretty similar, with the UserFactory being the most complicated one. The tests use an inMemory database and therefore require no connection to the university network.
- The tests are located at tests/models/

We also added Unit tests for the LoginController and the Quiz Controller since those accompany the before mentioned factories to form the central game mechanic and the log in system. 
- The tests are located at tests/controllers/

## Component Tests
Our component Tests aim to test all possible requests directed at the ProfileController and the QuizController. 

The key difference between those thests and the Unit tests is, that they use a real Application environment, therefore those tests cannot be run without a connection to the university network.

- The log in tests are located at tests/integrationTests/LogInTest.
- The quiz tests are located at tests/integrationTests/QuizTest.

## Integration Tests
Due to a bug in Intellij we were not able to test the Front-End of the Application


