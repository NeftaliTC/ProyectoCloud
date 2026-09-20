-- Datos de prueba para validar reportes de ventas e inventario.
-- MySQL. Ejecutar sobre la BD de KillaBeauty.
-- Los registros quedan marcados con REPTEST para poder reconocerlos.

SET @old_sql_safe_updates = @@SQL_SAFE_UPDATES;
SET SQL_SAFE_UPDATES = 0;

START TRANSACTION;

-- Limpieza de una corrida anterior del mismo script.
DELETE dp
FROM DetallePedido dp
INNER JOIN Pedido p ON dp.id_pedido = p.id_pedido
INNER JOIN Usuario u ON p.id_usuario = u.id_usuario
WHERE u.correo_electronico LIKE 'reptest.%@killabeauty.test';

DELETE p
FROM Pedido p
INNER JOIN Usuario u ON p.id_usuario = u.id_usuario
WHERE u.correo_electronico LIKE 'reptest.%@killabeauty.test';

DELETE FROM ImagenProducto
WHERE titulo LIKE 'REPTEST%';

DELETE FROM Producto
WHERE nombre LIKE 'REPTEST%';

DELETE FROM Subcategoria
WHERE descripcion LIKE 'REPTEST%';

DELETE FROM Categoria
WHERE descripcion LIKE 'REPTEST%';

DELETE FROM Marca
WHERE descripcion LIKE 'REPTEST%';

DELETE FROM Usuario
WHERE correo_electronico LIKE 'reptest.%@killabeauty.test';

-- Clientes para los pedidos.
INSERT INTO Usuario
    (nombre, apellido_paterno, apellido_materno, correo_electronico,
     fecha_nacimiento, genero, fecha_inscripcion, contrasena, telefono,
     activo, id_tipo_usuario, ultimo_acceso, dni)
VALUES
    ('Valeria', 'Rojas', 'Mendoza', 'reptest.valeria@killabeauty.test',
     '2001-04-12', 'Femenino', NOW(), 'demo123', '999111222',
     1, 2, NOW(), '70000001'),
    ('Camila', 'Torres', 'Vega', 'reptest.camila@killabeauty.test',
     '1999-08-20', 'Femenino', NOW(), 'demo123', '999333444',
     1, 2, NOW(), '70000002');

-- Catalogo minimo para que el reporte pueda unir Categoria, Subcategoria, Marca y Producto.
INSERT INTO Categoria (descripcion, activo)
VALUES
    ('REPTEST Maquillaje', 1),
    ('REPTEST Skincare', 1);

SET @cat_maquillaje = (SELECT id_categoria FROM Categoria WHERE descripcion = 'REPTEST Maquillaje' LIMIT 1);
SET @cat_skincare = (SELECT id_categoria FROM Categoria WHERE descripcion = 'REPTEST Skincare' LIMIT 1);

INSERT INTO Subcategoria (descripcion, activo, id_categoria)
VALUES
    ('REPTEST Labiales', 1, @cat_maquillaje),
    ('REPTEST Rostro', 1, @cat_maquillaje),
    ('REPTEST Cremas', 1, @cat_skincare);

SET @sub_labiales = (SELECT id_subcategoria FROM Subcategoria WHERE descripcion = 'REPTEST Labiales' LIMIT 1);
SET @sub_rostro = (SELECT id_subcategoria FROM Subcategoria WHERE descripcion = 'REPTEST Rostro' LIMIT 1);
SET @sub_cremas = (SELECT id_subcategoria FROM Subcategoria WHERE descripcion = 'REPTEST Cremas' LIMIT 1);

INSERT INTO Marca (descripcion, id_pais, activo)
VALUES
    ('REPTEST Killa Glow', 4, 1),
    ('REPTEST Seoul Care', 2, 1);

SET @marca_killa = (SELECT id_marca FROM Marca WHERE descripcion = 'REPTEST Killa Glow' LIMIT 1);
SET @marca_seoul = (SELECT id_marca FROM Marca WHERE descripcion = 'REPTEST Seoul Care' LIMIT 1);

