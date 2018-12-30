select last_name, first_name, salary from employees where salary > (select AVG(salary) from employees);

