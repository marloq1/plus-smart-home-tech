CREATE TABLE IF NOT EXISTS warehouse_product (
    product_id VARCHAR(255) PRIMARY KEY,
    quantity INTEGER,
    fragile BOOLEAN,
    width FLOAT NOT NULL,
    height FLOAT NOT NULL,
    depth FLOAT NOT NULL,
    weight FLOAT NOT NULL
);