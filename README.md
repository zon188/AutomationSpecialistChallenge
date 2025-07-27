# CRUD App with Authentication

This project is a simple full-stack application that supports basic CRUD operations with user authentication.

## Features

- User Registration and Login
- JWT Token Authentication
- Protected Routes
- CRUD operations (Create, Read, Update, Delete) for users

## Tools and Technologies Used

- **Backend**: Node.js, Express.js
- **Database**: MongoDB with Mongoose
- **Authentication**: JSON Web Tokens (JWT)
- **API Testing Tool**: Postman
- **Automation Testing**: Selenium WebDriver, TestNG, Java
- **Testing Pattern**: Page Object Model (POM)
- **Test Reporting**: Extent Reports

## Project Structure for https://github.com/jamesoyanna/Crud-app-Auth this project is alrday exit

Crud-app-Auth/
├── assets/
├── client/
├── config/
├── controller/
├── middleware/
├── models/
├── routes/
├── utils/
├── index.js
├── .env
├── .gitignore
├── package.json
├── README.md

## project is alrday exit ٍSetup:


## Setup Instructions

### Frontend

1. Navigate to the frontend folder:
  
   cd client
Install dependencies:


npm install
Start the React development server:


npm start
The frontend runs at http://localhost:3000.


Go back to the root directory, then enter backend folder:

cd ..
cd backend
npm install
The backend runs at http://localhost:4000.




Running Tests
API Tests
API tests are automated using Rest-Assured in Java.

To run API tests:

Open the project in your Java IDE (e.g., IntelliJ IDEA).
rest-assured  is used as the test framework

Run the TestNG test suite for API tests located in corFounctions Folder Class ApiTest.

UI Automation Tests
UI tests are automated using Selenium WebDriver with WebDriverManager.

TestNG is used as the test framework.

ExtentReports is used for generating HTML test reports.

To run UI tests:

Open the project in your Java IDE.

Run the TestNG test suite located in execution Folder Class TestCase.









## Testing

Before writing automated API tests, we tested all endpoints manually using **Postman**.

For UI automation, we used:

- **Java** with **Selenium WebDriver**
- TestNG as the test framework
- **ExtentReports** for generating HTML reports
- Hardcoded test data (due to time limits)
- Page Object Model (POM) as the main design pattern in test scripts

## Note



