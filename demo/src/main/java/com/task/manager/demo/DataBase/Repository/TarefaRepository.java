package com.task.manager.demo.DataBase.Repository;

import com.task.manager.demo.DataBase.Model.TarefaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<TarefaEntity, Long> {

    List<TarefaEntity>findByTimeTarefaId(Long timeId);

    List<TarefaEntity>findByUsuarioTarefaId(Long usuarioId);
}
