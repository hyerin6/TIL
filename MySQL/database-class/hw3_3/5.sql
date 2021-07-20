create view v_empmgr as
select e1.employee_id 'EMP#', e1.first_name 'EMP_NAME',
e1.manager_id 'MANAGER#', e2.first_name 'MANAGER_NAME'
from  employees e1  inner join employees b on e1. manager_id = e2.employee_id;
