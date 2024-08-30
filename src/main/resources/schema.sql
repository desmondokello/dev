-- Create Product table
CREATE TABLE Product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DECIMAL(10, 2) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Customer table
CREATE TABLE Customer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          phone VARCHAR(20),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Inventory table
CREATE TABLE Inventory (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           product_id BIGINT,
                           quantity INT NOT NULL,
                           location VARCHAR(255),
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (product_id) REFERENCES Product(id)
);

-- Create Order table
CREATE TABLE `Order` (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         customer_id BIGINT,
                         order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         total DECIMAL(10, 2) NOT NULL,
                         FOREIGN KEY (customer_id) REFERENCES Customer(id)
);

-- Create OrderItem table
CREATE TABLE OrderItem (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           order_id BIGINT,
                           product_id BIGINT,
                           quantity INT NOT NULL,
                           price DECIMAL(10, 2) NOT NULL,
                           FOREIGN KEY (order_id) REFERENCES `Order`(id),
                           FOREIGN KEY (product_id) REFERENCES Product(id)
);
