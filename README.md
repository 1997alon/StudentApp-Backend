# welcome to Student Backend
## go to the Front: [StudentApp Front](https://github.com/1997alon/StudentApp-Front)
Backend made in Java spring with Maven
## maven dependecies of Spring:
  1. spring-boot-starter-actuator
  2. spring-boot-starter-data-jpa
  3. spring-boot-starter-validation
  4. spring-boot-starter-web
  5. spring-boot-devtools
  6. mysql-connector-j
  7. lombok
  8. spring-boot-starter-test
  9. spring-restdocs-mockmvc
  10. spring-security-test


# how to start?
1. I used Inteliji for the backend with Java 17
2. make sure all the maven deps are ok
3. spring is working on your computer
4. in terminal run: mvn clean install
5. then in terminal run: mvn spring-boot:run
6. the server is running!

## End Points API:
### /student:
  1. /add : for adding student
  2. /getDepartments : get all the departments
  3. /getStudents : get all students
  4. /getExcellent : get all excellent students
  5. /update : update student
  6. /sort : sort by: sortBy(Column) and direction("asc" or "desc")
  7. /sortExcellent : sort excellent students by: sortBy(Column) and direction("asc" or "desc")
  8. /filter : filter all student with filterField(Column) and filterValue(input)
  9. /filterExcellent : filter excellent students with filterField(Column) and filterValue(input)
### /honorStudent: with column: Email, Department and GPA:
  1. /getHonorStudents : get all the honor students
  2. /getHighestGpaPerDepartment : get the best student from each department
  3. /sortHonorStudents : sort honor student by: sortBy(Column) and direction("asc" or "desc")
## connect to MySQL:
### built Schema named studentapp and inside one table:
the table is: student with (id, firstName, lastName, email, department and gpa)

# how the code built:
  1. with controller-service-repository interface
  2. Controllers with honor-student and student
  3. service for each controller
  4. one repository for one table
  5. model- student and department (ENUM)
  6. exception - student exception
  7. dto- for honor student with 3 columns only
  8. sql - there you can see the creation of the table
  9. config - for making the server async with threadpool for efficient
  10. util - message response and etc..




