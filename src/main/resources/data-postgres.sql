-- Address
INSERT INTO addresses (id, postal_code, street, neighborhood, city, state, number)
VALUES ('2ac9e579-9f46-4b3c-8b5d-409c4b0bbd04', '11111-111', 'Generic Street', 'Generic Neighborhood', 'Generic City', 'Generic State', '2');

-- User
INSERT INTO users (id, name, email, birthday, created_at, updated_at, address_id)
VALUES ('ab68ca39-e78f-4237-8cb2-11750ba4d8a8', 'Volnei', 'volnei@email.com', '1997-07-24', '2023-09-01', '2023-09-01', '2ac9e579-9f46-4b3c-8b5d-409c4b0bbd04');
