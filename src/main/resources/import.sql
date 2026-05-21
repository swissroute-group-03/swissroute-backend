INSERT INTO swissroute.roles (id, created_at, updated_at, flg_state, ip_created, ip_updated, user_created, user_updated, description, name) VALUES (1, CURRENT_TIMESTAMP, NULL, '1', '127.0.0.1', NULL, 'system', NULL, 'Perfil de Administrador', 'ADMIN');

INSERT INTO swissroute.roles (id, created_at, updated_at, flg_state, ip_created, ip_updated, user_created, user_updated, description, name) VALUES (2, CURRENT_TIMESTAMP, NULL, '1', '127.0.0.1', NULL, 'system', NULL, 'Perfil de Visitante', 'VISITANTE');

INSERT INTO swissroute.users (id, created_at, updated_at, flg_state, ip_created, ip_updated, user_created, user_updated, email, name, password, sign_up_date, ciudad_base, role_id) VALUES (1, CURRENT_TIMESTAMP, NULL, '1', '127.0.0.1', NULL, 'system', NULL, 'alexandra@gmail.com', 'Alexandra', '$2a$10$sxcKLKrXyfzKzB4Q5JKIgugZQO7acU3wIMuN1on2obqwzMqrgjWwy', CURRENT_DATE, 'Zurich', 1);

INSERT INTO swissroute.users (id, created_at, updated_at, flg_state, ip_created, ip_updated, user_created, user_updated, email, name, password, sign_up_date, ciudad_base, role_id) VALUES (2, CURRENT_TIMESTAMP, NULL, '1', '127.0.0.1', NULL, 'system', NULL, 'brenda@gmail.com', 'Brenda', '$2a$10$ELd8A4Xwsryxt5NYzdoVK.PD8QsStJ0p.9jIEehc8Rpcj8w6xq8aW', CURRENT_DATE, 'Zurich', 2);

SELECT setval('swissroute.roles_id_seq', (SELECT MAX(id) FROM swissroute.roles));

SELECT setval('swissroute.users_id_seq', (SELECT MAX(id) FROM swissroute.users));