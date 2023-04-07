create database db_smart_energy;
use db_smart_energy;

describe CLIENTE;
describe COMPANHIA;
describe RESIDENCIA;
describe FATURA;
describe MEDIDOR;



CREATE TABLE CLIENTE (
    codigo SMALLINT auto_increment PRIMARY KEY,
    nome VARCHAR (50) NOT NULL,
    cpf VARCHAR (14) NOT NULL,
    senha VARCHAR (20) NOT NULL,
    email VARCHAR (50) NOT NULL,
    telefone VARCHAR (11) NOT NULL
);

CREATE TABLE COMPANHIA (
    codigo SMALLINT AUTO_INCREMENT PRIMARY KEY,
    tarifa_tusd DECIMAL(10) NOT NULL,
    sigla VARCHAR(10) NOT NULL,
    uf VARCHAR(2) NOT NULL,
    tarifa_te DECIMAL(10) NOT NULL,
    pis DECIMAL(4) NOT NULL,
    cofins DECIMAL,
    icms DECIMAL
);

CREATE TABLE RESIDENCIA (
    codigo SMALLINT auto_increment PRIMARY KEY,
    logradouro VARCHAR (200) NOT NULL,
    cep VARCHAR (9) NOT NULL,
    municipio VARCHAR (50) NOT NULL,
    uf VARCHAR (2) NOT NULL,
    numero INTEGER (10) NOT NULL,
    bairro VARCHAR (60) NOT NULL,
    complemento VARCHAR (50) NOT NULL,
    FK_CLIENTE_codigo SMALLINT
);
 
ALTER TABLE RESIDENCIA ADD CONSTRAINT FK_RESIDENCIA_2
    FOREIGN KEY (FK_CLIENTE_codigo)
    REFERENCES RESIDENCIA (codigo);

CREATE TABLE FATURA (
    codigo SMALLINT auto_increment PRIMARY KEY,
    valor DECIMAL (8) NOT NULL,
    mes_vigente DATE NOT NULL,
    medicao_mes_anterior DECIMAL (9) NOT NULL,
    medicao_mes_atual DECIMAL (9) NOT NULL,
    FK_CLIENTE_codigo SMALLINT,
    FK_RESIDENCIA_codigo SMALLINT,
    FK_COMPANHIA_codigo SMALLINT,
    data_leitura_mes_atual DATE NOT NULL,
    data_leitura_mes_anterior DATE NOT NULL
);
 
ALTER TABLE FATURA ADD CONSTRAINT FK_FATURA_2
    FOREIGN KEY (FK_COMPANHIA_codigo)
    REFERENCES FATURA (codigo);
 
ALTER TABLE FATURA ADD CONSTRAINT FK_FATURA_3
    FOREIGN KEY (FK_RESIDENCIA_codigo)
    REFERENCES FATURA (codigo);
 
ALTER TABLE FATURA ADD CONSTRAINT FK_FATURA_4
    FOREIGN KEY (FK_CLIENTE_codigo)
    REFERENCES FATURA (codigo);
    
    
CREATE TABLE MEDIDOR (
    consumo DECIMAL (5) NOT NULL,
    registro_dia DATE NOT NULL,
    registro_horario TIME NOT NULL,
    codigo SMALLINT PRIMARY KEY,
    medicao_atual DECIMAL,
    FK_FATURA_codigo SMALLINT,
    FK_RESIDENCIA_codigo SMALLINT
);
SELECT * FROM MEDIDOR WHERE codigo = '1';
ALTER TABLE MEDIDOR MODIFY COLUMN codigo INT AUTO_INCREMENT;

insert into MEDIDOR (consumo, registro_dia, registro_horario, medicao_atual, FK_FATURA_codigo, FK_RESIDENCIA_codigo ) VALUES (5, '2022-03-22', '10:30:00', 5, 1, 1);
alter table RESIDENCIA 


/* Comandos eu utilizei gravar e consultar dados */

SET @ultimo_valor = (SELECT MAX(medicao_atual) FROM medidor WHERE FK_RESIDENCIA_codigo = 1);
SET @ultimo_codigo = (SELECT MAX(codigo) FROM medidor WHERE FK_RESIDENCIA_codigo = 1);
SET @consumo = ROUND(RAND() * 5);

INSERT INTO medidor (consumo, registro_dia, registro_horario, medicao_atual, FK_FATURA_codigo, FK_RESIDENCIA_codigo)
SELECT 
  @consumo,
  CURDATE(),
  '23:00:00',
  @ultimo_valor + @consumo,
  1,
  1
FROM medidor
WHERE codigo = @ultimo_codigo;

select * from medidor order by codigo desc;

-- Somar valores para o dia de hoje
SELECT SUM(consumo) FROM medidor WHERE registro_dia = CURDATE();

-- Soma para o mês atual
SELECT SUM(consumo) FROM medidor WHERE MONTH(registro_dia) = MONTH(CURDATE());

-- Soma entre datas
SELECT SUM(consumo) AS total_consumo
FROM medidor
WHERE registro_dia BETWEEN '2023-04-05' AND '2023-04-06';

-- Soma entre datas definindo uma data de inicio fixa(data de leitura) até dia de hoje
SET @inicio_leitura = '2023-04-05';
SELECT SUM(consumo) AS total_consumo
FROM medidor
WHERE registro_dia BETWEEN @inicio_leitura AND CURDATE();

-- Somar valores de acordo com um horário
SELECT SUM(consumo) FROM medidor 
WHERE registro_dia = CURDATE() 
AND registro_horario BETWEEN '08:00:00' AND '20:00:00';
