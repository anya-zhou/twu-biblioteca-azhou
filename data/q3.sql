SELECT b.title FROM book AS b WHERE b.id NOT IN (SELECT book_id FROM checkout_item WHERE book_id IS NOT NULL)
UNION
SELECT m.title FROM movie AS m WHERE m.id NOT IN (SELECT movie_id FROM checkout_item WHERE movie_id IS NOT NULL);

