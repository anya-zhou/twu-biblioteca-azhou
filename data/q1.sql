SELECT m.name FROM member AS m, book AS b, checkout_item AS c
    WHERE b.title = "The Hobbit"
    AND b.id = c.book_id
    AND m.id = c.member_id;