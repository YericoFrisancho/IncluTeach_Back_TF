package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.UsuarioDTO;
import com.upc.inclutechtrabajo_parcial.service.UsuarioService;
import com.upc.inclutechtrabajo_parcial.security.JwtTokenUtil;
import com.upc.inclutechtrabajo_parcial.security.UsuarioDetailsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap; // Para el Map de respuesta
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "https://inclutech-frontend.web.app")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsuarioDetailsService usuarioDetailsService;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.registrar(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody UsuarioDTO dto) {
        dto.setId(id);
        UsuarioDTO usuarioActualizado = usuarioService.actualizar(id, dto);

        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(usuarioActualizado.getUsername());
        final String nuevoToken = jwtTokenUtil.generateToken(userDetails);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("usuario", usuarioActualizado);
        respuesta.put("newToken", nuevoToken);

        return ResponseEntity.ok(respuesta);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/rol/{rol}")
    public ResponseEntity<List<UsuarioDTO>> filtrarPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.filtrarPorRol(rol));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/favoritos")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosConFavoritos() {
        return ResponseEntity.ok(usuarioService.listarConFavoritos());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/foros-seguidos")
    public ResponseEntity<List<UsuarioDTO>> listarUsuariosQueSiguenForos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosQueSiguenForos());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/distribucion-roles")
    public ResponseEntity<List<Map<String, Object>>> obtenerDistribucionRoles() {
        return ResponseEntity.ok(usuarioService.obtenerDistribucionRoles());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/estadisticas/roles")
    public ResponseEntity<List<Map<String, Object>>> getEstadisticasRoles() {
        return ResponseEntity.ok(usuarioService.obtenerEstadisticasRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(id));
    }
}