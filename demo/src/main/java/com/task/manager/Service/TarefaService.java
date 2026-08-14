package com.task.manager.Service;

import com.task.manager.DataBase.Model.TarefaEntity;
import com.task.manager.DataBase.Model.TimeEntity;
import com.task.manager.DataBase.Model.UsuarioEntity;
import com.task.manager.DataBase.Repository.TarefaRepository;
import com.task.manager.DataBase.Repository.TimeRepository;
import com.task.manager.DataBase.Repository.UsuarioRepository;
import com.task.manager.Dto.request.TarefaRequestDto;
import com.task.manager.Dto.response.TarefaResponseDto;
import com.task.manager.Enums.RoleType;
import com.task.manager.Exception.BadRequestException;
import com.task.manager.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.LongFunction;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TimeRepository timeRepository ;

    @Transactional
    public void criarTarefa(TarefaRequestDto tarefaDto, Long usuarioId) {

        UsuarioEntity usuarioLogado = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!usuarioLogado.getRoleType().equals(RoleType.LIDER)) {
            throw new BadRequestException(
                    "Você não tem permissão para criar tarefas nesse time"
            );
        }

        TimeEntity time = usuarioLogado.getTime();

        if (time == null) {
            throw new NotFoundException("Usuário não possui um time");
        }

        tarefaRepository.save(
                TarefaEntity.builder()
                        .nome(tarefaDto.nome())
                        .descricao(tarefaDto.descricao())
                        .status(tarefaDto.status())
                        .usuarioTarefa(usuarioLogado)
                        .timeTarefa(time)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public List<TarefaResponseDto> buscarTarefasDoTimeDoUsuario(Long usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (usuario.getTime() == null) {
            throw new BadRequestException("O usuário não pertence a nenhum time.");
        }

        Long timeId = usuario.getTime().getId();

        return tarefaRepository.findByTimeIdWithFetch(timeId)
                .stream()
                .map(tarefa -> new TarefaResponseDto(
                        tarefa.getId(),
                        tarefa.getNome(),
                        tarefa.getDescricao(),
                        tarefa.getStatus(),
                        tarefa.getTimeTarefa().getNome(),
                        tarefa.getUsuarioTarefa() != null ? tarefa.getUsuarioTarefa().getEmail() : null
                ))
                .toList();
    }

    public void deletarTarefa(Long id) {
        TarefaEntity tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada"));

        tarefaRepository.delete(tarefa);
    }
}
