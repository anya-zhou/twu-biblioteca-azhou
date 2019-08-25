INSERT INTO book (id, title) VALUES (11, "The Pragmatic Programmer");

INSERT INTO member (id, name) VALUES (43, "Annie Zhou");

INSERT INTO checkout_item (member_id, book_id) VALUES (43, 11);

SELECT m.name FROM member AS m, book AS b, checkout_item AS c
    WHERE b.title = "The Pragmatic Programmer"
    AND b.id = c.book_id
    AND m.id = c.member_id;