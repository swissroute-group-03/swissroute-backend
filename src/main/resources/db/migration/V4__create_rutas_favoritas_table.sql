CREATE TABLE IF NOT EXISTS swissroute.rutas_favoritas (

    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    flg_state VARCHAR(1) NOT NULL DEFAULT '1',
    ip_created VARCHAR(45) NOT NULL,
    ip_updated VARCHAR(45),
    user_created VARCHAR(100) NOT NULL,
    user_updated VARCHAR(100),
    nombre VARCHAR(255) NOT NULL,
    origen_id VARCHAR(50) NOT NULL,
    origen_nombre VARCHAR(255) NOT NULL,
    destino_id VARCHAR(50) NOT NULL,
    destino_nombre VARCHAR(255) NOT NULL,
    tipo_transporte VARCHAR(100),
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_rutas_favoritas_user
    FOREIGN KEY (user_id)
    REFERENCES swissroute.users(id)
    );

    CREATE UNIQUE INDEX IF NOT EXISTS uk_rutas_favoritas_user_nombre_activo
    ON swissroute.rutas_favoritas (user_id, LOWER(nombre))
    WHERE flg_state = '1';