package com.task.manager.DataBase.Repository;

import com.task.manager.DataBase.Model.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeRepository extends JpaRepository<TimeEntity, Long> {

    Optional<TimeEntity> findByNome(String nome);

    Boolean existsByNome(String nome);
}
