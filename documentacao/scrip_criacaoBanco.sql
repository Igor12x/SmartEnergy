create database smart_energy;
use smart_energy;

describe CLIENTE;
describe COMPANHIA;
describe RESIDENCIA;
describe FATURA;



CREATE TABLE CLIENTE (
    codigo SMALLINT auto_increment PRIMARY KEY,
    nome VARCHAR (50) NOT NULL,
    cpf INTEGER (14) NOT NULL,
    senha VARCHAR (20) NOT NULL,
    email VARCHAR (50) NOT NULL,
    telefone INTEGER (11) NOT NULL
);

CREATE TABLE COMPANHIA (
    codigo SMALLINT auto_increment PRIMARY KEY,
    tarifa_tusd DECIMAL (10) NOT NULL,
    sigla VARCHAR (10) NOT NULL,
    uf VARCHAR (2) NOT NULL,
    tarifa_te DECIMAL (10) NOT NULL
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
    REFERENCES CLIENTE (codigo);
    
    CREATE TABLE FATURA (
    codigo SMALLINT auto_increment PRIMARY KEY,
    valor FLOAT (8) NOT NULL,
    mes_vigente DATE NOT NULL,
    vencimento DATE NOT NULL,
    consumo DECIMAL (8) NOT NULL,
    data_leitura DATE NOT NULL,
    medicao_inicial DECIMAL (9) NOT NULL,
    medicao_atual DECIMAL (9) NOT NULL,
    pis FLOAT (4) NOT NULL,
    icms FLOAT (4) NOT NULL,
    FK_CLIENTE_codigo SMALLINT,
    FK_RESIDENCIA_codigo SMALLINT,
    FK_COMPANHIA_codigo SMALLINT
);
 
ALTER TABLE FATURA ADD CONSTRAINT FK_FATURA_2
    FOREIGN KEY (FK_COMPANHIA_codigo)
    REFERENCES COMPANHIA (codigo);
 
ALTER TABLE FATURA ADD CONSTRAINT FK_FATURA_3
    FOREIGN KEY (FK_RESIDENCIA_cogigo)
    REFERENCES RESIDENCIA (codigo);
 
ALTER TABLE FATURA ADD CONSTRAINT FK_FATURA_4
    FOREIGN KEY (FK_CLIENTE_codigo)
    REFERENCES CLIENTE (codigo);
