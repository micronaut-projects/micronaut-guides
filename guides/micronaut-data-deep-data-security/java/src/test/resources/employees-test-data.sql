DELETE FROM hr.employees;

INSERT INTO hr.employees VALUES (100, 'Victoria', 'Williams',
                                 'victoria@supremo.onmicrosoft.com', NULL,
                                 '219-09-9999', 13000, '555-0100');

INSERT INTO hr.employees VALUES (200, 'Marvin', 'Anderson',
                                 'marvin@supremo.onmicrosoft.com', 'victoria@supremo.onmicrosoft.com',
                                 '457-55-5462', 12030, '555-0200');

INSERT INTO hr.employees VALUES (300, 'Chris', 'Evans',
                                 'chris@supremo.onmicrosoft.com', 'victoria@supremo.onmicrosoft.com',
                                 '321-12-4567', 6900, '555-0300');

INSERT INTO hr.employees VALUES (400, 'Emma', 'Baker',
                                 'emma@supremo.onmicrosoft.com', 'marvin@supremo.onmicrosoft.com',
                                 '733-02-9821', 8200, '555-0400');

INSERT INTO hr.employees VALUES (500, 'Taylor', 'Mills',
                                 'taylor@supremo.onmicrosoft.com', 'marvin@supremo.onmicrosoft.com',
                                 '558-76-1243', 9000, '555-0500');

COMMIT;
