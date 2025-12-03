package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.ForoDTO;
import com.upc.inclutechtrabajo_parcial.service.ForoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/foros")
@CrossOrigin(origins = "http://localhost:4200")
public class ForoController {
    @Autowired
    private ForoService foroService;

    @PostMapping("/registrar")
    public ResponseEntity<ForoDTO> registrar(@RequestBody ForoDTO dto) {
        return ResponseEntity.ok(foroService.registrar(dto));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ForoDTO>> listar() {
        return ResponseEntity.ok(foroService.listar());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ForoDTO> actualizar(@PathVariable Integer id, @RequestBody ForoDTO dto) {
        return ResponseEntity.ok(foroService.actualizar(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        foroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ForoDTO>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(foroService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ForoDTO>> listarPorCategoria(@PathVariable Integer idCategoria) {
        return ResponseEntity.ok(foroService.listarPorCategoria(idCategoria));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<ForoDTO>> listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(foroService.listarPorFechas(inicio, fin));
    }

    @GetMapping("/desde")
    public ResponseEntity<List<ForoDTO>> listarDesdeFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde) {
        return ResponseEntity.ok(foroService.listarDesdeFecha(fechaDesde));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<ForoDTO>> listarRecientes() {
        return ResponseEntity.ok(foroService.listarRecientes());
    }

    @GetMapping("/seguidos/{idUsuario}")
    public ResponseEntity<List<ForoDTO>> listarForosSeguidos(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(foroService.listarForosSeguidosPorUsuario(idUsuario));
    }
}