Report ideas

-- total number of rows in the table
select count(*) from CMSC495.region1_sales_2016;

-- oldest transaction date
select min(transaction_date) from CMSC495.region1_sales_2016;

-- newest transaction date
select max(transaction_date) from CMSC495.region1_sales_2016;

-- number of distinct employees
select count(distinct employee_id) from CMSC495.region1_sales_2016;

-- number of distinct customers
select count(distinct customer_id) from CMSC495.region1_sales_2016;

-- total amount for the report
select truncate((select sum(amount)from CMSC495.region1_sales_2016), 2) as report_average;

-- average amount for the report
select truncate((select avg(amount) from CMSC495.region1_sales_2016), 2) as report_average;



-- joining names for customers and employees to the table results

select r.transaction_id, r.transaction_date, r.amount, 
r.customer_id, c.customer_name, r.employee_id, e.employee_name 
from CMSC495.region1_sales_2016 r
join CMSC495.employees e on r.employee_id = e.employee_id
join CMSC495.customers c on r.customer_id = c.customer_id;