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

    public void criarTarefa(TarefaRequestDto tarefaDto, String emailUsuarioLogado){

        UsuarioEntity usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        UsuarioEntity usuario = usuarioRepository.findByEmail(tarefaDto.emailUsuario())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        TimeEntity time = timeRepository.findByNome(tarefaDto.nomeTime())
                .orElseThrow(() -> new NotFoundException("Time não encontrado"));

        if (!usuario.getTime().getId().equals(time.getId())) {
            throw new BadRequestException("Usuário não pertence ao time especificado");
        }

        if (!usuarioLogado.getRoleType().equals(RoleType.LIDER)
                || !usuarioLogado.getTime().getId().equals(time.getId())) {
            throw new BadRequestException("Você não tem permissão para criar tarefas nesse time");
        }

        tarefaRepository.save(TarefaEntity.builder()
                .nome(tarefaDto.nome())
                .descricao(tarefaDto.descricao())
                .status(tarefaDto.status())
                .usuarioTarefa(usuario)
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
