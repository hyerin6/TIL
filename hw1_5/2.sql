select e.last_name, e.first_name, COUNT(j.job_id)
from employees e right outer join job_history j on e.job_id = j.job_id
group by e.employee_id
having count(j.job_id) > 0;