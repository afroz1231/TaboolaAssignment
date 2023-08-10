USE tabooladb;

/* Creation of Product Table */

CREATE TABLE product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    added_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    added_by VARCHAR(255) NOT NULL
);

/* Inserting Data into Product Table */

INSERT INTO product (name, category, added_by) VALUES ('TV', 'Electronics', 'John');
INSERT INTO product (name, category, added_by) VALUES ('BedFrame', 'Furniture', 'Peter');
INSERT INTO product (name, category, added_by) VALUES ('Denim Jeans', 'Clothing', 'Thomas');

/* Selecting Records Present in Product Table */

select * from product;

/* Creation of Product Price Table */

CREATE TABLE product_price (
    price_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    price DECIMAL(10,2) NOT NULL,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

/* Inserting Records in Product Price Table */

INSERT INTO product_price (product_id, price, updated_by) 
VALUES (1, 100.00, 'Michael');

INSERT INTO product_price (product_id, price, discount_percent, updated_by) 
VALUES (2, 500.00, 10, 'Ricky');

INSERT INTO product_price (product_id, price, discount_percent, updated_by) 
VALUES (3, 700.00, 5, 'Sara');

/* Selection of Product Price Table */

select * from product_price;

/* Creating Product Price Change Log Table */

CREATE TABLE product_price_change_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    price_id INT,
    old_price DECIMAL(10,2),
    new_price DECIMAL(10,2),
    changed_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(255) NOT NULL,
    FOREIGN KEY (price_id) REFERENCES product_price(price_id)
);

/* Inserting Records in Product Price Change Log Table */

INSERT INTO product_price_change_log (price_id, old_price, new_price, changed_by) 
VALUES (1, 100.00, 100.00, 'Admin');

INSERT INTO product_price_change_log (price_id, old_price, new_price, changed_by) 
VALUES (1, 500.00, 450.00, 'Admin');

INSERT INTO product_price_change_log (price_id, old_price, new_price, changed_by) 
VALUES (1, 700.00, 665.00, 'Admin');

/* Selection of Records in Product Price Change Log Table */

select * from product_price_change_log;

/*  Query to join Product Table and Product Price Table to display the product name, category, who/when 
it gets updated*/

SELECT 
    p.name AS product_name,
    p.category,
    pp.price,
    pp.updated_by,
    pp.updated_time
FROM 
    product p
JOIN 
    product_price pp ON p.product_id = pp.product_id;







