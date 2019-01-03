select e1.employee_id 'EMP#', e1.first_name 'EMP_NAME',
e2.employee_id 'MANAGER#', e2.first_name 'MANAGER_NAME'
from employees e1 
inner join employees e2 
on e1.manager_id = e2.employee_id;