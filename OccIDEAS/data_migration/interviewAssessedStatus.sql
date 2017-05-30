update Interview 
set assessedStatus = 'Not Assessed'
where assessedStatus = '' or assessedStatus is null;