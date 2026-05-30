CREATE TABLE IF NOT EXISTS swissroute.estaciones_favoritas (

    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    flg_state VARCHAR(1) NOT NULL DEFAULT '1',
    ip_created VARCHAR(45) NOT NULL,
    ip_updated VARCHAR(45),
    user_created VARCHAR(100) NOT NULL,
    user_updated VARCHAR(100),
    estacion_id_externo VARCHAR(100) NOT NULL,
    nombre_estacion VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_estaciones_favoritas_user
    FOREIGN KEY (user_id)
    REFERENCES swissroute.users(id)
    );