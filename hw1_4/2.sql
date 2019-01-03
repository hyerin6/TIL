select d.location_id, l.street_address, city, l.state_province, l.country_id
from departments d 
inner join locations l;
