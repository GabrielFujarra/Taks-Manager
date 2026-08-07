package com.task.manager.demo.DataBase.Repository;

import com.task.manager.demo.DataBase.Model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
