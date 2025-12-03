package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.AuthRequest;
import com.upc.inclutechtrabajo_parcial.dto.AuthResponse;
import com.upc.inclutechtrabajo_parcial.dto.RegisterRequest;
import com.upc.inclutechtrabajo_parcial.model.Rol;
import com.upc.inclutechtrabajo_parcial.model.Usuario;
import com.upc.inclutechtrabajo_parcial.repository.RolRepository;
import com.upc.inclutechtrabajo_parcial.repository.UsuarioRepository;
import com.upc.inclutechtrabajo_parcial.security.JwtTokenUtil;
import com.upc.inclutechtrabajo_parcial.security.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections; // Importante

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UsuarioDetailsService usuarioDetailsService;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(new AuthResponse(
                token,
                usuario.getUsername(),
                usuario.getRol().getId(),
                usuario.getId()
        ));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));

        Usuario nuevoUsuario = Usuario.builder()
                .nombre(request.getNombre())
                .username(request.getUsername())
                .email(request.getEmail())
                .contrasena(passwordEncoder.encode(request.getPassword()))
                .rol(rol)
                .build();

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(Collections.singletonMap("mensaje", "Usuario registrado exitosamente"));
    }
}