update Interview_Question
set type = "Q_multiple" 
where type is null and question_id in (select idNode from Node where type="Q_multiple")

update Interview_Question a, node b
set a.type = b.type
where 
question_id = idNode
and b.type <> a.type