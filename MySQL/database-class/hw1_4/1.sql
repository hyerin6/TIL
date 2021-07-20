select e.employee_id, e.last_name, d.department_id, d.department_name 
from employees e inner join departments d 
on e.department_id = d.department_id 
where e.department_id <= 100;
