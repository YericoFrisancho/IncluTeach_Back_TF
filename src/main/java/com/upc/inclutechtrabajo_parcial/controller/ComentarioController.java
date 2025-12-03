package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.ComentarioDTO;
import com.upc.inclutechtrabajo_parcial.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin(origins = "*")
public class ComentarioController {
    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/registrar")
    public ResponseEntity<ComentarioDTO> registrar(@RequestBody ComentarioDTO dto) {
        return ResponseEntity.ok(comentarioService.registrar(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ComentarioDTO>> listar() {
        return ResponseEntity.ok(comentarioService.listar());
    }

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<List<ComentarioDTO>> listarPorPublicacionNormal(@PathVariable Integer id) {
        return ResponseEntity.ok(comentarioService.listarPorPublicacion(id));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/publicacion/{id}")
    public ResponseEntity<List<ComentarioDTO>> listarPorPublicacionAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(comentarioService.listarPorPublicacion(id));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        comentarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}