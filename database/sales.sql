USE healthfirst_pims;

SELECT *
FROM sales
ORDER BY sale_id DESC
LIMIT 5;

SELECT *
FROM sale_items
ORDER BY sale_item_id DESC
LIMIT 10;

SELECT medicine_id, name, quantity_in_stock
FROM medicines;
