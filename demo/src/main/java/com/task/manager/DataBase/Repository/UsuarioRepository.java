package com.task.manager.DataBase.Repository;

import com.task.manager.DataBase.Model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
