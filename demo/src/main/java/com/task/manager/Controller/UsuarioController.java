package com.task.manager.Controller;

import com.task.manager.Dto.request.UsuarioRequestDto;
import com.task.manager.Dto.response.UsuarioResponseDto;
import com.task.manager.Security.UsuarioDetails;
import com.task.manager.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        List<UsuarioResponseDto> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioPorEmail(@PathVariable String email){
        UsuarioResponseDto usuario = usuarioService.buscarUsuarioPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Void> criarUsuario(@RequestBody UsuarioRequestDto usuarioRequest){
        usuarioService.criarUsuario(usuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String email){
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{email}/promover")
    @PreAuthorize("hasRole('LIDER')")
    public ResponseEntity<Void> promoverUsuario (@PathVariable String email) {
        usuarioService.promoverUsuario(email);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}/promover-teste")
    public ResponseEntity<Void> promoverUsuarioTeste (@PathVariable String email) {
        usuarioService.promoverUsuario(email);

        return ResponseEntity.noContent().build();

    }

    @PutMapping("/entrar-no-time/{nomeTime}")
    public ResponseEntity<Void> entrarNoTime (@PathVariable String nomeTime, @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        usuarioService.entrarNoTime(nomeTime, usuarioDetails.getUsername());
        return ResponseEntity.noContent().build();
    }


}
