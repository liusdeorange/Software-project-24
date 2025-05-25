# Account-Book Accounting book application

## Project Introduction
The personal account book application developed based on Java and designed with the MVC architecture helps users record and manage their daily income and expenditure.

## Functional characteristics
- 📅 Check income and expenditure (range query based on year/month/day)
- 📊 Statistical report
- 💰 Income and expenditure record management (add, delete, modify, query/Batch import/batch export)
- 🤖 Intelligent consumption classification consumption suggestions
- 📁 CSV file path migration
- ⚙️ Account Settings (password, gender, age)

## Project structure
![屏幕截图 2025-05-25 194245](https://github.com/user-attachments/assets/2b0e05ee-4687-46a2-b5ca-51f166f8da46)

### 🗂 Core Module Description

| Table of Contents                | Function description |
|---------------------|--------------------------|
| **controller**     | Handle user operations and invoke business logic |
| **model**          | Data entity class(user/csv/account)|
| **test**           | Test code|
| **util**           | Tool Class (Ai Classification /MD5)|
| **view**           | User interface implementation       |
| **resources**      | Static resources and configuration files|

### 📝 配置文件说明
- `config.properties`:Contains the default account file storage path
- `testUser_finance.csv`:Test the CSV data of the user (fields: date, category, amount, remarks)

## 🚀 Start quickly
### 🛠 Development environment
- JDK 21
- Maven 3.9+

### Instructions for Use
#### 1Clone project[Account-Book](https://github.com/liusdeorange/Software-project-24/tree/main/Account-Book)
#### 2.Build the project using maven
#### 3.Compile and run directly**main.java**<ins> (Account-Book/src/main/java/main.java)</ins>
