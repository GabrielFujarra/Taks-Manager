package com.task.manager.Dto.request;

import com.task.manager.Enums.StatusType;

public record TarefaRequestDto(String nome, String descricao, StatusType status, String nomeTime, String emailUsuario) {
}
