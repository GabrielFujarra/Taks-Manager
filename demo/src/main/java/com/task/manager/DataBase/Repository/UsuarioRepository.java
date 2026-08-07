package com.task.manager.DataBase.Repository;

import com.task.manager.DataBase.Model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {


    boolean existsByEmail(String email);

    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.time")
    List<UsuarioEntity> findAllWithTime();

    Optional<UsuarioEntity> findByEmail(String email);


}
