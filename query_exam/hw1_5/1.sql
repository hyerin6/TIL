select d.department_name, MAX(e.salary), MIN(e.salary), MAX(e.salary) - MIN(e.salary)
from employees e join departments d
on e.department_id = d.department_id 
group by d.department_id;