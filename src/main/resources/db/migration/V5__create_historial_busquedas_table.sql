CREATE TABLE IF NOT EXISTS swissroute.historial_busquedas (


    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    flg_state CHAR(1) NOT NULL DEFAULT '1',
    ip_created VARCHAR(45) NOT NULL,
    ip_updated VARCHAR(45),
    user_created VARCHAR(100) NOT NULL,
    user_updated VARCHAR(100),
    origen VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    fecha_consulta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    num_resultados INTEGER NOT NULL DEFAULT 0,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_historial_busquedas_user
    FOREIGN KEY (user_id)
    REFERENCES swissroute.users(id)
    );