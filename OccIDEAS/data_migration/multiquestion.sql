update Interview_Question
set type = "Q_multiple" 
where type is null and question_id in (select idNode from Node where type="Q_multiple")