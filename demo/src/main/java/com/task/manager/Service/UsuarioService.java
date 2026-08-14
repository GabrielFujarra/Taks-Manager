package com.task.manager.Service;


import com.task.manager.Config.SecurityConfig;
import com.task.manager.DataBase.Model.TimeEntity;
import com.task.manager.DataBase.Model.UsuarioEntity;
import com.task.manager.DataBase.Repository.TimeRepository;
import com.task.manager.DataBase.Repository.UsuarioRepository;
import com.task.manager.Dto.request.UsuarioRequestDto;
import com.task.manager.Dto.response.UsuarioResponseDto;
import com.task.manager.Enums.RoleType;
import com.task.manager.Exception.BadRequestException;
import com.task.manager.Exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TimeRepository timeRepository;
    private final PasswordEncoder passwordEncoder ;


    public void criarUsuario(UsuarioRequestDto usuarioDto){

        boolean usuario = usuarioRepository.existsByEmail(usuarioDto.email());

        if (usuario){
            throw new BadRequestException("Usuário já existe");
        }

        usuarioRepository.save(UsuarioEntity.builder()
                .nome(usuarioDto.nome())
                .email(usuarioDto.email())
                .senha(passwordEncoder.encode(usuarioDto.senha()))
                .roleType(RoleType.INTEGRANTE)
                .build());


    }

    public List<UsuarioResponseDto> listarUsuarios() {

        return usuarioRepository.findAllWithTime()
                .stream()
                .map(usuario -> new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail(),usuario.getTime().getNome()))
                .toList();
    }

    public UsuarioResponseDto buscarUsuarioPorEmail(String email){

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        return new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTime().getNome());
    }

    public void deletarUsuarioPorEmail(String email){

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));;
        usuarioRepository.delete(usuario);
    }

    public void promoverUsuario (String email) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        usuario.setRoleType(RoleType.LIDER);

        usuarioRepository.save(usuario);
    }

    public void entrarNoTime (String nomeTime, String emailUsuarioLogado) {

        TimeEntity time = timeRepository.findByNome(nomeTime)
                .orElseThrow(() -> new NotFoundException("Time não encontrado")) ;

        UsuarioEntity usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(()-> new NotFoundException("Usuário não encontrado")) ;

        usuario.setTime(time);
        usuarioRepository.save(usuario);
    }

}
