package com.task.manager.DataBase.Repository;

import com.task.manager.DataBase.Model.TarefaEntity;
import com.task.manager.DataBase.Model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<TarefaEntity, Long> {

    @Query("SELECT u FROM TarefaEntity u JOIN FETCH u.timeTarefa JOIN FETCH u.usuarioTarefa")
    List<TarefaEntity> findAllWithTime();


    @Query("SELECT t FROM TarefaEntity t JOIN FETCH t.timeTarefa JOIN FETCH t.usuarioTarefa WHERE t.timeTarefa.id = :timeId")
    List<TarefaEntity> findByTimeIdWithFetch(Long timeId);
}
