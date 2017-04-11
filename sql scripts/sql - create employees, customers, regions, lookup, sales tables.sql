create table CMSC495.regions (
	region_id int primary key,
	description varchar(100) not null,
	manager_employee_id int not null
    );

-- select * from CMSC495.regions;
insert into CMSC495.regions values(1, 'East', 115472);
insert into CMSC495.regions values(2, 'West', 115473);
insert into CMSC495.regions values(3, 'North', 115474);
insert into CMSC495.regions values(4, 'South', 115475);
-- drop table CMSC495.regions;

create table CMSC495.lookup (
	table_id int primary key,
	report_table_name varchar(100) not null,
	description varchar(100) not null,
	report_year int not null,
	region_id int not null
    );

-- select * from CMSC495.lookup;
insert into CMSC495.lookup values(1, 'region1_sales_2016', '2016 Region 1 Sales', 2016, 1);
-- drop table CMSC495.lookup;

ALTER TABLE CMSC495.lookup
 ADD CONSTRAINT lookup_region_id_fk
 FOREIGN KEY(region_id)
 REFERENCES CMSC495.regions(region_id);

create table CMSC495.customers (
	customer_id	int primary key,
    customer_name varchar(100) not null,
	customer_email varchar(100),
	customer_phone varchar(15) not null
    );
    
-- select * from CMSC495.customers;
insert into CMSC495.customers values(172635, 'Wayne, Bruce', 'notbatman@batcave.com', '(555) 281-1989');
-- truncate CMSC495.customers;
-- drop table CMSC495.customers;

create table CMSC495.employees (
	employee_id	int primary key,
    employee_name varchar(100) not null,
	employee_email varchar(100) not null,
	employee_phone varchar(15) not null,
    hire_date date not null,
	region_id int not null
    );
    
-- select * from CMSC495.employees;
-- insert the employees used as regional managers
insert into CMSC495.employees values(115472, 'Johnson, Don', 'DonJohnson@gmail.com', '(555) 221-1234', '2006-08-31', 1);
insert into CMSC495.employees values(115473, 'Santana, Carlos', 'CarlosSantana@gmail.com', '(555) 221-1235', '2003-12-10', 2);
insert into CMSC495.employees values(115474, 'Murray, Bill', 'BillMurray@gmail.com', '(555) 221-1236', '2000-11-09', 3);
insert into CMSC495.employees values(115475, 'Smith, Matt', 'MattSmith@gmail.com', '(555) 221-1237', '2009-03-23', 4);
-- truncate CMSC495.employees;
-- drop table CMSC495.employees;

ALTER TABLE CMSC495.employees
 ADD CONSTRAINT employees_region_id_fk
 FOREIGN KEY(region_id)
 REFERENCES CMSC495.regions(region_id);
 
 ALTER TABLE CMSC495.regions
 ADD CONSTRAINT regions_employee_id_fk
 FOREIGN KEY(manager_employee_id)
 REFERENCES CMSC495.employees(employee_id);
 
 -- remove constraint before dropping employee
 -- ALTER TABLE CMSC495.regions DROP FOREIGN KEY regions_employee_id_fk;

create table CMSC495.region1_sales_2016 (
	transaction_id int primary key,
    customer_id	int not null,
    employee_id	int not null,
	transaction_date datetime not null,
	amount	float not null
    );

ALTER TABLE CMSC495.region1_sales_2016
 ADD CONSTRAINT region1_sales_2016_customer_id_fk
 FOREIGN KEY(customer_id)
 REFERENCES CMSC495.customers(customer_id);
 
ALTER TABLE CMSC495.region1_sales_2016
 ADD CONSTRAINT region1_sales_2016_employee_id_fk
 FOREIGN KEY(employee_id)
 REFERENCES CMSC495.employees(employee_id);
    
-- select * from CMSC495.region1_sales_2016;
insert into CMSC495.region1_sales_2016 values(1, 172635, 115472, '2016-01-01 11:23:00', 456.86);
-- truncate CMSC495.region1_sales_2016;
-- drop table CMSC495.region1_sales_2016;

create table CMSC495.region2_sales_2016 (
	transaction_id int primary key,
    customer_id	int not null,
    employee_id	int not null,
	transaction_date datetime not null,
	amount	float not null
    );

ALTER TABLE CMSC495.region2_sales_2016
 ADD CONSTRAINT region2_sales_2016_customer_id_fk
 FOREIGN KEY(customer_id)
 REFERENCES CMSC495.customers(customer_id);
 
ALTER TABLE CMSC495.region2_sales_2016
 ADD CONSTRAINT region2_sales_2016_employee_id_fk
 FOREIGN KEY(employee_id)
 REFERENCES CMSC495.employees(employee_id);
 
 create table CMSC495.region3_sales_2016 (
	transaction_id int primary key,
    customer_id	int not null,
    employee_id	int not null,
	transaction_date datetime not null,
	amount	float not null
    );

ALTER TABLE CMSC495.region3_sales_2016
 ADD CONSTRAINT region3_sales_2016_customer_id_fk
 FOREIGN KEY(customer_id)
 REFERENCES CMSC495.customers(customer_id);
 
ALTER TABLE CMSC495.region3_sales_2016
 ADD CONSTRAINT region3_sales_2016_employee_id_fk
 FOREIGN KEY(employee_id)
 REFERENCES CMSC495.employees(employee_id);