INSERT INTO Producto
    (nombre, precio_base, stock, disponible, promocion, activo, id_marca, id_subcategoria)
VALUES
    ('REPTEST Labial Rosa Andina', 35.00, 80, 1, 0, 1, @marca_killa, @sub_labiales),
    ('REPTEST Base Liquida Natural', 72.00, 45, 1, 0, 1, @marca_killa, @sub_rostro),
    ('REPTEST Crema Hidratante Arroz', 58.00, 30, 1, 1, 1, @marca_seoul, @sub_cremas);

SET @prod_labial = (SELECT id_producto FROM Producto WHERE nombre = 'REPTEST Labial Rosa Andina' LIMIT 1);
SET @prod_base = (SELECT id_producto FROM Producto WHERE nombre = 'REPTEST Base Liquida Natural' LIMIT 1);
SET @prod_crema = (SELECT id_producto FROM Producto WHERE nombre = 'REPTEST Crema Hidratante Arroz' LIMIT 1);

INSERT INTO ImagenProducto (url, titulo, orden, principal, activo, id_producto)
VALUES
    ('https://example.com/reptest-labial.jpg', 'REPTEST Labial', 1, 1, 1, @prod_labial),
    ('https://example.com/reptest-base.jpg', 'REPTEST Base', 1, 1, 1, @prod_base),
    ('https://example.com/reptest-crema.jpg', 'REPTEST Crema', 1, 1, 1, @prod_crema);

SET @cli_valeria = (SELECT id_usuario FROM Usuario WHERE correo_electronico = 'reptest.valeria@killabeauty.test' LIMIT 1);
SET @cli_camila = (SELECT id_usuario FROM Usuario WHERE correo_electronico = 'reptest.camila@killabeauty.test' LIMIT 1);

-- Pedidos dentro de junio 2026 para el reporte de ventas.
-- id_estado_pedido = 5 corresponde a ENTREGADO en el enum EstadoPedido.
INSERT INTO Pedido
    (fecha_pedido, subtotal, igv, total, id_usuario, id_direccion, id_cupon, id_estado_pedido)
VALUES
    ('2026-06-05 10:15:00', 130.00, 23.40, 153.40, @cli_valeria, NULL, NULL, 5),
    ('2026-06-12 16:40:00', 174.00, 31.32, 205.32, @cli_camila, NULL, NULL, 5);

SET @ped_1 = (
    SELECT id_pedido FROM Pedido
    WHERE id_usuario = @cli_valeria AND fecha_pedido = '2026-06-05 10:15:00'
    LIMIT 1
);
SET @ped_2 = (
    SELECT id_pedido FROM Pedido
    WHERE id_usuario = @cli_camila AND fecha_pedido = '2026-06-12 16:40:00'
    LIMIT 1
);

INSERT INTO DetallePedido
    (cantidad, precio_unitario_aplicado, subtotal, id_pedido, id_producto)
VALUES
    (2, 35.00, 70.00, @ped_1, @prod_labial),
    (1, 60.00, 60.00, @ped_1, @prod_crema),
    (1, 72.00, 72.00, @ped_2, @prod_base),
    (3, 34.00, 102.00, @ped_2, @prod_labial);

COMMIT;

-- Verificacion rapida: debe devolver filas para el reporte de ventas.
SELECT p.id_pedido, p.fecha_pedido, u.nombre AS cliente, c.descripcion AS categoria,
       pr.nombre AS producto, dp.cantidad, dp.precio_unitario_aplicado, p.total
FROM Pedido p
INNER JOIN Usuario u ON p.id_usuario = u.id_usuario
INNER JOIN DetallePedido dp ON p.id_pedido = dp.id_pedido
INNER JOIN Producto pr ON dp.id_producto = pr.id_producto
INNER JOIN Subcategoria s ON pr.id_subcategoria = s.id_subcategoria
INNER JOIN Categoria c ON s.id_categoria = c.id_categoria
WHERE u.correo_electronico LIKE 'reptest.%@killabeauty.test'
ORDER BY p.fecha_pedido, p.id_pedido;

SET SQL_SAFE_UPDATES = @old_sql_safe_updates;
