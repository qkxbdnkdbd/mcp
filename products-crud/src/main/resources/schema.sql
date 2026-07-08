CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    barcode VARCHAR(64),
    specification VARCHAR(255),
    category_id INT,
    brand VARCHAR(128),
    purchase_price DECIMAL(12, 2) DEFAULT 0.00,
    selling_price DECIMAL(12, 2) DEFAULT 0.00,
    unit VARCHAR(64),
    stock_quantity INT DEFAULT 0,
    min_stock INT DEFAULT 0,
    max_stock INT,
    image_url VARCHAR(512),
    description TEXT,
    status INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(64),
    supplier_id INT
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    display_name VARCHAR(128),
    role VARCHAR(32) NOT NULL DEFAULT 'ADMIN',
    status INT NOT NULL DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME
);
