CREATE TABLE IF NOT EXISTS warehouse_product (
    product_id VARCHAR(255) PRIMARY KEY,
    quantity BIGINT,
    fragile BOOLEAN,
    width FLOAT NOT NULL,
    height FLOAT NOT NULL,
    depth FLOAT NOT NULL,
    weight FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS order_delivery (
    order_id VARCHAR(255),
    delivery_id VARCHAR(255),
    PRIMARY KEY(order_id,delivery_id)
);
