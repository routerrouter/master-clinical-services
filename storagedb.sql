-- --------------------------------------------------------
-- Anfitrião:                    127.0.0.1
-- Versão do servidor:           PostgreSQL 12.14 (Ubuntu 12.14-0ubuntu0.20.04.1) on x86_64-pc-linux-gnu, compiled by gcc (Ubuntu 9.4.0-1ubuntu1~20.04.1) 9.4.0, 64-bit
-- SO do servidor:               
-- HeidiSQL Versão:              12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES  */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- A despejar estrutura para tabela public.tb_categories
DROP TABLE IF EXISTS "tb_categories";
CREATE TABLE IF NOT EXISTS "tb_categories" (
	"category_id" UUID NOT NULL,
	"last_update_at" TIMESTAMP NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"user_group" UUID NOT NULL,
	PRIMARY KEY ("category_id")
);

-- A despejar dados para tabela public.tb_categories: 0 rows
DELETE FROM "tb_categories";
/*!40000 ALTER TABLE "tb_categories" DISABLE KEYS */;
INSERT INTO "tb_categories" ("category_id", "last_update_at", "name", "registered_at", "user_group") VALUES
	('833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '2023-05-30 13:51:51.375407', 'COMPRIMIDO', '2023-05-30 13:51:51.375359', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('202d5146-4363-4d41-be79-c86824d05245', '2023-05-30 13:53:09.595222', 'XAROPE', '2023-05-30 13:53:09.595192', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('e2a36ad5-a06e-4a3e-bc36-9d6564352e1a', '2023-05-30 13:53:20.262937', 'PASTILHAS', '2023-05-30 13:53:20.262919', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('07cc6419-eccc-4600-a454-366183a6ff9b', '2023-05-30 13:53:28.570712', 'SAQUETAS', '2023-05-30 13:53:28.570686', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('d41948fa-38f0-4042-9946-8887b410cc7f', '2023-05-30 13:53:37.299673', 'PASTA', '2023-05-30 13:53:37.29965', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('202b160d-fc4c-43c0-a23a-17a0d046c0ed', '2023-05-30 13:53:53.34806', 'ANTIBACTERIANOS', '2023-05-30 13:53:53.348032', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('dd4b6371-755d-4f9c-abdb-ac1848fbbadc', '2023-05-30 13:54:01.619974', 'BLISTER', '2023-05-30 13:54:01.619946', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('3337b933-8883-43e9-af0b-049fa293b912', '2023-05-30 13:54:13.686377', 'SMPOLAS', '2023-05-30 13:54:13.686346', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('0f12b142-23e9-4efa-b179-e19a5b274605', '2023-05-30 13:54:28.443742', 'COLIRIO', '2023-05-30 13:54:28.443725', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('c560c4ca-d0c2-42bc-bc93-a305bdabd723', '2023-05-30 13:54:42.151413', 'GEL OFTALMICO', '2023-05-30 13:54:42.151397', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('78267670-302a-4c9f-bf37-728b57ddfd49', '2023-05-30 13:54:57.077113', 'INALANTES', '2023-05-30 13:54:57.077059', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('bb61e136-6aef-468e-a325-eb71de105334', '2023-05-30 13:55:07.354825', 'MATERIAS CIRURGICOS', '2023-05-30 13:55:07.354798', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('10f09c90-add8-4ad6-b9f2-875e07e5e634', '2023-05-30 13:55:19.408997', 'LENTES GRADUADAS/ LENTES INTRAOCULAR', '2023-05-30 13:55:19.408971', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('c87183af-54e6-4c2b-910a-66b037e22c15', '2023-05-30 13:55:30.711825', 'POMADA OFTALMICA', '2023-05-30 13:55:30.711742', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('8b00bebb-1529-4dc0-80a3-78b8e7376c10', '2023-05-30 13:55:40.770519', 'SUPOSITORIOS', '2023-05-30 13:55:40.770499', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('84d9864c-ae1a-4618-bdf0-d9fa0da803a4', '2023-05-30 13:55:50.845996', 'REAGENTE LAB', '2023-05-30 13:55:50.845963', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('734d8af5-6c76-4d7b-98ff-79dd3a99ed01', '2023-05-30 13:56:02.286357', 'INJ. GRANDE VOLUME/SOROS', '2023-05-30 13:56:02.286337', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('771caf65-6094-4328-bff4-0b8b1c6067d3', '2023-05-30 13:56:17.493262', 'IAPARELHOS PARA CONSULTAS', '2023-05-30 13:56:17.493232', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('2f5b6d2b-2deb-4198-83a6-097a322d2816', '2023-05-30 13:56:32.998851', 'OCULOS DE PROTEÇÃO', '2023-05-30 13:56:32.998828', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('6cfe3688-5b8c-4f42-a91f-ff00a93ed303', '2023-05-30 13:56:45.483363', 'TERMOMETRO DE TEMPERATURA', '2023-05-30 13:56:45.483338', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('197b3bf7-0305-4883-ade0-6efe0e1bbc2c', '2023-05-30 13:57:20.366791', 'TESTES', '2023-05-30 13:57:20.366757', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('8212ec1b-428f-40e3-926d-5f69b024b87c', '2023-05-30 13:57:30.300039', 'M/ LABORATORIO', '2023-05-30 13:57:30.300007', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('e2dad8cd-8e8e-4c44-9461-778d04191863', '2023-05-30 13:57:47.226508', 'FIOS DE SUTURAS', '2023-05-30 13:57:47.22648', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('87c8b876-5739-48c2-9911-382b5e5dd3a6', '2023-05-30 13:58:00.214243', 'POMADA CUTANEA', '2023-05-30 13:58:00.214213', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('e6ffb362-83ba-45a8-a770-92f107c3396e', '2023-05-30 13:58:40.919536', 'MAT. BIO SEGURANÇA', '2023-05-30 13:58:40.919516', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('97d2e5d4-8a98-4af9-9f4f-48de09d2cdcb', '2023-05-30 13:58:58.570506', 'MATERIAL GASTAVEL/CONSUMIVEL', '2023-05-30 13:58:58.570479', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('1bc2f69a-c9de-4872-958a-0640be40bf64', '2023-05-30 13:59:13.28142', 'ANTISSEPTICO/DESINFECTANTE', '2023-05-30 13:59:13.281404', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('9f4bed74-4664-4d1e-b312-57b895b68473', '2023-05-30 13:59:20.979055', 'CREMES', '2023-05-30 13:59:20.979023', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('9355c4bc-f970-4c3e-8387-cda5e29a7928', '2023-05-30 13:59:29.854412', 'SOL. EXTERNA', '2023-05-30 13:59:29.854386', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('98f3d64c-434c-41b5-bbe4-2c48b6ab6d9e', '2023-05-30 13:59:44.712311', 'SORO ORAL', '2023-05-30 13:59:44.712292', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('adb53571-1096-4daf-ae26-58b7b8d3ae3d', '2023-05-30 13:59:55.799279', 'SOLUÇAO INALATORIA', '2023-05-30 13:59:55.79926', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('7741fb8e-9302-416f-b117-d664b1e91f9b', '2023-05-30 14:00:06.112822', 'CAPSULAS', '2023-05-30 14:00:06.11279', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('51e778fb-0ffa-4a31-b7fe-338a9a3cec2e', '2023-05-30 14:00:16.573273', 'FIO DE SUTURA', '2023-05-30 14:00:16.573247', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('0c601ec3-1aae-4d7d-bfe2-d1e345aa7ea9', '2023-05-30 14:00:32.49138', 'GOTAS', '2023-05-30 14:00:32.491341', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('4898a417-5d2e-4eb5-9897-b4f847a136ad', '2023-05-30 14:01:15.907024', 'MAT. INFORMÁTICOS', '2023-05-30 14:01:15.906996', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('6d2c51f7-58a3-4a89-919c-650159e6b2e7', '2023-05-30 14:01:28.946062', 'MAT. ESCRITORIO', '2023-05-30 14:01:28.946043', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('80fc4a39-3e5c-4450-a895-3c70ec228c4a', '2023-05-30 14:01:42.077406', 'MAT. LIMPEZA', '2023-05-30 14:01:42.077366', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('bb2df729-cf68-4ced-8308-a46a2145d165', '2023-05-30 14:01:53.907655', 'CATEGORIA', '2023-05-30 14:01:53.907625', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002');
