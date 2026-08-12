package com.task.manager.Controller;


import com.task.manager.Dto.request.LoginRequestDto;
import com.task.manager.Dto.response.LoginResponseDto;
import com.task.manager.Security.TokenService;
import com.task.manager.Security.UsuarioDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService ;
    private final AuthenticationManager authenticationManager ;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login (@RequestBody LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.senha()));

        UsuarioDetails usuarioDetails = (UsuarioDetails) authentication.getPrincipal();

        String token = tokenService.gerarToken(usuarioDetails);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
