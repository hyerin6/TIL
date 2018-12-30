select employee_id, salary, commission_pct from employees where commission_pct IS NOT NULL order by salary, commission_pct;
