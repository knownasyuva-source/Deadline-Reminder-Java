CREATE DATABASE deadlineDB;
USE deadlineDB;

CREATE TABLE deadlines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    type VARCHAR(20),
    dueDate DATE,
    subject VARCHAR(50),
    teamMembers VARCHAR(100)
);
