-- Insert sample data into Product Table
INSERT INTO product_table (name, description, price, stock, created_by)
VALUES
    ('Product 1', 'Description for product 1', 19.99, 100, 'admin'),
    ('Product 2', 'Description for product 2', 29.99, 200, 'admin');

-- Insert sample data into Customer Table
INSERT INTO customer_table (name, email, phone, created_by)
VALUES
    ('John Doe', 'john.doe@example.com', '123-456-7890', 'admin'),
    ('Jane Smith', 'jane.smith@example.com', '098-765-4321', 'admin');

-- Insert sample data into Inventory Table
INSERT INTO inventory_table (product_id, quantity, location, created_by)
VALUES
    (1, 50, 'Warehouse A', 'admin'),
    (2, 30, 'Warehouse B', 'admin');

-- Insert sample data into Order Table
INSERT INTO order_table (customer_id, total_price, status, order_date, created_by)
VALUES
    (1, 49.99, 'Pending', NOW(), 'admin'),
    (2, 29.99, 'Shipped', NOW(), 'admin');

-- Insert sample data into OrderItem Table
INSERT INTO order_item_table (order_id, product_id, quantity, price, created_by)
VALUES
    (1, 1, 2, 19.99, 'admin'),
    (1, 2, 1, 29.99, 'admin'),
    (2, 2, 1, 29.99, 'admin');
