package com.upc.inclutechtrabajo_parcial.controller;

import com.upc.inclutechtrabajo_parcial.dto.NotificacionDTO;
import com.upc.inclutechtrabajo_parcial.model.Notificacion;
import com.upc.inclutechtrabajo_parcial.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {
    @Autowired private NotificacionService notificacionService;

    @PostMapping("/toggle/usuario/{uid}/foro/{fid}")
    public ResponseEntity<String> toggle(@PathVariable Integer uid, @PathVariable Integer fid) {
        String res = notificacionService.toggleNotificacion(uid, fid);
        return ResponseEntity.ok("{\"mensaje\": \"" + res + "\"}");
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<NotificacionDTO>> listar() {
        return ResponseEntity.ok(notificacionService.listar());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/consultas/usuario/{id}")
    public ResponseEntity<List<NotificacionDTO>> listarPorUsuarioAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(id));
    }

    @GetMapping("/mis-notificaciones/{id}")
    public ResponseEntity<List<NotificacionDTO>> listarMisNotificaciones(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(id));
    }

    @GetMapping("/contador/{id}")
    public ResponseEntity<Long> contar(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.contarSinLeer(id));
    }

    @PutMapping("/marcar-visto/{id}")
    public ResponseEntity<Void> marcarVisto(@PathVariable Integer id) {
        notificacionService.marcarComoVisto(id);
        return ResponseEntity.ok().build();
    }
}
