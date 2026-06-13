INSERT INTO category (title) VALUES ('Electronics');
INSERT INTO category (title) VALUES ('Books');
INSERT INTO category (title) VALUES ('Clothing');
INSERT INTO category (title) VALUES ('Home Appliances');
INSERT INTO category (title) VALUES ('Sports');
INSERT INTO category (title) VALUES ('Toys');
INSERT INTO category (title) VALUES ('Groceries');
INSERT INTO category (title) VALUES ('Beauty');
INSERT INTO category (title) VALUES ('Automotive');
INSERT INTO category (title) VALUES ('Furniture');


-- Products for Electronics
INSERT INTO product (name, description, category_id, maximum_retail_price, image_url)
VALUES ('Smartphone Model 1', 'Android smartphone with 64GB storage', 1, 19999.00, 'http://example.com/images/smartphone1.jpg');

INSERT INTO product (name, description, category_id, maximum_retail_price, image_url)
VALUES ('Smartphone Model 2', 'Android smartphone with 128GB storage', 1, 24999.00, 'http://example.com/images/smartphone2.jpg');

-- Products for Books
INSERT INTO product (name, description, category_id, maximum_retail_price, image_url)
VALUES ('Book Title 1', 'Fiction novel', 2, 499.00, 'http://example.com/images/book1.jpg');

INSERT INTO product (name, description, category_id, maximum_retail_price, image_url)
VALUES ('Book Title 2', 'Science textbook', 2, 999.00, 'http://example.com/images/book2.jpg');

-- Products for Clothing
INSERT INTO product (name, description, category_id, maximum_retail_price, image_url)
VALUES ('T-Shirt 1', 'Cotton round-neck T-shirt', 3, 799.00, 'http://example.com/images/tshirt1.jpg');

INSERT INTO product (name, description, category_id, maximum_retail_price, image_url)
VALUES ('T-Shirt 2', 'Polyester sports T-shirt', 3, 999.00, 'http://example.com/images/tshirt2.jpg');

    
