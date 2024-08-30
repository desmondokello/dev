-- Insert sample products
INSERT INTO Product (name, description, price) VALUES
                                                   ('Product A', 'Description for Product A', 29.99),
                                                   ('Product B', 'Description for Product B', 49.99);

-- Insert sample customers
INSERT INTO Customer (name, email, phone) VALUES
                                              ('John Doe', 'john.doe@example.com', '123-456-7890'),
                                              ('Jane Smith', 'jane.smith@example.com', '098-765-4321');

-- Insert sample inventories
INSERT INTO Inventory (product_id, quantity, location) VALUES
                                                           (1, 100, 'Warehouse A'),
                                                           (2, 50, 'Warehouse B');

-- Insert sample orders
INSERT INTO `Order` (customer_id, total) VALUES
                                             (1, 79.98),
                                             (2, 49.99);

-- Insert sample order items
INSERT INTO OrderItem (order_id, product_id, quantity, price) VALUES
                                                                  (1, 1, 2, 29.99),
                                                                  (2, 2, 1, 49.99);
