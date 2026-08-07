ALTER TABLE tarefa
    DROP CONSTRAINT fk_tarefa_time,
    DROP CONSTRAINT fk_tarefa_usuario;

ALTER TABLE tarefa
    ADD CONSTRAINT fk_tarefa_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_tarefa_time FOREIGN KEY (time_id) REFERENCES time(id) ON DELETE CASCADE;
