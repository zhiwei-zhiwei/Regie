The code is Java based. I imported the two different kind of mysql-connector-j library in roder to connect the mysql. 
Please up-zip them or download the version fits on your computer from the website https://dev.mysql.com/downloads/connector/j/
Remember to import the libary to the project path.

RECOMMENDATION: Use IntelliJ IDEA to access the project.

In order to run the main, run the db/database-init.sql first, I provided some sample data set. Check the user table 
in order to log in with the role of STUDENT or ADMIN, they have different responsibilities and functions. Please 
follow the console's text instruction to run the program.

For the Practicum 3, with the idea of S.O.L.I.D. I designed my project with multiple interface and apply the idea of 
Strategy Pattern. 

First of all, For the relationship between each class make sure they are following the Inheritance of the oop, and 
ensure the aggregation and composition relationship between two class, for example, a course "has-a" lab with a 
aggregation relationship, since not all course has a lab. 

Meanwhile, I separate each class and let they only take one responsibility. For example, DAO only take responsibility 
to fulfill the method of the database query and the manager class will implement the methods of DAOs. At this point, 
It could ensure the dynamic modeling, since it doesn't need to modify to much code and just can add or mortify a method
in DAO without editing the manager class. It ensure the dynamic modeling and followed the SPD of SOLID.
Reference: "A class should have one and only one reason to change, meaning that a class should have only one job."

Besides, interfaces in the practicum, the high level models like LabManager and CourseManager depend on the interfaces
It also ensures the idea of DIP, which "Entities must depend on abstractions, not on concretions. It states that the 
high-level module must not depend on the low-level module, but they should depend on abstractions.".

More implementations that follows the idea of "Object-Oriented Programming" in the code.