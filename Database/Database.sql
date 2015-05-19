/*CREATE DATABASE IF NOT EXISTS team06;
use team06;*/

/*/********************Drop Tables*********************/
DROP TABLE IF EXISTS ClubMember;

DROP TABLE IF EXISTS Employees;

DROP TABLE IF EXISTS ReturnVehicle; 

DROP TABLE IF EXISTS RentalAgreement;

DROP TABLE IF EXISTS Reservation;

DROP TABLE IF EXISTS Additional_equipment;

DROP TABLE IF EXISTS ForsaleVehicles;

DROP TABLE IF EXISTS Vehicle;

DROP TABLE IF EXISTS VehicleType;

DROP TABLE IF EXISTS Branch;

DROP TABLE IF EXISTS Online_Customer;

DROP TABLE IF EXISTS Customer;

/*/******************Create Tables***********************/

CREATE TABLE Customer (
	Phone_number varchar(15) PRIMARY KEY, 
	Name Varchar(30),
	Address varchar(30),
	City varchar(20),
	Clubmember tinyint(1),  
	Roadstar tinyint(1))   
ENGINE = InnoDB;

CREATE TABLE Online_Customer (
	Phone_number varchar(15) PRIMARY KEY, 
	Username varchar(20) not null unique,
	Password varchar(20),
	FOREIGN KEY ( Phone_number ) REFERENCES Customer ( Phone_number )
	ON DELETE CASCADE ON UPDATE CASCADE)   
ENGINE = InnoDB;
  
CREATE TABLE ClubMember  (
	Phone_number  varchar(15)  primary key,
	Points  int DEFAULT 500,
	Mem_date DATETIME,
	Amount_spent float,
	Fee_paid tinyint(1), /*have to include*/
	FOREIGN KEY ( Phone_number ) REFERENCES Customer ( Phone_number )
	ON DELETE CASCADE ON UPDATE CASCADE)
ENGINE = InnoDB; 
   
/*we will deal with this table at a later point*/

CREATE TABLE Employees  (
	Emp_id  int PRIMARY KEY AUTO_INCREMENT,
	Emp_name varchar(30),
	Username  varchar(30) not null unique, 
	Password  varchar(15) not null,
	Type  varchar(10))
ENGINE = InnoDB;
   
CREATE TABLE Branch  (
	BranchID  int primary key,
	Location  varchar(64),
	City  varchar(20),
	Postal_code varchar(15),
	Phone_number  varchar(15))
ENGINE = InnoDB;
   
CREATE TABLE VehicleType(
	Vtype_name varchar(20) primary key,
	Features varchar(30),
	Category varchar(20), /*Have Included*/
	Weekly_rate float not null, 
	Daily_rate float not null, 
	Hourly_rate float not null, 
	Km_rate float not null, 
	Ins_wrate float not null,
	Ins_drate float not null,
	Ins_hrate float not null)
ENGINE = InnoDB;

