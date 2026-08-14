package com.task.manager.Controller;

import com.task.manager.Dto.request.TarefaRequestDto;
import com.task.manager.Dto.response.TarefaResponseDto;
import com.task.manager.Security.UsuarioDetails;
import com.task.manager.Service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService ;


    @GetMapping("/meu-time")
    public ResponseEntity<List<TarefaResponseDto>> listarTarefaPorTime (@AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        Long usuarioId = usuarioDetails.getUsuarioEntity().getId();
        List<TarefaResponseDto> tarefas  = tarefaService.buscarTarefasDoTimeDoUsuario(usuarioId);
        return ResponseEntity.ok(tarefas);
    }

    @PostMapping
    public ResponseEntity<Void> criarTarefa (@RequestBody TarefaRequestDto tarefaRequest, @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        Long usuarioId = usuarioDetails.getUsuarioEntity().getId();

        tarefaService.criarTarefa(tarefaRequest, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefaPorId (@PathVariable Long id) {
        tarefaService.deletarTarefa(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
