select last_name, first_name, salary from employees where salary > all (select salary from employees where job_id = 'SH_CLERK');