CREATE TABLE Vehicle(
	Vlicense  int primary key, /*vehicle license no*/
	Vname  varchar(30) NOT NULL, /*name of the vehicle*/
	Vtype_name varchar(20) not null,  /*type of the vehicle which it belongs (eg: premium, economic etc...)*/
	Category varchar(20) not null, /*whether it is car or truck?*/
	Year int, /*vehicle bought year*/
	Initial_price double, /*initial price of the vehicle(ie. price at which the vehicle is bought for)*/
	Odometer float,
	BranchID int not null,  /*vehicle belongs to which branch*/
	ForRentFlag tinyint(1) not null, /*status of the vehicle whether it is for rent*/
	Status tinyint(1) not null, /*availability status*/
	FOREIGN KEY ( BranchID )references Branch ( BranchID ) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY ( Vtype_name ) REFERENCES VehicleType ( Vtype_name ) ON DELETE NO ACTION ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE TABLE ForsaleVehicles(
	Vlicense  int primary key,
	SalePrice double,
	SoldFlag tinyint(1) not null, 
	SoldPrice double,
	SoldDate DATE,
	SoldTo  varchar(20),
	FOREIGN KEY ( Vlicense ) REFERENCES Vehicle ( Vlicense ))
ENGINE = InnoDB;

CREATE TABLE Additional_equipment(
	EquipmentName varchar(20) primary key,
	VehicleCategory varchar(15) not null, /*it is of type either car or truck */
	Daily_rate float not null, 
	Hourly_rate float not null,
	AvailabaleQty int)  /*not sure if we need this column, we can remove it if we don't*/
ENGINE = InnoDB;

CREATE TABLE Reservation(
	Confno int primary key AUTO_INCREMENT,
	Phone_number varchar(15) not null, 
	Vtype_name varchar(20) not null,
	Vlicense int not null,
	BranchID  int not null,
	Pickup_time datetime, 
	Dropoff_time datetime,
	Equipment varchar(20),
	FOREIGN KEY ( Phone_number )references Customer ( Phone_number ) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY ( Vtype_name ) REFERENCES VehicleType ( Vtype_name ) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY ( BranchID )references Branch ( BranchID ) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY ( Vlicense )references Vehicle ( Vlicense ) ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY ( Equipment ) REFERENCES Additional_equipment ( EquipmentName ))
ENGINE = InnoDB;

CREATE TABLE RentalAgreement(
	RentId int primary key AUTO_INCREMENT, 
	ConfNo int,
	Phone_number varchar(15) not null,
	Dlicense int,
	Vlicense int not null,
	CardNo int, 
	ExpiryDate date, 
	CardType Varchar(20),
	Equipment varchar(20), 
	Odometer float not null,
	Pickup_time datetime, 
	Dropoff_time datetime,
	FOREIGN KEY ( ConfNo )references Reservation ( ConfNo ),
	FOREIGN KEY ( Phone_number )references Customer ( Phone_number ) ON DELETE no action ON UPDATE CASCADE,
	FOREIGN KEY ( Vlicense ) REFERENCES Vehicle ( Vlicense ) ON DELETE no action ON UPDATE CASCADE,
	FOREIGN KEY ( Equipment ) REFERENCES Additional_equipment ( EquipmentName ))
ENGINE = InnoDB;

CREATE TABLE ReturnVehicle(
	RentId int primary key, 
	Dropoff_time datetime,
	Fulltank tinyint(1),
	Odometer float,
	Cost float,
	FOREIGN KEY ( RentId ) REFERENCES RentalAgreement (RentId) ON DELETE CASCADE ON UPDATE CASCADE)
ENGINE = InnoDB; 


/*/*****************************************************************/

/*Trigger Club_Membership */
/*DELIMITER $$
CREATE TRIGGER club_member_update 
AFTER UPDATE ON ClubMember
FOR EACH ROW BEGIN

  UPDATE Customer 
  SET Clubmember = new.Fee_paid
  WHERE Phone_number = old.Phone_number; 

END $$
DELIMITER ;

SET GLOBAL event_scheduler = ON;*/

/*Event Club_Membership*/
/*
DELIMITER $$
CREATE EVENT club_membership
ON SCHEDULE EVERY 1 day 
STARTS CURRENT_TIMESTAMP
ENDS '2016-04-15 00:00.00'
DO BEGIN
   
   UPDATE ClubMember set Fee_paid=0,Mem_date=sysdate()
   where floor(abs(((DATEDIFF(sysdate(),Mem_date))/365)))>=1;
   
END $$
DELIMITER ;
*/

/*Event Kill Process*/
/*
DELIMITER $$
CREATE EVENT e_kill_process
ON SCHEDULE EVERY 1 minute 
STARTS CURRENT_TIMESTAMP
ENDS '2016-04-15 00:00.00'
DO BEGIN
DECLARE a int;
SELECT count(1) into a FROM INFORMATION_SCHEMA.PROCESSLIST; 

if a>15 then
call kill_process;
end if;

END $$
DELIMITER ;
*/


/*Stored procedure kill_process*/
/*
DELIMITER //
CREATE PROCEDURE kill_process()
BEGIN
DECLARE a VARCHAR(20);
DECLARE no_more_rows BOOLEAN;
DECLARE c_kill CURSOR FOR SELECT CONCAT('KILL ',id,';') FROM INFORMATION_SCHEMA.PROCESSLIST where (db like '%team06%' or db like '%crs%') and (command not like '%Query%' and command not like '%Daemon%');
DECLARE CONTINUE HANDLER FOR NOT FOUND

  SET no_more_rows = true;
  OPEN c_kill;
  get_process: LOOP
	FETCH c_kill INTO a;
	IF no_more_rows THEN
		CLOSE c_kill;
		LEAVE get_process;
	END IF;

	SET @s = a;
	PREPARE stmt FROM @s;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
  END LOOP get_process;
  
END//
DELIMITER ;
*/

/*
show processlist;

call kill_process;*/
