# go to the Front: [StudentApp Backend](https://github.com/1997alon/StudentApp-Front)
Backend made in Java spring with Maven
## maven dependecies:
spring-boot-starter-actuator
spring-boot-starter-data-jpa:
spring-boot-starter-validation
spring-boot-starter-web
spring-boot-devtools
mysql-connector-j
lombok
spring-boot-starter-test
spring-restdocs-mockmvc
spring-security-test

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
### /student: with column: Email, Department and GPA:
  1. /getHonorStudents : get all the honor students
  2. /getHighestGpaPerDepartment : get the best student from each department
  3. /sortHonorStudents : sort honor student by: sortBy(Column) and direction("asc" or "desc")
## connect to MySQL:
# built Schema named studentapp and inside one table:
the table is: student with (id, firstName, lastName, email, department and gpa)

# how the code built:
with controller-service-repository interface
model- student and department (ENUM) 
exception - student exception
dto- for honor student with 3 columns only
sql - there you can see the creation of the table
config - for making the server async with threadpool for efficient
util - message response and etc..
