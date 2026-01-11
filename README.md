# 📚 Library Management System

A robust, console-based Library Management System built using **Java** and **JDBC** (Java Database Connectivity). This application allows users to manage library records, offering functionality to add, borrow, and return books seamlessly while persisting data in a MySQL database.

## 🚀 Features

* **Add Books:** Insert new book records (Title, Author) into the database.
* **View Catalog:** Display all books with their current status (Available/Borrowed).
* **Borrow Books:** Check out books using their ID (updates database status to unavailable).
* **Return Books:** Check in books (updates database status back to available).
* **Persistent Storage:** All data is stored in a MySQL database, ensuring data is saved even after the application closes.

## 🛠️ Tech Stack

* **Language:** Java (JDK 8+)
* **Database:** MySQL
* **API:** JDBC (Java Database Connectivity)
* **IDE:** IntelliJ IDEA / Eclipse / VS Code (Optional)

## ⚙️ Prerequisites

Before running the application, ensure you have the following installed:

1.  **Java Development Kit (JDK)** installed on your machine.
2.  **MySQL Server** installed and running.
3.  **MySQL JDBC Driver** (Connector/J) added to your project's classpath/library.

## 💾 Database Setup

1.  Open your MySQL Workbench or Command Line.
2.  Run the following SQL script to set up the database and table:

```sql
CREATE DATABASE library_db;

USE library_db;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);
