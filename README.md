# User-Access-Management-System

This project is a web-based Java application that provides a role-based user access management system. Built using Java Servlets, JSP, and PostgreSQL, it allows users to register, request access to software, and for managers and admins to manage those requests.

Features
User Roles: Admin, Manager, Employee
Admin: Add software and define access levels
Employee: Request access to specific software
Manager: Approve or reject software access requests
Authentication: Secure login with session management

Tech Stack
Frontend: JSP, HTML, CSS
Backend: Java Servlets, JDBC
Database: PostgreSQL
Server: Apache Tomcat/10.1.41
IDE: IntelliJ IDEA

Create Database
CREATE DATABASE UserManagementSystem;

Create Tables
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT CHECK (role IN ('Employee', 'Manager', 'Admin')) NOT NULL
);
CREATE TABLE software (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    access_levels TEXT NOT NULL
);
CREATE TABLE requests (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    software_id INT REFERENCES software(id),
    access_type TEXT,
    reason TEXT,
    status TEXT
);

