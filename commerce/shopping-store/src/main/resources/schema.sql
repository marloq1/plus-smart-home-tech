CREATE TABLE IF NOT EXISTS products (
  product_id VARCHAR(255),
  product_name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  image_src VARCHAR(512),
  quantity_state VARCHAR(6),
  product_state VARCHAR(10),
  product_category VARCHAR(10),
  price FLOAT,
  CONSTRAINT pk_user PRIMARY KEY (product_id),
  CONSTRAINT uq_product_id UNIQUE(product_id)
);