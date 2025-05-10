-- Include your create table DDL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO COMP421;

-- Remember to put the create table ddls for the tables with foreign key references
--    ONLY AFTER the parent tables have already been created.

-- This is only an example of how you add create table ddls to this file.
--   You may remove it.
CREATE TABLE Employee 
(
eid INTEGER PRIMARY KEY NOT NULL,
fName VARCHAR(20),
lName VARCHAR(30),
shift VARCHAR(10),
wage DECIMAL CHECK(wage > 13.0)
);
	
CREATE TABLE Chef 
(
eid INTEGER PRIMARY KEY NOT NULL,
FOREIGN KEY (eid) REFERENCES Employee,
rank INTEGER
);

CREATE TABLE Manager
(
eid INTEGER PRIMARY KEY NOT NULL,
FOREIGN KEY (eid) REFERENCES Employee
);

CREATE TABLE Waiter 
(
eid INTEGER PRIMARY KEY NOT NULL,
FOREIGN KEY (eid) REFERENCES Employee
);

CREATE TABLE Host
(
eid INTEGER PRIMARY KEY NOT NULL,
FOREIGN KEY (eid) REFERENCES Employee
);

CREATE TABLE Ingredient 
(
name VARCHAR(20) PRIMARY KEY NOT NULL,
stock DECIMAL CHECK(stock >= 0.0)
);

CREATE TABLE MenuItem 
(
name VARCHAR(50) PRIMARY KEY NOT NULL,
description VARCHAR(100),
price DECIMAL
);

CREATE TABLE Supplier 
(
supplierId INT PRIMARY KEY NOT NULL,
name VARCHAR(50),
email VARCHAR(50),
phone VARCHAR(10)
);

CREATE TABLE Table
(
tableNumber INTEGER PRIMARY KEY NOT NULL,
size INTEGER
);

CREATE TABLE Bill 
(
billId INTEGER PRIMARY KEY NOT NULL,
date DATE,
time TIME,
amtPaid DECIMAL
);

CREATE TABLE Reservation
(
resId INTEGER PRIMARY KEY NOT NULL,
fName VARCHAR(30),
lName VARCHAR(30),
date DATE,
time TIME,
size INTEGER,
hostId INTEGER,
FOREIGN KEY (hostId) REFERENCES Host
);

CREATE TABLE Seats
(
tableNumber INTEGER PRIMARY KEY NOT NULL,
hostId INTEGER,
time TIME,
FOREIGN KEY (tableNumber) REFERENCES Table,
FOREIGN KEY (hostId) REFERENCES Host
);

CREATE TABLE ServesCheque 
(
tableNumber INTEGER NOT NULL,
billId INTEGER NOT NULL,
time TIME NOT NULL,
PRIMARY KEY (tableNumber, billId),
FOREIGN KEY (tableNumber) REFERENCES Table,
FOREIGN KEY (billId) REFERENCES Bill,
waiterId INTEGER,
FOREIGN KEY (waiterId) REFERENCES Waiter
);

CREATE TABLE Assignment 
(
tableNumber INTEGER NOT NULL,
resId INTEGER NOT NULL,
PRIMARY KEY (tableNumber, resId),
FOREIGN KEY (tableNumber) REFERENCES Table,
FOREIGN KEY (resId) REFERENCES Reservation
);

CREATE TABLE Attends 
(
waiterId INTEGER NOT NULL,
tableNumber INTEGER NOT NULL,
PRIMARY KEY (waiterId, tableNumber),
FOREIGN KEY (waiterId) REFERENCES Waiter,
FOREIGN KEY (tableNumber) REFERENCES Table
);

CREATE TABLE Contains
(
itemName VARCHAR(30) NOT NULL,
billId INTEGER NOT NULL,
PRIMARY KEY (itemname, billId),
FOREIGN KEY (itemName) REFERENCES MenuItem,
FOREIGN KEY (billId) REFERENCES Bill
);

CREATE TABLE Modifies
(
chefId INTEGER NOT NULL,
managerId INTEGER NOT NULL,
itemName VARCHAR(30) NOT NULL,
PRIMARY KEY (chefId, managerId, itemName),
FOREIGN KEY (chefId) REFERENCES Chef,
FOREIGN KEY (managerId) REFERENCES Manager,
FOREIGN KEY (itemName) REFERENCES MenuItem
);

CREATE TABLE MakesUp 
(
ingredientName VARCHAR(20) NOT NULL,
itemName VARCHAR(30) NOT NULL,
PRIMARY KEY (ingredientName, itemname),
FOREIGN KEY (ingredientName) REFERENCES Ingredient,
FOREIGN KEY (itemName) REFERENCES MenuItem
);

CREATE TABLE Orders
(
managerId INTEGER NOT NULL,
ingredientname VARCHAR(20) NOT NULL,
supplierId INTEGER NOT NULL,
PRIMARY KEY (managerId, ingredientName, supplierId),
FOREIGN KEY (managerId) REFERENCES Manager,
FOREIGN KEY (ingredientName) REFERENCES Ingredient,
FOREIGN KEY (supplierId) REFERENCES Supplier,
quantity DECIMAL,
date DATE
);

