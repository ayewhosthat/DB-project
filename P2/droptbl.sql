-- Include your drop table DDL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO COMP421;

-- Remember to put the drop table ddls for the tables with foreign key references
--    BEFORE the ddls to drop the parent tables (reverse of the creation order).

-- This is only an example of how you add drop table ddls to this file.
--   You may remove it.
DROP TABLE Employee;
DROP TABLE Chef;
DROP TABLE Manager;
DROP TABLE Waiter;
DROP TABLE Host;
DROP TABLE Ingredient;
DROP TABLE Supplier;
DROP TABLE Table;
DROP TABLE Bill;
DROP TABLE Reservation;
DROP TABLE Seats;
DROP TABLE ServesCheque;
DROP TABLE Assignment;
DROP TABLE Attends;
DROP TABLE Contains;
DROP TABLE Modifies;
DROP TABLE MakesUp;
DROP TABLE Orders;
DROP TABLE MenuItem;

