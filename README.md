# Gym Management System

## Descrição

O **Gym Management System** é uma aplicação desenvolvida em **JavaFX** para gerenciar as operações de uma academia, incluindo o cadastro de alunos, personal trainers, aulas e planos. O sistema permite realizar buscas, editar informações e gerenciar dados de maneira eficiente.

Este projeto utiliza o **JavaFX** para a interface gráfica e o **MySQL** como banco de dados para armazenamento das informações. As funcionalidades principais incluem:

- Cadastro de Alunos
- Cadastro de Personal Trainers
- Gerenciamento de Aulas
- Cadastro de Planos de academia
- Busca de registros por nome e tipo
- Edição e exclusão de dados cadastrados

### Integração com Banco de Dados

- O sistema utiliza o MySQL para armazenar todos os dados.

## Estrutura do Banco

```bash
-- Criar o Banco de Dados
CREATE DATABASE IF NOT EXISTS academia;
USE academia;

-- Criar Tabela Pessoa (base para Aluno e PersonalTrainer)
CREATE TABLE IF NOT EXISTS pessoa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100)
);

-- Criar Tabela Plano
CREATE TABLE IF NOT EXISTS plano (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    descricao TEXT
);

-- Criar Tabela PersonalTrainer (extensão da tabela Pessoa)
CREATE TABLE IF NOT EXISTS personal_trainer (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pessoa_id INT NOT NULL,
    registro_profissional VARCHAR(50) NOT NULL,
    especialidade VARCHAR(100),
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE
);

-- Criar Tabela Aluno (extensão da tabela Pessoa)
CREATE TABLE IF NOT EXISTS aluno (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pessoa_id INT NOT NULL,
    data_inicio DATE NOT NULL,
    idade INT,
    personal_id INT,
    plano_id INT,
    FOREIGN KEY (pessoa_id) REFERENCES pessoa(id) ON DELETE CASCADE,
    FOREIGN KEY (personal_id) REFERENCES personal_trainer(id) ON DELETE SET NULL,
    FOREIGN KEY (plano_id) REFERENCES plano(id) ON DELETE SET NULL
);

-- Criar Tabela Aula
CREATE TABLE IF NOT EXISTS aula (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome_aula VARCHAR(100) NOT NULL,
    data DATE NOT NULL,
    duracao INT NOT NULL,  -- duração em minutos
    personal_id INT,
    FOREIGN KEY (personal_id) REFERENCES personal_trainer(id) ON DELETE SET NULL
);
CREATE TABLE aula_aluno (
    aula_id INT NOT NULL,
    aluno_id INT NOT NULL,
    PRIMARY KEY (aula_id, aluno_id),
    FOREIGN KEY (aula_id) REFERENCES aula(id) ON DELETE CASCADE,
    FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE
);


-- Inserir dados na tabela Pessoa
INSERT INTO pessoa (nome, cpf, telefone, email) VALUES
('João Silva', '123.456.789-00', '11999999999', 'joao@example.com'),
('Maria Souza', '987.654.321-00', '11888888888', 'maria@example.com');

-- Inserir dados na tabela PersonalTrainer
INSERT INTO personal_trainer (pessoa_id, registro_profissional, especialidade) VALUES
(1, 'PT1234', 'Musculação'),
(2, 'PT5678', 'Yoga');

-- Inserir dados na tabela Plano
INSERT INTO plano (tipo, valor, descricao) VALUES
('Mensal', 150.00, 'Plano mensal com acesso livre a todas as aulas'),
('Anual', 1200.00, 'Plano anual com desconto');

-- Inserir dados na tabela Aluno
INSERT INTO aluno (pessoa_id, data_inicio, idade, personal_id, plano_id) VALUES
(1, '2023-01-01', 25, 1, 1),
(2, '2023-02-01', 30, 2, 2);

-- Inserir dados na tabela Aula
INSERT INTO aula (nome_aula, data, duracao, personal_id) VALUES
('Musculação Avançada', '2023-09-01', 60, 1),
('Yoga para Iniciantes', '2023-09-05', 45, 2);


```

## É necessário alterar as variáveis de usuário e senha no arquivo DatabaseConnection.java.
