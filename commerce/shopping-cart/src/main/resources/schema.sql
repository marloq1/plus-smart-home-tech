CREATE TABLE IF NOT EXISTS carts (
    cart_id VARCHAR(255) DEFAULT gen_random_uuid() PRIMARY KEY,
    user_name VARCHAR(255),
    cart_state BOOLEAN,
    CONSTRAINT uq_user_name UNIQUE(user_name)
);

CREATE TABLE IF NOT EXISTS cart_products (
  product_id VARCHAR(100) NOT NULL,
  quantity BIGINT,
  cart_id VARCHAR(100) REFERENCES carts(cart_id) ON DELETE CASCADE,
  PRIMARY KEY (cart_id, product_id)
);