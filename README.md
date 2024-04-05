# How to start the Application

**Prerequisites**
- IntelliJ IDEA (recommended)
- SDK - Amazon Coretto version 11.0.21 (recommended)

**How to start**

1. clone the repository
2. open the Project using IntelliJ IDEA
3. select "Import project from external model" then select "sbt"
4. select an SDK (Amazon Coretto version 11.0.21 recommended) and create the project
5. open the SBT shell and type "run"
6. the application is started at [http://localhost:9000/](http://localhost:9000/)
7. You may now use the credentials "MaxMaxMustermann", "RescueReady" to log in or create a new Account, as you prefer.
8. please note, that the project uses a volatile in Memory Database for Demo purposes, therefore all changes made are not retained upon restarting the application.

Note: Play may ask you to apply Database evolutions. If so click the "Apply Evolutions" button. This is used to ensure the correct state of the database.
Note: For futrther information please see the Project Wiki
