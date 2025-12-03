package com.upc.inclutechtrabajo_parcial.security;


import com.upc.inclutechtrabajo_parcial.model.Usuario;
import com.upc.inclutechtrabajo_parcial.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (usuario.getRol() != null) {
            String rolOriginal = usuario.getRol().getNombre();


            String rolFinal = rolOriginal.toUpperCase();

            System.out.println(">>> LOGIN INTENTO: " + username);
            System.out.println(">>> ROL EN BD: " + rolOriginal);
            System.out.println(">>> PERMISO GENERADO: " + rolFinal);

            authorities.add(new SimpleGrantedAuthority(rolFinal));
        }

        return new User(usuario.getUsername(), usuario.getContrasena(), authorities);
    }
}