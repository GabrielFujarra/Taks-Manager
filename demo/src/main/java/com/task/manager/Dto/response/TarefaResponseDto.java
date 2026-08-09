package com.task.manager.Dto.response;

import com.task.manager.Enums.StatusType;

public record TarefaResponseDto(Long id, String nome, String descricao, StatusType statusType, String nomeTime, String emailUsuario) {
}
