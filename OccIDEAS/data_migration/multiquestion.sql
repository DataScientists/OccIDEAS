update interview_question
set type = "Q_multiple" 
where type is null and question_id in (select idNode from node where type="Q_multiple")