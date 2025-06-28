CREATE TABLE IF NOT EXISTS payments (
    payment_id VARCHAR(255) PRIMARY KEY,
    order_id VARCHAR(255),
    total_payment FLOAT,
    delivery_total FLOAT,
    fee_total FLOAT,
    payment_state VARCHAR(8)
);