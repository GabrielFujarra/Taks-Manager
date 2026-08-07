CREATE TABLE time (
    id  BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE usuario(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    time_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario_time FOREIGN KEY (time_id) REFERENCES time(id)
);

CREATE TABLE tarefa(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_tarefa_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    time_id BIGINT NOT NULL,
    CONSTRAINT fk_tarefa_time FOREIGN KEY (time_id) REFERENCES time(id)
);