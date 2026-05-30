ALTER TABLE swissroute.roles ALTER COLUMN flg_state TYPE VARCHAR(1);
ALTER TABLE swissroute.users ALTER COLUMN flg_state TYPE VARCHAR(1);
ALTER TABLE swissroute.rutas_favoritas ALTER COLUMN flg_state TYPE VARCHAR(1);
ALTER TABLE swissroute.historial_busquedas ALTER COLUMN flg_state TYPE VARCHAR(1);
ALTER TABLE swissroute.estaciones_favoritas ALTER COLUMN flg_state TYPE VARCHAR(1);

ALTER TABLE swissroute.rutas_favoritas ADD COLUMN IF NOT EXISTS origen_id VARCHAR(50);
ALTER TABLE swissroute.rutas_favoritas ADD COLUMN IF NOT EXISTS origen_nombre VARCHAR(255);
ALTER TABLE swissroute.rutas_favoritas ADD COLUMN IF NOT EXISTS destino_id VARCHAR(50);
ALTER TABLE swissroute.rutas_favoritas ADD COLUMN IF NOT EXISTS destino_nombre VARCHAR(255);

UPDATE swissroute.rutas_favoritas
SET origen_nombre = origen,
    destino_nombre = destino,
    origen_id = '',
    destino_id = ''
WHERE origen IS NOT NULL AND origen_id = '';

ALTER TABLE swissroute.rutas_favoritas ALTER COLUMN origen_id SET NOT NULL;
ALTER TABLE swissroute.rutas_favoritas ALTER COLUMN origen_nombre SET NOT NULL;
ALTER TABLE swissroute.rutas_favoritas ALTER COLUMN destino_id SET NOT NULL;
ALTER TABLE swissroute.rutas_favoritas ALTER COLUMN destino_nombre SET NOT NULL;

ALTER TABLE swissroute.rutas_favoritas DROP COLUMN IF EXISTS origen;
ALTER TABLE swissroute.rutas_favoritas DROP COLUMN IF EXISTS destino;

ALTER TABLE swissroute.rutas_favoritas ALTER COLUMN tipo_transporte SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uk_rutas_favoritas_user_nombre_activo
ON swissroute.rutas_favoritas (user_id, LOWER(nombre))
WHERE flg_state = '1';
