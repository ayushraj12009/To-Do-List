APIwiz Project Assignment

Project: Task Management API

### Description:
   In this project,, I have implemented user authentication so that users can Sigin and then log in authenticated users can create, view, modify, update status, and delete their tasks.

### How To Run Project:
        - Clone this repository to your local machine.
        - Wait a few minutes to install dependencies.
        - Create a database in MySQL, then paste the database name in the application.properties file. You can choose any name.
        - Run the application

### Here are the APIs you can test using Swagger or Postman:
![image](https://github.com/ayushraj12009/apiwizAssignment/assets/51042913/a19f519c-99ff-476d-b497-80848022dd75)


- For easy understanding, I've named each API according to its functionality

1. **Signup**
   - Path: `http://localhost:8080/auth/signup`
   - Method: POST
   - Body {
    "fristName": "API",
    "lastName": "wiz",
    "userName": "APIwiz2024",
    "email": "task@gmail.com", 
    "password": "Task@#2024" 
}


2. **Signin**
   - Path: `http://localhost:8080/auth/signin`
   - Method: POST
   - Body {            
            "email":"task@gmail.com",
            "password":"Task@#2024"
          }

3. **Create Task**
   - Path: `http://localhost:8080/api/createTask`
   - Method: POST
   - Body {            
           "title": "Task 1",
            "description": "Task 1 description ",
            "dueDate": "19/03/2024",
            "status": "pending",
            "email":"task@gmail.com",
            "password":"Task@#2024"
          }

4. **Get All Task of User**
   - Path: `http://localhost:8080/api/getAllUserTask`
   - Method: GET
   - Body {            
           "email":"task@gmail.com",
           "password":"Task@#2024"
          }

5. **Find Task By ID**
   - Path: `http://localhost:8080/api/findTaskByID/{id}`
   - Method: GET
   - Body {            
           "email":"task@gmail.com",
           "password":"Task@#2024"
          }


6. **Find Task By Autheticate User**
   - Path: `http://localhost:8080/api/findTaskAuthenticateUserById/{id}`
   - Method: GET
   - Body {            
           "email":"task@gmail.com",
           "password":"Task@#2024"
          }

7. **Delete Task By Autheticate User**
   - Path: `http://localhost:8080/api/deleteTaskAuthenticateUserById/{id}`
   - Method: DELETE
   - Body {            
           "email":"task@gmail.com",
           "password":"Task@#2024"
          }

8. **Task Status or Task Update By Autheticate User**
   - Path: `http://localhost:8080/api/updateStatus`
   - Method: DELETE
   - Body {            
           "email":"task@gmail.com",
           "password":"Task@#2024",
           "taskId":8,
           "newStatus":"completed",
           "taskUpdate":"kam ho gya hai"
          }

### Note: For Admin To access all the taks
  - Path:`http://localhost:8080/admin/getAllUserTask`

### Additional Features

9. **Paginationr**
   - Path: `http://localhost:8080/api/getTaskList?page=0&size=10`
   - Method: GET
   - Description: Just copy this url and past in postman
  
10. **Filter**
   - Path: `http://localhost:8080/api/filterByStatus?status=completed&sortBy=dueDate&sortOrder=asc`
   - Method: GET
   - Description: Just copy this url and past in postman and go to params and filter according to status date ase or des.

11. Implemented error handling mechanisms and ensured the use of appropriate status codes in API responses. 

12. Comprehensive unit tests have been developed for critical components of the application to ensure its reliability and functionality.







     
