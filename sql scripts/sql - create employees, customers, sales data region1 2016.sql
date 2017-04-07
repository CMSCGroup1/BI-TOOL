create table CMSC495.lookup (
	table_id int primary key,
	report_table_name varchar(100) not null,
	description varchar(100) not null
    );

--select * from CMSC495.lookup;
insert into CMSC495.lookup values(1, 'region1_sales_2016', '2016 Region 1 Sales');
--drop table CMSC495.lookup;

create table CMSC495.customers (
	customer_id	int primary key,
    customer_name varchar(100) not null,
	customer_email varchar(100),
	customer_phone varchar(15) not null
    );
    
--select * from CMSC495.customers;
insert into CMSC495.customers values(72635, 'Wayne, Bruce', 'notbatman@batcave.com', '(555) 281-1989');
--truncate CMSC495.customers;

create table CMSC495.employees (
	employee_id	int primary key,
    employee_name varchar(100) not null,
	employee_email varchar(100) not null,
	employee_phone varchar(15) not null,
    hire_date date not null
    );
    
--select * from CMSC495.employees;
insert into CMSC495.employees values(15472, 'Johnson, Don', 'dude@buddy.com', '(555) 221-1234', '2006-08-31');
--truncate CMSC495.employees;
--drop table CMSC495.employees;

create table CMSC495.region1_sales_2016 (
	transaction_id int primary key,
    customer_id	int not null,
    employee_id	int not null,
	transaction_date datetime not null,
	amount	float not null
    );

ALTER TABLE CMSC495.region1_sales_2016
 ADD CONSTRAINT customer_id_fk
 FOREIGN KEY(customer_id)
 REFERENCES CMSC495.customers(customer_id);
 
ALTER TABLE CMSC495.region1_sales_2016
 ADD CONSTRAINT employee_id_fk
 FOREIGN KEY(employee_id)
 REFERENCES CMSC495.employees(employee_id);
    
--select * from CMSC495.region1_sales_2016;
insert into CMSC495.region1_sales_2016 values(1, 72635, 15472, '2016-01-01 11:23:00', 456.86);
--truncate CMSC495.region1_sales_2016;
--drop table CMSC495.region1_sales_2016;
