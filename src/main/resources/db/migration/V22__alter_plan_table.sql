ALTER TABLE plans
DROP COLUMN description,
DROP COLUMN duration_unit,
DROP COLUMN features,
DROP COLUMN duration;

INSERT INTO plans (id, price, name) VALUES
    ('1', 0, 'Free'),
    ('2', 20, 'Basic'),
    ('3', 50, 'Advanced'),
    ('4', 100, 'Premium');