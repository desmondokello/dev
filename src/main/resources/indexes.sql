-- Index on Product's name
CREATE INDEX idx_product_name ON Product(name);

-- Index on Customer's email
CREATE INDEX idx_customer_email ON Customer(email);

-- Index on Inventory's product_id
CREATE INDEX idx_inventory_product_id ON Inventory(product_id);

-- Index on Order's customer_id
CREATE INDEX idx_order_customer_id ON `Order`(customer_id);

-- Index on OrderItem's order_id
CREATE INDEX idx_orderitem_order_id ON OrderItem(order_id);
