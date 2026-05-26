INSERT INTO swissroute.roles (id, created_at, updated_at, flg_state, ip_created, ip_updated, user_created, user_updated, description, name) VALUES (1, CURRENT_TIMESTAMP, NULL, '1', '127.0.0.1', NULL, 'system', NULL, 'Perfil de Administrador', 'ADMIN');

INSERT INTO swissroute.roles (id, created_at, updated_at, flg_state, ip_created, ip_updated, user_created, user_updated, description, name) VALUES (2, CURRENT_TIMESTAMP, NULL, '1', '127.0.0.1', NULL, 'system', NULL, 'Perfil de Visitante', 'VISITANTE');

-- For H2 we don't use setval. If using IDENTITY, we might need to adjust the next value.
-- But for these tests, we can just avoid inserting users in import.sql to avoid conflicts.
