select e.employee_id, e.last_name, e.job_id, j.job_title
from employees e left outer join jobs j on e.job_id = j.job_id;