/*!40000 ALTER TABLE "tb_categories" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_company
DROP TABLE IF EXISTS "tb_company";
CREATE TABLE IF NOT EXISTS "tb_company" (
	"company_id" UUID NOT NULL,
	"address" VARCHAR(255) NOT NULL,
	"creation_date" TIMESTAMP NOT NULL,
	"last_update_date" TIMESTAMP NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"nif" VARCHAR(255) NOT NULL,
	"phone_number" VARCHAR(255) NOT NULL,
	"responsible" VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY ("company_id")
);

-- A despejar dados para tabela public.tb_company: 0 rows
DELETE FROM "tb_company";
/*!40000 ALTER TABLE "tb_company" DISABLE KEYS */;
INSERT INTO "tb_company" ("company_id", "address", "creation_date", "last_update_date", "name", "nif", "phone_number", "responsible") VALUES
	('25789b23-5bc6-41e5-84f2-64497e81f42e', 'Av Brasil', '2023-06-01 22:58:03', '2023-06-01 22:56:22.472507', 'Hospital Americo Boa Vida (HAB)', '500000000', '930000000', 'Drº Francisco Goveia dos Santos');
/*!40000 ALTER TABLE "tb_company" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_company_image
DROP TABLE IF EXISTS "tb_company_image";
CREATE TABLE IF NOT EXISTS "tb_company_image" (
	"id" UUID NOT NULL,
	"created_date" TIMESTAMP NOT NULL,
	"modified_date" TIMESTAMP NULL DEFAULT NULL,
	"base64" INTEGER NULL DEFAULT NULL,
	"content_type" VARCHAR(255) NULL DEFAULT NULL,
	"company_company_id" UUID NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkkyr3jc9cs41q2nqlsg19s43pj" FOREIGN KEY ("company_company_id") REFERENCES "tb_company" ("company_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- A despejar dados para tabela public.tb_company_image: 0 rows
DELETE FROM "tb_company_image";
/*!40000 ALTER TABLE "tb_company_image" DISABLE KEYS */;
INSERT INTO "tb_company_image" ("id", "created_date", "modified_date", "base64", "content_type", "company_company_id") VALUES
	('8edfd44c-b196-4f82-9e0b-a4642a1239b6', '2023-06-02 12:38:08.345', '2023-06-02 12:52:25.493', 41149, 'image/png', '25789b23-5bc6-41e5-84f2-64497e81f42e');
