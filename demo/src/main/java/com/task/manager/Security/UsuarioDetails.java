package com.task.manager.Security;

import com.task.manager.DataBase.Model.UsuarioEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class UsuarioDetails implements UserDetails {

    @Getter
    private final UsuarioEntity usuarioEntity ;

    @Override
    public String getUsername() {

        return usuarioEntity.getEmail();
    }

    @Override
    public String getPassword() {
       return usuarioEntity.getSenha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuarioEntity.getRoleType().name()));

    }

    @Override
    public boolean isAccountNonExpired() {
       return true ;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true ;
    }

    @Override
    public boolean isCredentialsNonExpired() {
       return true ;
    }

    @Override
    public boolean isEnabled() {
        return true ;
    }


}
