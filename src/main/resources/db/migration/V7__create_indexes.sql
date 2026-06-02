CREATE INDEX IF NOT EXISTS idx_users_role_id
    ON swissroute.users(role_id);

CREATE INDEX IF NOT EXISTS idx_rutas_favoritas_user_id
    ON swissroute.rutas_favoritas(user_id);

CREATE INDEX IF NOT EXISTS idx_historial_busquedas_user_id
    ON swissroute.historial_busquedas(user_id);

CREATE INDEX IF NOT EXISTS idx_historial_busquedas_user_fecha
    ON swissroute.historial_busquedas(user_id, fecha_consulta);

CREATE INDEX IF NOT EXISTS idx_estaciones_favoritas_user_id
    ON swissroute.estaciones_favoritas(user_id);