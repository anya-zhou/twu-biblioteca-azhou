SELECT COUNT(m.name) FROM member AS m
    WHERE m.id NOT IN
    (SELECT member_id from checkout_item);