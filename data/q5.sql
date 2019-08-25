SELECT name FROM (
    SELECT m.name AS name, COUNT(book_id) + COUNT(movie_id) AS sum_items
        FROM checkout_item INNER JOIN member AS m ON m.id = member_id
        GROUP BY member_id
) WHERE sum_items > 1;