UPDATE parent SET parent_number = new_parent_number FROM (
SELECT corp_name, parent_number, ordered_nums[1]::bigint as new_parent_number FROM (
SELECT corp_name, parent_number, array_agg(parsed_corp_number order by length(parsed_corp_number) DESC) as ordered_nums from (
select corp_name, parent_number, regexp_split_to_table(corp_name, '[^0-9]') as parsed_corp_number 
from parent) a
group by corp_name, parent_number) b) c
WHERE c.corp_name = parent.corp_name AND parent.parent_number = 0;