CREATE TABLE IF NOT EXISTS swissroute.users (

    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    flg_state CHAR(1) NOT NULL DEFAULT '1',
    ip_created VARCHAR(45) NOT NULL,
    ip_updated VARCHAR(45),
    user_created VARCHAR(100) NOT NULL,
    user_updated VARCHAR(100),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    ciudad_base VARCHAR(255),
    sign_up_date DATE NOT NULL DEFAULT CURRENT_DATE,
    role_id BIGINT NOT NULL,

    CONSTRAINT fk_users_role
    FOREIGN KEY (role_id)
    REFERENCES swissroute.roles(id)
    );