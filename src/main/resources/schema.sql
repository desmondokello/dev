-- Create Product Table
CREATE TABLE product_table (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               description TEXT NOT NULL,
                               price DECIMAL(10, 2) NOT NULL,
                               stock INT NOT NULL,
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at DATETIME DEFAULT NULL,
                               deleted_flag TINYINT(1) DEFAULT 0,
                               deleted_at DATETIME DEFAULT NULL,
                               created_by VARCHAR(255) DEFAULT NULL,
                               created_date DATETIME DEFAULT NULL,
                               last_modified_by VARCHAR(255) DEFAULT NULL,
                               last_modified_date DATETIME DEFAULT NULL,
                               deleted_by VARCHAR(255) DEFAULT NULL,
                               deleted_date DATETIME DEFAULT NULL
);

-- Create Customer Table
CREATE TABLE customer_table (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                name VARCHAR(255) NOT NULL,
                                email VARCHAR(255) NOT NULL UNIQUE,
                                phone VARCHAR(50) NOT NULL,
                                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at DATETIME DEFAULT NULL,
                                deleted_flag TINYINT(1) DEFAULT 0,
                                deleted_at DATETIME DEFAULT NULL,
                                created_by VARCHAR(255) DEFAULT NULL,
                                created_date DATETIME DEFAULT NULL,
                                last_modified_by VARCHAR(255) DEFAULT NULL,
                                last_modified_date DATETIME DEFAULT NULL,
                                deleted_by VARCHAR(255) DEFAULT NULL,
                                deleted_date DATETIME DEFAULT NULL
);

-- Create Inventory Table
CREATE TABLE inventory_table (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 product_id BIGINT NOT NULL,
                                 quantity INT NOT NULL,
                                 location VARCHAR(255) NOT NULL,
                                 created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at DATETIME DEFAULT NULL,
                                 deleted_flag TINYINT(1) DEFAULT 0,
                                 deleted_at DATETIME DEFAULT NULL,
                                 created_by VARCHAR(255) DEFAULT NULL,
                                 created_date DATETIME DEFAULT NULL,
                                 last_modified_by VARCHAR(255) DEFAULT NULL,
                                 last_modified_date DATETIME DEFAULT NULL,
                                 deleted_by VARCHAR(255) DEFAULT NULL,
                                 deleted_date DATETIME DEFAULT NULL,
                                 FOREIGN KEY (product_id) REFERENCES product_table(id)
);

-- Create Order Table
CREATE TABLE order_table (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             customer_id BIGINT NOT NULL,
                             total_price DECIMAL(10, 2) NOT NULL,
                             status VARCHAR(50) NOT NULL,
                             order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT NULL,
                             deleted_flag TINYINT(1) DEFAULT 0,
                             deleted_at DATETIME DEFAULT NULL,
                             created_by VARCHAR(255) DEFAULT NULL,
                             created_date DATETIME DEFAULT NULL,
                             last_modified_by VARCHAR(255) DEFAULT NULL,
                             last_modified_date DATETIME DEFAULT NULL,
                             deleted_by VARCHAR(255) DEFAULT NULL,
                             deleted_date DATETIME DEFAULT NULL,
                             FOREIGN KEY (customer_id) REFERENCES customer_table(id)
);

-- Create OrderItem Table
CREATE TABLE order_item_table (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  order_id BIGINT NOT NULL,
                                  product_id BIGINT NOT NULL,
                                  quantity INT NOT NULL,
                                  price DECIMAL(10, 2) NOT NULL,
                                  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME DEFAULT NULL,
                                  deleted_flag TINYINT(1) DEFAULT 0,
                                  deleted_at DATETIME DEFAULT NULL,
                                  created_by VARCHAR(255) DEFAULT NULL,
                                  created_date DATETIME DEFAULT NULL,
                                  last_modified_by VARCHAR(255) DEFAULT NULL,
                                  last_modified_date DATETIME DEFAULT NULL,
                                  deleted_by VARCHAR(255) DEFAULT NULL,
                                  deleted_date DATETIME DEFAULT NULL,
                                  FOREIGN KEY (order_id) REFERENCES order_table(id),
                                  FOREIGN KEY (product_id) REFERENCES product_table(id)
);
