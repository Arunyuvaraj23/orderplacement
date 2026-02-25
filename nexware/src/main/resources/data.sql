-- Insert Users
INSERT INTO users (name) VALUES ('Arun');          -- Will get ID 1
INSERT INTO users (name) VALUES ('Yuvi');          -- Will get ID 2
INSERT INTO users (name) VALUES ('Nexware Admin'); -- Will get ID 3

-- Insert Products
-- Note: version is set to 0 for Optimistic Locking
INSERT INTO products (name, price, stock, version) VALUES ('Wireless Mouse', 25.50, 100, 0);   -- ID 1
INSERT INTO products (name, price, stock, version) VALUES ('Mechanical Keyboard', 89.99, 50, 0);-- ID 2
INSERT INTO products (name, price, stock, version) VALUES ('Monitor 24 inch', 150.00, 30, 0);   -- ID 3
INSERT INTO products (name, price, stock, version) VALUES ('USB-C Hub', 45.00, 200, 0);         -- ID 4
INSERT INTO products (name, price, stock, version) VALUES ('Laptop Stand', 35.00, 75, 0);       -- ID 5