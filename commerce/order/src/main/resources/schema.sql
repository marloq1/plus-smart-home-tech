CREATE TABLE IF NOT EXISTS addresses (
    address_id VARCHAR(255) PRIMARY KEY,
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    house VARCHAR(255),
    flat VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS orders (
       order_id VARCHAR(255) PRIMARY KEY,
       cart_id VARCHAR(255),
       address_id VARCHAR(255),
       user_name VARCHAR(255) NOT NULL,
       payment_id VARCHAR(255),
       delivery_id VARCHAR(255),
       state VARCHAR(16),
       delivery_weight FLOAT,
       delivery_volume FLOAT,
       fragile BOOLEAN,
       total_price FLOAT,
       delivery_price FLOAT,
       product_price FLOAT,
       CONSTRAINT uq_cart_id UNIQUE(cart_id),
       CONSTRAINT uq_user_name UNIQUE(user_name),
       FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS order_products (
  product_id VARCHAR(100) NOT NULL,
  quantity BIGINT,
  cart_id VARCHAR(100) REFERENCES orders(cart_id) ON DELETE CASCADE,
  PRIMARY KEY (cart_id, product_id)
);