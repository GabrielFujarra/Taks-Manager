package com.task.manager.demo.DataBase.Repository;

import com.task.manager.demo.DataBase.Model.TimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeRepository extends JpaRepository<TimeEntity, Long> {

    Optional<TimeEntity> findByNome(String nome);
}
