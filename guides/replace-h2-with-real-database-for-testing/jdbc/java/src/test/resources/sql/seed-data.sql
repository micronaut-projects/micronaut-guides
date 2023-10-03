INSERT INTO products(id, code, name) VALUES(1, 'p101', 'Apple MacBook Pro') ON CONFLICT DO NOTHING;
INSERT INTO products(id, code, name) VALUES(2, 'p102', 'Sony TV') ON CONFLICT DO NOTHING;
