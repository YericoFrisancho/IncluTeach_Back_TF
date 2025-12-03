package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.RecursoEducativoDTO;
import com.upc.inclutechtrabajo_parcial.service.RecursoEducativoService;
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
@RequestMapping("/recursos")
@CrossOrigin(origins = "*")
public class RecursoEducativoController {
    @Autowired
    private RecursoEducativoService recursoService;

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'PROFESIONAL')")
    @PostMapping("/registrar")
    public ResponseEntity<RecursoEducativoDTO> registrar(@RequestBody RecursoEducativoDTO dto) {
        return ResponseEntity.ok(recursoService.registrar(dto));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id,
                                        @RequestBody RecursoEducativoDTO dto,
                                        Authentication authentication) {

        RecursoEducativoDTO recurso = recursoService.obtenerPorId(id);
        if (recurso == null) return ResponseEntity.notFound().build();

        String usuarioLogueado = authentication.getName();
        boolean esAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"));
        boolean esDuenio = recurso.getNombreUsuario().equals(usuarioLogueado);

        if (!esAdmin && !esDuenio) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para editar este recurso");
        }

        return ResponseEntity.ok(recursoService.actualizar(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id, Authentication authentication) {

        RecursoEducativoDTO recurso = recursoService.obtenerPorId(id);
        if (recurso == null) return ResponseEntity.notFound().build();

        String usuarioLogueado = authentication.getName();
        boolean esAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRADOR"));
        boolean esDuenio = recurso.getNombreUsuario().equals(usuarioLogueado);

        if (!esAdmin && !esDuenio) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar este recurso");
        }

        recursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RecursoEducativoDTO>> listar() {
        return ResponseEntity.ok(recursoService.listar());
    }

    @GetMapping("/favoritos/{userId}")
    public ResponseEntity<List<RecursoEducativoDTO>> listarFavoritos(@PathVariable Integer userId) {
        return ResponseEntity.ok(recursoService.listarFavoritos(userId));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/categoria/{id}")
    public ResponseEntity<List<RecursoEducativoDTO>> porCategoria(@PathVariable Integer id) {
        return ResponseEntity.ok(recursoService.listarPorCategoria(id));
    }

    @GetMapping("/estadisticas/categorias")
    public ResponseEntity<List<Map<String, Object>>> getEstadisticasCategorias() {
        return ResponseEntity.ok(recursoService.obtenerEstadisticasCategorias());
    }
}