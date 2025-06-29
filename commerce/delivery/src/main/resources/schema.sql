CREATE TABLE IF NOT EXISTS addresses (
    address_id VARCHAR(255) PRIMARY KEY,
    country VARCHAR(255),
    city VARCHAR(255),
    street VARCHAR(255),
    house VARCHAR(255),
    flat VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id VARCHAR(255) DEFAULT gen_random_uuid() PRIMARY KEY,
    from_address_id VARCHAR(255) NOT NULL,
    to_address_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(255) NOT NULL,
    delivery_state VARCHAR(16) NOT NULL,
    FOREIGN KEY (from_address_id) REFERENCES addresses(address_id) ON DELETE CASCADE,
    FOREIGN KEY (to_address_id) REFERENCES addresses(address_id) ON DELETE CASCADE
);

