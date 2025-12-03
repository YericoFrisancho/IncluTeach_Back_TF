package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.PublicacionForoDTO;
import com.upc.inclutechtrabajo_parcial.service.PublicacionForoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publicaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicacionForoController {

    @Autowired
    private PublicacionForoService publicacionService;

    @PostMapping("/registrar")
    public ResponseEntity<PublicacionForoDTO> registrar(@RequestBody PublicacionForoDTO dto) {
        return ResponseEntity.ok(publicacionService.registrar(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PublicacionForoDTO>> listar() {
        return ResponseEntity.ok(publicacionService.listar());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id,
                                        @RequestBody PublicacionForoDTO dto,
                                        Authentication authentication) {

        PublicacionForoDTO postActual = publicacionService.obtenerPorId(id);

        if (postActual == null) return ResponseEntity.notFound().build();

        String usuarioLogueado = authentication.getName();
        boolean esAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"));

        boolean esDuenio = postActual.getNombreUsuario().equals(usuarioLogueado);

        if (!esAdmin && !esDuenio) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para editar esto");
        }

        return ResponseEntity.ok(publicacionService.actualizar(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id, Authentication authentication) {

        PublicacionForoDTO postActual = publicacionService.obtenerPorId(id);
        if (postActual == null) return ResponseEntity.notFound().build();

        String usuarioLogueado = authentication.getName();
        boolean esAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"));
        boolean esDuenio = postActual.getNombreUsuario().equals(usuarioLogueado);

        if (!esAdmin && !esDuenio) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No puedes eliminar contenido de otros");
        }

        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/foro/{id}")
    public ResponseEntity<List<PublicacionForoDTO>> listarPorForoPublico(@PathVariable("id") Long idForo) {
        return ResponseEntity.ok(publicacionService.filtrarPorForo(idForo));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/usuario/{id}")
    public ResponseEntity<List<PublicacionForoDTO>> filtrarPorUsuario(@PathVariable("id") Long idUsuario) {
        return ResponseEntity.ok(publicacionService.filtrarPorUsuario(idUsuario));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/foro/{id}")
    public ResponseEntity<List<PublicacionForoDTO>> filtrarPorForoAdmin(@PathVariable("id") Long idForo) {
        return ResponseEntity.ok(publicacionService.filtrarPorForo(idForo));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/busqueda/{palabra}")
    public ResponseEntity<List<PublicacionForoDTO>> buscarPorTitulo(@PathVariable("palabra") String palabra) {
        return ResponseEntity.ok(publicacionService.buscarPorTitulo(palabra));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/estadisticas/foros")
    public ResponseEntity<List<Map<String, Object>>> getEstadisticasForos() {
        return ResponseEntity.ok(publicacionService.obtenerEstadisticasForos());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/estadisticas/top-usuarios")
    public ResponseEntity<List<Map<String, Object>>> getTopUsuarios() {
        return ResponseEntity.ok(publicacionService.obtenerTopUsuarios());
    }
}