/*!40000 ALTER TABLE "tb_company_image" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_entities
DROP TABLE IF EXISTS "tb_entities";
CREATE TABLE IF NOT EXISTS "tb_entities" (
	"entity_id" UUID NOT NULL,
	"address" VARCHAR(255) NULL DEFAULT NULL,
	"creation_date" TIMESTAMP NOT NULL,
	"email" VARCHAR(255) NULL DEFAULT NULL,
	"enabled" BOOLEAN NOT NULL,
	"entity_type" VARCHAR(255) NOT NULL,
	"last_update_date" TIMESTAMP NULL DEFAULT NULL,
	"name" VARCHAR(255) NOT NULL,
	"nif" VARCHAR(255) NOT NULL,
	"phone_number" VARCHAR(255) NULL DEFAULT NULL,
	"responsible" VARCHAR(255) NOT NULL,
	"user_group" UUID NOT NULL,
	PRIMARY KEY ("entity_id")
);

-- A despejar dados para tabela public.tb_entities: 0 rows
DELETE FROM "tb_entities";
/*!40000 ALTER TABLE "tb_entities" DISABLE KEYS */;
INSERT INTO "tb_entities" ("entity_id", "address", "creation_date", "email", "enabled", "entity_type", "last_update_date", "name", "nif", "phone_number", "responsible", "user_group") VALUES
	('17264f1a-eff7-40e6-b870-4c6d63c17858', 'Primeiro andar ala 01', '2023-05-26 14:44:58.247802', '', 'true', 'UNIT', NULL, 'Bloco Operatório 01', '00000000000', '999999999', 'DEFAULT', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('df970c99-d373-4eb0-a42a-386290e2ffb6', 'Primeiro andar ala 01', '2023-05-26 14:43:55.753166', '', 'true', 'UNIT', '2023-05-26 14:53:04.552627', 'Bloco Operatório 01', '00000000000', '999999999', 'Drº Eugenia Vandunem', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('adb5720c-c1f0-43da-b232-701916589737', 'Av. 21 de Maio', '2023-05-26 16:28:04.635529', 'geral@socompser.com', 'true', 'SUPPLIER', NULL, 'Socompser', '5000000000', '922000022', 'Gildo Francisco', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('f127bc6d-8264-4dd0-a24e-ed4400343159', 'Segundo andar porta nº 234', '2023-05-30 16:30:14.096303', 'departamento@fincancas.com', 'true', 'UNIT', NULL, 'Departamento de Finanças', '000000000000', '920000000', 'José Severino', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002');
/*!40000 ALTER TABLE "tb_entities" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_groups
DROP TABLE IF EXISTS "tb_groups";
CREATE TABLE IF NOT EXISTS "tb_groups" (
	"group_id" UUID NOT NULL,
	"last_update_at" TIMESTAMP NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"user_group" UUID NULL DEFAULT NULL,
	PRIMARY KEY ("group_id")
);

-- A despejar dados para tabela public.tb_groups: 0 rows
DELETE FROM "tb_groups";
/*!40000 ALTER TABLE "tb_groups" DISABLE KEYS */;
INSERT INTO "tb_groups" ("group_id", "last_update_at", "name", "registered_at", "user_group") VALUES
	('7250e84d-92ac-46ba-861b-2f8713f63422', '2023-05-30 09:20:14.560093', 'CONS. ESCRITORIO', '2023-05-30 09:20:14.560036', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('56cc9be9-daef-4ebf-ab0a-00c9f2097aa6', '2023-05-30 09:20:28.433262', 'DIVERSOS', '2023-05-30 09:20:28.433195', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('671b9c17-167a-470d-b53e-dd817feb9199', '2023-05-30 09:20:37.551964', 'EQUIPAMENTOS', '2023-05-30 09:20:37.551941', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('5665e6bc-c972-491d-95b6-0fe6d56fc082', '2023-05-30 09:44:00.04745', 'MAT. FARMACOLÓGICO', '2023-05-30 09:44:00.047417', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('b89426c4-f5b5-4e72-b100-7e3f4ab2405c', '2023-05-30 09:44:14.182356', 'MAT. GASTÁVEIS', '2023-05-30 09:44:14.182328', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('56431d46-470b-409f-aa11-f18252aa802a', '2023-05-30 09:44:26.164837', 'OUTROS', '2023-05-30 09:44:26.164806', '106c496c-a3e8-41de-bee5-ca26ee9db491');
/*!40000 ALTER TABLE "tb_groups" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_locations
DROP TABLE IF EXISTS "tb_locations";
CREATE TABLE IF NOT EXISTS "tb_locations" (
	"location_id" UUID NOT NULL,
	"description" VARCHAR(255) NOT NULL,
	"enabeld" BOOLEAN NOT NULL,
	"last_update_at" TIMESTAMP NULL DEFAULT NULL,
	"partition" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"shelf" VARCHAR(255) NOT NULL,
	"storage_storage_id" UUID NOT NULL,
	PRIMARY KEY ("location_id"),
	CONSTRAINT "fktb15hg9qbrrfbpy43955fdciu" FOREIGN KEY ("storage_storage_id") REFERENCES "tb_storages" ("storage_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- A despejar dados para tabela public.tb_locations: 0 rows
DELETE FROM "tb_locations";
/*!40000 ALTER TABLE "tb_locations" DISABLE KEYS */;
INSERT INTO "tb_locations" ("location_id", "description", "enabeld", "last_update_at", "partition", "registered_at", "shelf", "storage_storage_id") VALUES
	('4dd53c70-a515-456c-b16e-0f383885e8e1', 'ARMAZEM1-Prateleira 001-Divisoria 001', 'true', NULL, 'Divisoria 001', '2023-05-30 14:23:16.818008', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('13a53552-61fa-4d1a-a77a-0973b1a12038', 'ARMAZEM1-Prateleira 001-Divisoria 002', 'true', NULL, 'Divisoria 002', '2023-05-30 14:23:37.845939', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('12e42846-4e0f-4c96-9d37-bc3aec28c6fe', 'ARMAZEM1-Prateleira 001-Divisoria 003', 'true', NULL, 'Divisoria 003', '2023-05-30 14:23:43.180187', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('baae29e2-1f12-4598-abe9-afdde3573c32', 'ARMAZEM1-Prateleira 001-Divisoria 004', 'true', NULL, 'Divisoria 004', '2023-05-30 14:23:47.632733', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('d5d5689e-85d0-4d02-bbc8-710dff18c65d', 'ARMAZEM1-Prateleira 001-Divisoria 005', 'true', NULL, 'Divisoria 005', '2023-05-30 14:23:53.128656', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('e3bb8485-d651-4de2-9c8a-4af7ba2981e2', 'ARMAZEM1-Prateleira 001-Divisoria 006', 'true', NULL, 'Divisoria 006', '2023-05-30 14:23:57.939701', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('e4de5c67-bad9-4920-989c-93a3dfb5abc2', 'ARMAZEM1-Prateleira 001-Divisoria 007', 'true', NULL, 'Divisoria 007', '2023-05-30 14:24:02.392259', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('6969d16f-b3f7-4009-998d-849c34d8663c', 'ARMAZEM1-Prateleira 001-Divisoria 008', 'true', NULL, 'Divisoria 008', '2023-05-30 14:24:09.031596', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('fbb005ac-86fe-4bae-90fb-a1c578724220', 'ARMAZEM1-Prateleira 001-Divisoria 009', 'true', NULL, 'Divisoria 009', '2023-05-30 14:24:14.150125', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('9363f2b0-d5de-41a8-8fa9-dffead082e23', 'ARMAZEM1-Prateleira 001-Divisoria 010', 'true', NULL, 'Divisoria 010', '2023-05-30 14:24:19.15238', 'Prateleira 001', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc');
/*!40000 ALTER TABLE "tb_locations" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_movements
DROP TABLE IF EXISTS "tb_movements";
CREATE TABLE IF NOT EXISTS "tb_movements" (
	"movement_id" UUID NOT NULL,
	"description" VARCHAR(255) NULL DEFAULT NULL,
	"devolution_type" VARCHAR(255) NULL DEFAULT NULL,
	"document_number" VARCHAR(255) NULL DEFAULT NULL,
	"movement_date" DATE NOT NULL,
	"movement_status" VARCHAR(255) NOT NULL,
	"movement_type" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"total" NUMERIC(19,2) NOT NULL,
	"user_id" UUID NOT NULL,
	"entity_entity_id" UUID NOT NULL,
	"file_name" VARCHAR(255) NULL DEFAULT NULL,
	PRIMARY KEY ("movement_id"),
	CONSTRAINT "fk6jd51mqtc69m41kqu4ffj3rqb" FOREIGN KEY ("entity_entity_id") REFERENCES "tb_entities" ("entity_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- A despejar dados para tabela public.tb_movements: 0 rows
DELETE FROM "tb_movements";
/*!40000 ALTER TABLE "tb_movements" DISABLE KEYS */;
INSERT INTO "tb_movements" ("movement_id", "description", "devolution_type", "document_number", "movement_date", "movement_status", "movement_type", "registered_at", "total", "user_id", "entity_entity_id", "file_name") VALUES
	('df3d25a8-4fe0-4afc-b74b-5b9388ebdb27', 'Entrada de Equipamentos para reforma do departamento de Finanças', NULL, 'FT 003/23', '2023-05-30', 'FINISHED', 'BUY', '2023-05-30 15:40:25.532798', 2250000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'adb5720c-c1f0-43da-b232-701916589737', NULL),
	('57275301-f53f-43bf-801e-c0d4cfd007a7', 'Entrada de Equipamentos para reforma do departamento de Finanças', NULL, 'FT 004/23', '2023-05-30', 'FINISHED', 'BUY', '2023-05-30 15:44:47.670989', 5250000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'adb5720c-c1f0-43da-b232-701916589737', NULL),
	('e9931d93-a342-4359-8472-65c41d47eda5', 'Entrada de Equipamentos para reforma do departamento de Finanças', NULL, 'FT 015/23', '2023-05-30', 'FINISHED', 'DONATION', '2023-05-30 15:46:45.116438', 1875000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'adb5720c-c1f0-43da-b232-701916589737', NULL),
	('41c6e22e-4448-4012-985c-b36a36d0f86b', 'Entrada de Equipamentos para reforma do departamento de Finanças', NULL, 'FT 016/23', '2023-05-30', 'FINISHED', 'DONATION', '2023-05-30 15:50:21.250149', 3300000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'adb5720c-c1f0-43da-b232-701916589737', NULL),
	('eadcb6e9-f185-45fc-b367-f0498e6ebb95', 'Entrada de Equipamentos para reforma do departamento de Finanças', NULL, 'FT 016/23', '2023-05-30', 'FINISHED', 'DONATION', '2023-05-30 16:01:27.931819', 3300000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'adb5720c-c1f0-43da-b232-701916589737', NULL),
	('431cc109-4da6-447a-9df2-c801b774b2ed', 'Entrada de Equipamentos para reforma do departamento de Finanças', NULL, 'FT 016/23', '2023-05-30', 'FINISHED', 'DONATION', '2023-05-30 16:02:21.87205', 3300000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'adb5720c-c1f0-43da-b232-701916589737', NULL),
	('e6ab77ca-9f14-43f6-8db6-36fa58930d21', 'Saida de produto para paciente no ambulatório', NULL, 'Saida 00/22', '2023-05-30', 'FINISHED', 'PATIENT_OUTPUT', '2023-05-30 16:31:48.520767', 225000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', 'f127bc6d-8264-4dd0-a24e-ed4400343159', NULL),
	('cc0ceac9-5697-47ad-97ff-9dc4fe824b66', 'Saida de produto para paciente no ambulatório', NULL, 'Saida 00/22', '2023-05-30', 'FINISHED', 'ORDER', '2023-05-31 22:25:39.498504', 3300000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('b12e502d-7ea3-4680-939d-07af65a8edcf', 'Pedido de equipamentos para o departamento financeiro', NULL, 'Saida 00/22', '2023-05-30', 'FINISHED', 'ORDER', '2023-06-01 08:58:39.663847', 3300000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('7a84bc47-9b22-49fe-9580-6636a607903d', 'Pedido de equipamentos para o departamento financeiro', NULL, 'Saida 00/22', '2023-05-30', 'FINISHED', 'ORDER', '2023-06-01 09:01:09.159445', 375000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('efc90ca5-a7a4-4dd6-96b0-057655d3e325', 'Pedido de equipamentos para o departamento financeiro', NULL, 'Saida 00/22', '2023-05-30', 'FINISHED', 'ORDER', '2023-06-01 09:02:51.047782', 375000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('419cc23f-4a0e-4d4c-a411-913828a6d477', 'Pedido de equipamentos para o departamento financeiro', NULL, 'Saida 00/22', '2023-05-30', 'PENDING', 'ORDER', '2023-06-01 09:23:05.321255', 375000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('e20af8b8-2233-4899-bc6a-4a5216be1377', 'Saida de produto para paciente no ambulatório', NULL, 'Saida 00/22', '2023-05-30', 'PENDING', 'REQUEST', '2023-06-02 14:11:49.740356', 75000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('08b686ce-8d2f-4cbd-a058-893981b476c7', 'Saida de produto para paciente no ambulatório', NULL, 'Saida 00/22', '2023-05-30', 'PENDING', 'REQUEST', '2023-06-02 14:08:02.487642', 750000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('ba3abc87-67c3-4a13-b4ab-ce4818bbe2b5', 'Saida de produto para paciente no ambulatório', NULL, 'Saida 00/22', '2023-05-30', 'PENDING', 'REQUEST', '2023-06-02 14:10:34.091388', 150000.00, '1bad6860-0b45-4eca-a070-95c0c7531c23', '17264f1a-eff7-40e6-b870-4c6d63c17858', NULL),
	('e6bf86c8-aef5-4bc6-88be-a3033d8bdb87', 'Compra de medicamentos', NULL, 'CP001/23', '2023-06-02', 'FINISHED', 'BUY', '2023-06-02 16:12:09.346504', 4169450.00, '4efe97e1-5d9d-43ea-8cf3-e74b6970f2ae', 'adb5720c-c1f0-43da-b232-701916589737', NULL);
/*!40000 ALTER TABLE "tb_movements" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_movement_products
DROP TABLE IF EXISTS "tb_movement_products";
CREATE TABLE IF NOT EXISTS "tb_movement_products" (
	"id" UUID NOT NULL,
	"acquisition_date" DATE NULL DEFAULT NULL,
	"barcode" VARCHAR(255) NULL DEFAULT NULL,
	"cust" NUMERIC(19,2) NULL DEFAULT NULL,
	"last_update_at" TIMESTAMP NULL DEFAULT NULL,
	"lote" VARCHAR(255) NOT NULL,
	"model" VARCHAR(255) NULL DEFAULT NULL,
	"quantity" BIGINT NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"total_value" NUMERIC(19,2) NULL DEFAULT NULL,
	"unit_type" VARCHAR(255) NOT NULL,
	"location_location_id" UUID NOT NULL,
	"movement_movement_id" UUID NOT NULL,
	"product_id" UUID NOT NULL,
	"expiration_date" DATE NULL DEFAULT NULL,
	"lifespan" BIGINT NULL DEFAULT NULL,
	"manufacture_date" DATE NULL DEFAULT NULL,
	"serial_number" VARCHAR(255) NULL DEFAULT NULL,
	"destine_transfer" UUID NULL DEFAULT NULL,
	"origin_transfer" UUID NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fk39hp5d35rejictd5c2ffseys4" FOREIGN KEY ("movement_movement_id") REFERENCES "tb_movements" ("movement_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkgbblappdwa89xeesywukjwg8b" FOREIGN KEY ("product_id") REFERENCES "tb_products" ("product_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkj7mgkquvg60yfsd2wavg39x1w" FOREIGN KEY ("location_location_id") REFERENCES "tb_locations" ("location_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- A despejar dados para tabela public.tb_movement_products: 0 rows
DELETE FROM "tb_movement_products";
/*!40000 ALTER TABLE "tb_movement_products" DISABLE KEYS */;
INSERT INTO "tb_movement_products" ("id", "acquisition_date", "barcode", "cust", "last_update_at", "lote", "model", "quantity", "registered_at", "total_value", "unit_type", "location_location_id", "movement_movement_id", "product_id", "expiration_date", "lifespan", "manufacture_date", "serial_number", "destine_transfer", "origin_transfer") VALUES
	('03df7922-86d9-4f77-a170-7a88dd1bb5b4', '2023-05-29', '', 75000.00, '2023-05-30 15:40:20.347737', '', 'WINTEC 2320', 30, '2023-05-30 15:40:20.347691', 2250000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'df3d25a8-4fe0-4afc-b74b-5b9388ebdb27', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, NULL, NULL, NULL),
	('482a8ce0-845c-4745-885c-e74e2eaf0c5c', '2023-05-29', '', 75000.00, '2023-05-30 15:44:47.664437', '', 'WINTEC 2320', 70, '2023-05-30 15:44:47.664405', 5250000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '57275301-f53f-43bf-801e-c0d4cfd007a7', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, NULL, NULL, NULL),
	('704b8b30-930e-4711-bfc7-a26164ba447e', '2023-05-29', '', 75000.00, '2023-05-30 15:46:45.112787', '', 'WINTEC 2320', 25, '2023-05-30 15:46:45.112769', 1875000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e9931d93-a342-4359-8472-65c41d47eda5', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, NULL, NULL, NULL),
	('47699b91-458f-4fc4-a54e-b30e9d316c7b', '2023-05-29', '', 75000.00, '2023-05-30 15:50:21.200526', '', 'WINTEC 2320', 44, '2023-05-30 15:50:21.200492', 3300000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '41c6e22e-4448-4012-985c-b36a36d0f86b', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, NULL, NULL, NULL),
	('6dd22b69-dae6-4e15-902c-6db064c963d5', '2023-05-29', '', 75000.00, '2023-05-30 16:01:27.863528', '', 'WINTEC 2320', 44, '2023-05-30 16:01:27.863488', 3300000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'eadcb6e9-f185-45fc-b367-f0498e6ebb95', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '', NULL, NULL),
	('f7c0f7a6-0572-4307-bcfd-44c41be4ef73', '2023-05-29', '', 75000.00, '2023-05-30 16:02:21.866374', '', 'WINTEC 2320', 44, '2023-05-30 16:02:21.866357', 3300000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '431cc109-4da6-447a-9df2-c801b774b2ed', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('d421a1a8-11c0-48ee-be9b-35a305da09c7', '2023-05-29', '', 75000.00, '2023-05-30 16:31:48.51572', '', 'WINTEC 2320', 3, '2023-05-30 16:31:48.515703', 225000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e6ab77ca-9f14-43f6-8db6-36fa58930d21', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('b3ed6d1c-c52f-47a8-bf79-086d283fbd6c', '2023-05-29', '', 75000.00, '2023-05-31 22:25:39.41421', '', 'WINTEC 2320', 44, '2023-05-31 22:25:39.414156', 3300000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'cc0ceac9-5697-47ad-97ff-9dc4fe824b66', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('e6ad9650-4b2c-4492-946a-efb2863aa6ed', '2023-05-29', '', 75000.00, '2023-06-01 08:58:39.608164', '', 'WINTEC 2320', 44, '2023-06-01 08:58:39.608132', 3300000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'b12e502d-7ea3-4680-939d-07af65a8edcf', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('2fa838f1-c82d-425d-885e-d040c93669ba', '2023-05-29', '', 75000.00, '2023-06-01 09:00:24.379787', '', 'WINTEC 2320', 5, '2023-06-01 09:00:24.379755', 375000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '7a84bc47-9b22-49fe-9580-6636a607903d', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('cdfcef5c-6fb4-4545-92be-75eac4e10695', '2023-05-29', '', 75000.00, '2023-06-01 09:02:40.845242', '', 'WINTEC 2320', 5, '2023-06-01 09:02:40.845206', 375000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'efc90ca5-a7a4-4dd6-96b0-057655d3e325', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('14a0f970-647c-4eec-94e8-3b625af47b19', '2023-05-29', '', 75000.00, '2023-06-01 09:22:49.29911', '', 'WINTEC 2320', 5, '2023-06-01 09:22:49.299046', 375000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '419cc23f-4a0e-4d4c-a411-913828a6d477', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', NULL, 3, NULL, '00112233', NULL, NULL),
	('0b65420d-e2ed-4443-8b26-ed11fa21cc46', '2023-05-29', '', 75000.00, '2023-06-02 14:08:02.48762', '', 'HP', 10, '2023-06-02 14:08:02.487568', 750000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '08b686ce-8d2f-4cbd-a058-893981b476c7', '1c42af82-202b-4a7b-bcf6-95f137970de7', NULL, 3, NULL, '00112233', NULL, NULL),
	('fc4450e0-fed4-41e8-9bb1-bd9c61ad7f6d', '2023-05-29', '', 75000.00, '2023-06-02 14:10:34.091365', '', 'HP', 2, '2023-06-02 14:10:34.09133', 150000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'ba3abc87-67c3-4a13-b4ab-ce4818bbe2b5', '1c42af82-202b-4a7b-bcf6-95f137970de7', NULL, 3, NULL, '00112233', NULL, NULL),
	('e257090a-3c1e-442f-8e36-3e9c0beab095', '2023-05-29', '', 75000.00, '2023-06-02 14:11:49.740332', '', 'HP', 1, '2023-06-02 14:11:49.740299', 75000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e20af8b8-2233-4899-bc6a-4a5216be1377', '1c42af82-202b-4a7b-bcf6-95f137970de7', NULL, 3, NULL, '00112233', NULL, NULL),
	('40f6ab1e-3d8e-466c-810e-edcc68c47994', '2023-05-29', '', 75000.00, '2023-06-02 16:12:09.245646', 'LT001', '', 44, '2023-06-02 16:12:09.245613', 3300000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e6bf86c8-aef5-4bc6-88be-a3033d8bdb87', 'c87aa0f4-770d-436d-a50f-7456173ff4ce', NULL, 3, NULL, '', NULL, NULL),
	('5d42b165-1973-4320-9997-e64a35d973f3', '2023-05-29', '', 4000.00, '2023-06-02 16:12:09.304207', 'LT002', '', 200, '2023-06-02 16:12:09.304171', 800000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e6bf86c8-aef5-4bc6-88be-a3033d8bdb87', 'bc2c8513-325e-40ec-a4bf-648e08ba66e2', NULL, 3, NULL, '', NULL, NULL),
	('680b17ed-e180-431d-98fc-9c10e54e1b01', '2023-05-29', '', 125.00, '2023-06-02 16:12:09.320943', 'LT003', '', 350, '2023-06-02 16:12:09.320922', 43750.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e6bf86c8-aef5-4bc6-88be-a3033d8bdb87', 'f9cb06a1-d495-414a-b49b-4c58e8952005', NULL, 3, NULL, '', NULL, NULL),
	('0c2548d7-68eb-4de9-a7a7-ccbff9b6325d', '2023-05-29', '', 75.00, '2023-06-02 16:12:09.327623', 'LT004', '', 200, '2023-06-02 16:12:09.327602', 15000.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e6bf86c8-aef5-4bc6-88be-a3033d8bdb87', 'f9cb06a1-d495-414a-b49b-4c58e8952005', NULL, 3, NULL, '', NULL, NULL),
	('e8cafef1-227c-4b6d-b313-016dafc7d90c', '2023-05-29', '', 85.60, '2023-06-02 16:12:09.340637', 'LT006', '', 125, '2023-06-02 16:12:09.340618', 10700.00, 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e6bf86c8-aef5-4bc6-88be-a3033d8bdb87', '4939b39b-6ddf-4d6b-9b9d-abdc0f815519', NULL, 3, NULL, '', NULL, NULL);
/*!40000 ALTER TABLE "tb_movement_products" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_natures
DROP TABLE IF EXISTS "tb_natures";
CREATE TABLE IF NOT EXISTS "tb_natures" (
	"nature_id" UUID NOT NULL,
	"last_update_at" TIMESTAMP NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	PRIMARY KEY ("nature_id")
);

-- A despejar dados para tabela public.tb_natures: 0 rows
DELETE FROM "tb_natures";
/*!40000 ALTER TABLE "tb_natures" DISABLE KEYS */;
INSERT INTO "tb_natures" ("nature_id", "last_update_at", "name", "registered_at") VALUES
	('5d9274ec-2628-4fb9-93d4-4a09b80e5e9d', '2023-05-30 09:45:58.224929', 'MATERIAL DE LIMPEZA HIGIENE E CONSERVAÇAO', '2023-05-30 09:45:58.224893'),
	('5c6a1e8d-f955-49a8-8249-153575f59623', '2023-05-30 09:46:12.799072', 'EQUIPAMENTO DE INFORMATICA', '2023-05-30 09:46:12.799053'),
	('ce0a3503-893b-4d36-a2f7-a1044ae50e80', '2023-05-30 09:46:24.821455', 'OUTROS MATERIAIS DE CONSUMO CORRENTE', '2023-05-30 09:46:24.821434');
/*!40000 ALTER TABLE "tb_natures" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_products
DROP TABLE IF EXISTS "tb_products";
CREATE TABLE IF NOT EXISTS "tb_products" (
	"product_id" UUID NOT NULL,
	"brand" VARCHAR(255) NULL DEFAULT NULL,
	"critical_amount" BIGINT NULL DEFAULT NULL,
	"last_update_at" TIMESTAMP NULL DEFAULT NULL,
	"minimum_amount" BIGINT NULL DEFAULT NULL,
	"name" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"category_category_id" UUID NOT NULL,
	"group_group_id" UUID NOT NULL,
	"nature_id" UUID NULL DEFAULT NULL,
	"storage_storage_id" UUID NOT NULL,
	PRIMARY KEY ("product_id"),
	CONSTRAINT "fk5se73bafmlv16jgbdjyepeg0l" FOREIGN KEY ("category_category_id") REFERENCES "tb_categories" ("category_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fk83jf2uyc4ssh7ssukitkhd0vu" FOREIGN KEY ("group_group_id") REFERENCES "tb_groups" ("group_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkiknubsb0k5exd7lsx0jr24pil" FOREIGN KEY ("nature_id") REFERENCES "tb_natures" ("nature_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkstfgnhk7x9oc4em4kh4p92yak" FOREIGN KEY ("storage_storage_id") REFERENCES "tb_storages" ("storage_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- A despejar dados para tabela public.tb_products: 0 rows
DELETE FROM "tb_products";
/*!40000 ALTER TABLE "tb_products" DISABLE KEYS */;
INSERT INTO "tb_products" ("product_id", "brand", "critical_amount", "last_update_at", "minimum_amount", "name", "registered_at", "category_category_id", "group_group_id", "nature_id", "storage_storage_id") VALUES
	('b4efa864-2184-445e-8dbd-8a3d3b6d5a94', 'WINTECH', 0, NULL, 0, 'UPS', '2023-05-30 14:36:32.344157', '4898a417-5d2e-4eb5-9897-b4f847a136ad', '671b9c17-167a-470d-b53e-dd817feb9199', '5c6a1e8d-f955-49a8-8249-153575f59623', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('1c42af82-202b-4a7b-bcf6-95f137970de7', 'HP', 0, NULL, 0, 'COMPUTADOR HP', '2023-05-30 15:00:58.028525', '4898a417-5d2e-4eb5-9897-b4f847a136ad', '671b9c17-167a-470d-b53e-dd817feb9199', '5c6a1e8d-f955-49a8-8249-153575f59623', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('e45210dc-6cdf-4b4e-82df-fe07e8377fc3', 'WINTECH', 0, NULL, 0, 'IMPRESSORA WINTECH', '2023-05-30 15:01:59.793928', '4898a417-5d2e-4eb5-9897-b4f847a136ad', '671b9c17-167a-470d-b53e-dd817feb9199', '5c6a1e8d-f955-49a8-8249-153575f59623', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('f9cb06a1-d495-414a-b49b-4c58e8952005', '', 10, NULL, 50, 'Artimeter 500mg', '2023-06-02 15:53:12.150202', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('4939b39b-6ddf-4d6b-9b9d-abdc0f815519', '', 10, NULL, 50, 'BROMETO DE RECURONIO 10MG/AMP', '2023-06-02 15:54:39.421738', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('4c0c47c9-e43c-4dc6-a044-66d5cc89f605', '', 10, NULL, 50, 'SUXAMETONIO 100MG /2 ML', '2023-06-02 15:54:50.984493', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('573f343f-d063-4412-9b5a-8678469dab36', '', 10, NULL, 50, 'LOPINAVIR/ROTONAVIR80MG/20MG/XP', '2023-06-02 15:55:01.49598', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('c87aa0f4-770d-436d-a50f-7456173ff4ce', '', 10, NULL, 50, 'AMOXICILINA 250MG/5ML (SUSPENSÃO ORAL)/XP', '2023-06-02 15:55:13.612694', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('339b7a54-7fb3-4cfc-9275-aaea2aa4d488', '', 10, NULL, 50, 'DEXTROSE 5%-500ML/SORO', '2023-06-02 15:55:37.506705', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('908f09e2-b87b-446e-b112-fd80e2f969b1', '', 10, NULL, 50, 'CEFEPIMA( KABI) 1G/AMP', '2023-06-02 15:55:46.562436', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('d6dc4e43-6593-4dff-85b2-d9f8f8a57c7f', '', 10, NULL, 50, 'FERRO+ÁC.FÓLICO', '2023-06-02 15:55:57.197637', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('b41330fb-5cff-4731-833e-3552a0aa24e4', '', 10, NULL, 50, 'FENTANIL 50MG/AMP', '2023-06-02 15:56:10.928377', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('81ec2328-cd8b-462d-aca5-03af6faa1239', '', 10, NULL, 50, 'ETAMBUTOL 100MG/CP 2', '2023-06-02 15:56:21.51583', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('ee3693e1-f7ef-4ba6-a24d-910d5f388b7e', '', 10, NULL, 50, 'EFAVIRENZ 600MG/CP', '2023-06-02 15:56:34.816302', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('b4abb2e0-bb90-44b0-95df-d79d62f519d0', '', 10, NULL, 50, 'EFAVIRENS 200 MG', '2023-06-02 15:56:46.979125', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('aded9dc6-b419-4224-8418-7e343264f53c', '', 10, NULL, 50, 'LOPINAVIR/RITONAVIR 40MG /10MG (PILHETES)', '2023-06-02 15:56:57.968167', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('8f81af0b-9fb5-48ff-8d7e-8b051359293b', '', 10, NULL, 50, 'TENOF 300 MG/LAMIVUD 300 MG/DOLUTEGRAVIR 50 MG.', '2023-06-02 15:57:12.589001', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('658933ae-4de8-4de2-b6ab-3a093c12333a', '', 10, NULL, 50, 'DOLUTEGRAVIR 50 MG CP', '2023-06-02 15:57:26.917691', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('33fcfe2a-13fb-49db-9346-29b8f2201b1a', '', 10, NULL, 50, 'CLARITROMICINA 500MG/AMP', '2023-06-02 15:57:37.711599', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('fc524dc4-88d4-4acb-8030-a5e5d8578125', '', 10, NULL, 50, 'METRONIDAZOL 200MG/XP', '2023-06-02 15:57:52.402742', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('4d9d1d10-f0d3-4bc1-b5ba-ba38c402c704', '', 10, NULL, 50, 'OMEPRAZOL 20MG/CP', '2023-06-02 15:58:07.465107', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('28af8c88-0efb-4089-a163-6d0df490beaf', '', 10, NULL, 50, 'TETRACICLINA 500MG(CÁPSULA)  /CP', '2023-06-02 15:58:19.397331', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('235569e2-2f91-4658-bc0b-eef98a885794', '', 10, NULL, 50, 'NIFEDIPINO 20MG/CP', '2023-06-02 15:58:30.940343', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('7a35d88d-e713-46fd-b570-643246cfe73d', '', 10, NULL, 50, 'NIFEDIPINO 10MG/CP', '2023-06-02 15:58:42.389096', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('9bf5e064-c55a-4a6f-acc9-c2f583a97142', '', 10, NULL, 50, 'INDOMETACINA 25MG(CÁPSULA) /CP', '2023-06-02 15:58:55.22392', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc'),
	('bc2c8513-325e-40ec-a4bf-648e08ba66e2', '', 5, NULL, 50, 'AMPICILINA 500MG/CÁPSULAS 2', '2023-06-02 15:55:25.056595', '833c776a-8f8f-4ebb-b10e-bbe6794a0ffa', '5665e6bc-c972-491d-95b6-0fe6d56fc082', NULL, '915a93a2-0b2d-4dbc-949f-7e557acd1ffc');
/*!40000 ALTER TABLE "tb_products" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_stocks
DROP TABLE IF EXISTS "tb_stocks";
CREATE TABLE IF NOT EXISTS "tb_stocks" (
	"id" UUID NOT NULL,
	"acquisition_date" DATE NULL DEFAULT NULL,
	"barcode" VARCHAR(255) NULL DEFAULT NULL,
	"cust" NUMERIC(19,2) NOT NULL,
	"expiration_date" DATE NULL DEFAULT NULL,
	"lifespan" BIGINT NULL DEFAULT NULL,
	"lote" VARCHAR(255) NULL DEFAULT NULL,
	"model" VARCHAR(255) NULL DEFAULT NULL,
	"quantity" BIGINT NOT NULL,
	"serial_number" VARCHAR(255) NULL DEFAULT NULL,
	"unit_type" VARCHAR(255) NOT NULL,
	"location_location_id" UUID NOT NULL,
	"product_id" UUID NOT NULL,
	"storage_storage_id" UUID NOT NULL,
	"manufacture_date" DATE NULL DEFAULT NULL,
	"modified_date" TIMESTAMP NULL DEFAULT NULL,
	"created_date" TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY ("id"),
	CONSTRAINT "fkd08ljfqege3t8r33fshvpaa23" FOREIGN KEY ("storage_storage_id") REFERENCES "tb_storages" ("storage_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkekta65jxhhcxokj9ctpaea4t0" FOREIGN KEY ("location_location_id") REFERENCES "tb_locations" ("location_id") ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT "fkff7be959jyco0iukc1dcjj9qm" FOREIGN KEY ("product_id") REFERENCES "tb_products" ("product_id") ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- A despejar dados para tabela public.tb_stocks: 0 rows
DELETE FROM "tb_stocks";
/*!40000 ALTER TABLE "tb_stocks" DISABLE KEYS */;
INSERT INTO "tb_stocks" ("id", "acquisition_date", "barcode", "cust", "expiration_date", "lifespan", "lote", "model", "quantity", "serial_number", "unit_type", "location_location_id", "product_id", "storage_storage_id", "manufacture_date", "modified_date", "created_date") VALUES
	('34a738e4-c58f-40ef-a2b1-02a114a11ab3', '2023-05-29', NULL, 4000.00, '2023-06-02', 3, 'LT002', '', 7, '', 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'bc2c8513-325e-40ec-a4bf-648e08ba66e2', '809a774c-d88d-4b11-9458-e93d7b466367', NULL, NULL, NULL),
	('50c68a98-d214-4ace-88a1-ea759ee0d376', '2023-05-29', NULL, 125.00, '2023-12-02', 3, 'LT003', '', 10, '', 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'f9cb06a1-d495-414a-b49b-4c58e8952005', '809a774c-d88d-4b11-9458-e93d7b466367', NULL, NULL, NULL),
	('89fc816b-aab9-4265-9222-53b482e6ed2d', '2023-05-29', NULL, 75.00, '2025-06-02', 3, 'LT004', '', 8, '', 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'f9cb06a1-d495-414a-b49b-4c58e8952005', '809a774c-d88d-4b11-9458-e93d7b466367', NULL, NULL, NULL),
	('f41a31ae-c0e6-4ec7-b461-f3d5dcf65068', '2023-05-29', NULL, 85.60, '2024-06-02', 3, 'LT006', '', 5, '', 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', '4939b39b-6ddf-4d6b-9b9d-abdc0f815519', '809a774c-d88d-4b11-9458-e93d7b466367', NULL, NULL, NULL),
	('e5564adf-564b-480f-8a43-b5fdcf7354af', '2023-05-29', NULL, 105.89, '2024-06-02', 3, 'LT001', '', 44, '', 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'c87aa0f4-770d-436d-a50f-7456173ff4ce', '809a774c-d88d-4b11-9458-e93d7b466367', NULL, '2023-06-03 02:06:43.275', NULL),
	('bc62489c-f6ca-44b2-96d7-15f4a7bb48b4', '2023-05-29', NULL, 75000.00, NULL, 3, '', 'WINTEC 2320', 254, '00112233', 'UN', '13a53552-61fa-4d1a-a77a-0973b1a12038', 'e45210dc-6cdf-4b4e-82df-fe07e8377fc3', '915a93a2-0b2d-4dbc-949f-7e557acd1ffc', NULL, NULL, NULL);
/*!40000 ALTER TABLE "tb_stocks" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_storages
DROP TABLE IF EXISTS "tb_storages";
CREATE TABLE IF NOT EXISTS "tb_storages" (
	"storage_id" UUID NOT NULL,
	"capacity" BIGINT NOT NULL,
	"description" VARCHAR(255) NULL DEFAULT NULL,
	"last_update_at" TIMESTAMP NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	"registered_at" TIMESTAMP NOT NULL,
	"user_group" UUID NOT NULL,
	PRIMARY KEY ("storage_id")
);

-- A despejar dados para tabela public.tb_storages: 0 rows
DELETE FROM "tb_storages";
/*!40000 ALTER TABLE "tb_storages" DISABLE KEYS */;
INSERT INTO "tb_storages" ("storage_id", "capacity", "description", "last_update_at", "name", "registered_at", "user_group") VALUES
	('915a93a2-0b2d-4dbc-949f-7e557acd1ffc', 550, 'Armazém 1', '2023-05-30 14:10:10.449174', 'ARMAZEM1', '2023-05-30 14:10:10.449138', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('3458e202-7ef4-4d06-abd3-e70fe6a8b4c8', 550, 'Armazém 2', '2023-05-30 14:10:21.472114', 'ARMAZEM2', '2023-05-30 14:10:21.47209', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('0de2b643-5e7d-4f47-9e91-bbfff4b19609', 550, 'Armazém 3', '2023-05-30 14:10:32.957247', 'ARMAZEM3', '2023-05-30 14:10:32.95722', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('c4445d8d-6377-4eac-add0-febdb1604b2e', 550, 'Armazém 4', '2023-05-30 14:10:52.143713', 'ARMAZEM4', '2023-05-30 14:10:52.143697', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002'),
	('3e67ab31-c6dd-402c-bd6d-bfd6ff73f357', 2000, 'ARMAZEM ESPECIAL', '2023-05-30 14:12:15.313836', 'ARMAZEM ESPECIAL', '2023-05-30 14:12:15.313818', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('4b8eb3b3-90bf-42fc-8b97-4db7eac63f37', 2000, 'ARMAZEM GASTAVEIS', '2023-05-30 14:12:32.6541', 'ARMAZEM GASTAVEIS', '2023-05-30 14:12:32.654064', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('d9d13666-009c-4b26-a615-a4550ef7fc8a', 2000, 'ARMAZEM DE TESTE', '2023-05-30 14:13:17.654122', 'ARMAZEM TESTE', '2023-05-30 14:13:17.654092', '106c496c-a3e8-41de-bee5-ca26ee9db491'),
	('809a774c-d88d-4b11-9458-e93d7b466367', 2000, 'ARMAZEM DE MEDICAMENTOS', '2023-05-30 14:12:56.998312', 'MEDICAMENTOS', '2023-05-30 14:12:56.998295', '106c496c-a3e8-41de-bee5-ca26ee9db491');
/*!40000 ALTER TABLE "tb_storages" ENABLE KEYS */;

-- A despejar estrutura para tabela public.tb_users
DROP TABLE IF EXISTS "tb_users";
CREATE TABLE IF NOT EXISTS "tb_users" (
	"user_id" UUID NOT NULL,
	"creation_date" TIMESTAMP NOT NULL,
	"email" VARCHAR(255) NOT NULL,
	"full_name" VARCHAR(255) NOT NULL,
	"group_id" UUID NULL DEFAULT NULL,
	"last_update_date" TIMESTAMP NULL DEFAULT NULL,
	"username" VARCHAR(50) NOT NULL,
	PRIMARY KEY ("user_id"),
	UNIQUE INDEX "uk_r43af9ap4edm43mmtq01oddj6" ("username")
);

-- A despejar dados para tabela public.tb_users: 0 rows
DELETE FROM "tb_users";
/*!40000 ALTER TABLE "tb_users" DISABLE KEYS */;
INSERT INTO "tb_users" ("user_id", "creation_date", "email", "full_name", "group_id", "last_update_date", "username") VALUES
	('c32ffa6f-0c8b-4414-97cf-bff8a8ab6eff', '2023-04-25 00:00:00', 'admin@master.com', 'Administrador', 'f5b2794c-4ef7-422f-a05d-48d671bdd4b2', '2023-04-25 13:13:18', 'user.admin'),
	('3ce74305-d091-45a6-8b15-3b51f066e92e', '2023-05-25 10:04:55.887373', 'user@contabilidade.com', 'Usuário da Contabilidade', '0b36efde-148e-4ee4-881e-900976503e3c', NULL, 'user.contabilidade'),
	('740c92e8-3d3f-4fd3-bf54-192a8195a456', '2023-05-25 10:04:35.383628', 'user@tesouraria.com', 'Usuário da Tesouraria', '2a619f0e-a9a3-4b6b-b236-cc14e422cf6b', NULL, 'user.tesouraria'),
	('1bad6860-0b45-4eca-a070-95c0c7531c23', '2023-05-25 10:05:42.306184', 'user@patrimonio.com', 'Usuário do Patrimonio', 'c83aeaa9-c4a5-4269-bd9f-cb4a1b6fe002', '2023-05-25 10:15:09.831621', 'user.patrimonio'),
	('4efe97e1-5d9d-43ea-8cf3-e74b6970f2ae', '2023-05-25 10:02:36.936714', 'user@estoque.com', 'Usuário do Estoque', '106c496c-a3e8-41de-bee5-ca26ee9db491', NULL, 'user.estoque'),
	('79140389-dd34-4864-bd5e-ecd3c11a929c', '2023-05-25 16:31:40.116718', 'user@guest.com', 'Usuário Convidado', '0b36efde-148e-4ee4-881e-900976503e3c', '2023-05-25 16:33:48.193982', 'user.guest');
/*!40000 ALTER TABLE "tb_users" ENABLE KEYS */;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
