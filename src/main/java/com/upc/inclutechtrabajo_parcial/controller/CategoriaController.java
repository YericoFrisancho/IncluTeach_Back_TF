package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.CategoriaDTO;
import com.upc.inclutechtrabajo_parcial.model.Categoria;
import com.upc.inclutechtrabajo_parcial.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'PROFESIONAL')")
    @PostMapping("/registrar")
    public ResponseEntity<Categoria> registrar(@RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.registrar(dto));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Integer id, @RequestBody CategoriaDTO dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }
}