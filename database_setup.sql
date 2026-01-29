-- ============================================
-- CS Móveis - Script de Criação do Banco
-- ============================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS csmoveis
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE csmoveis;

-- ============================================
-- Tabela: clientes
-- Armazena todos os dados dos clientes
-- ============================================

CREATE TABLE IF NOT EXISTS clientes (
    -- Identificação
    id INT AUTO_INCREMENT PRIMARY KEY,
    
    -- Dados Pessoais
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE COMMENT 'CPF formatado: 000.000.000-00',
    rg VARCHAR(20) UNIQUE COMMENT 'RG/Identidade',
    data_nascimento DATE,
    
    -- Contato Principal
    telefone VARCHAR(20) NOT NULL COMMENT 'Telefone principal formatado',
    email VARCHAR(150) NOT NULL,
    
    -- Contato Secundário
    telefone_secundario VARCHAR(20) COMMENT 'Telefone alternativo',
    
    -- Endereço Completo
    cep VARCHAR(9) NOT NULL COMMENT 'CEP formatado: 00000-000',
    endereco VARCHAR(200) NOT NULL COMMENT 'Rua, Avenida, etc',
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2) COMMENT 'UF: SP, RJ, MG, etc',
    
    -- Status do Cliente
    status VARCHAR(20) DEFAULT 'Ativo' CHECK(status IN ('Ativo', 'Inativo')),
    
    -- Observações
    observacoes TEXT,
    
    -- Controle Automático
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Cadastro de clientes';

-- ============================================
-- Índices para Performance
-- ============================================

-- CPF já é UNIQUE, serve como índice automaticamente
CREATE INDEX idx_nome ON clientes(nome);
CREATE INDEX idx_email ON clientes(email);
CREATE INDEX idx_telefone ON clientes(telefone);
CREATE INDEX idx_status ON clientes(status);
CREATE INDEX idx_cidade ON clientes(cidade);
CREATE INDEX idx_data_cadastro ON clientes(data_cadastro);

-- ============================================
-- Dados de Teste (Opcional - Comentado)
-- ============================================

/*
INSERT INTO clientes (nome, cpf, rg, telefone, email, cep, endereco, numero, complemento, bairro, cidade, estado) VALUES
('Ana Silva', '123.456.789-00', '12.345.678-9', '(11) 98765-4321', 'ana.silva@email.com', '01001-000', 'Av. Paulista', '1000', 'Apto 42', 'Bela Vista', 'São Paulo', 'SP'),
('Carlos Júnior', '987.654.321-11', '98.765.432-1', '(21) 91234-5678', 'carlos.jr@empresa.com', '20000-000', 'Rua das Flores', '250', '', 'Centro', 'Rio de Janeiro', 'RJ'),
('Mariana Alves', '456.789.012-34', '45.678.901-2', '(31) 99887-7665', 'mariana.alves@design.com', '30000-000', 'Rua Central', '500', 'Casa', 'Savassi', 'Belo Horizonte', 'MG');
*/

-- ============================================
-- Verificação
-- ============================================

-- Ver estrutura da tabela
DESCRIBE clientes;

-- Contar registros
SELECT COUNT(*) AS total_clientes FROM clientes;

-- ✅ Script Completo!
