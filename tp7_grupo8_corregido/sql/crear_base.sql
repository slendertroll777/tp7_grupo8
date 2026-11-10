-- Ejecutar como administrador de MySQL 8. Cambie estas credenciales si lo requiere.
CREATE DATABASE IF NOT EXISTS persistencebd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'tp7'@'localhost' IDENTIFIED BY 'tp7_1234';
GRANT ALL PRIVILEGES ON persistencebd.* TO 'tp7'@'localhost';
