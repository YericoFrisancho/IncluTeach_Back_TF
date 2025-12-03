package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.NotificacionDTO;
import com.upc.inclutechtrabajo_parcial.model.ForoQueSigueUsuario;
import com.upc.inclutechtrabajo_parcial.model.Notificacion;
import com.upc.inclutechtrabajo_parcial.repository.ForoQueSigueUsuarioRepository;
import com.upc.inclutechtrabajo_parcial.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {
    @Autowired private NotificacionRepository notificacionRepository;
    @Autowired private ForoQueSigueUsuarioRepository seguimientoRepository;

    public String toggleNotificacion(Integer usuarioId, Integer foroId) {
        ForoQueSigueUsuario seguimiento = seguimientoRepository.findByUsuarioIdAndForoId(usuarioId, foroId)
                .orElseThrow(() -> new RuntimeException("No sigues este foro"));

        boolean estadoActual = Boolean.TRUE.equals(seguimiento.getNotificacionesActivas());
        boolean nuevoEstado = !estadoActual;

        seguimiento.setNotificacionesActivas(nuevoEstado);
        seguimientoRepository.save(seguimiento);

        return nuevoEstado ? "ACTIVADO" : "DESACTIVADO";
    }

    public List<NotificacionDTO> listarPorUsuario(Integer usuarioId) {
        return notificacionRepository.listarPorUsuario(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public long contarSinLeer(Integer userId) {
        return notificacionRepository.contarNoLeidas(userId);
    }

    public void limpiarBandeja(Integer userId) {
        notificacionRepository.deleteByUsuarioId(userId);
    }

    public void marcarComoVisto(Integer id) {
        notificacionRepository.findById(id).ifPresent(n -> {
            n.setVisto(true);
            notificacionRepository.save(n);
        });
    }

    public NotificacionDTO cambiarEstadoVisto(Integer id) {
        Notificacion n = notificacionRepository.findById(id).orElseThrow();
        n.setVisto(!Boolean.TRUE.equals(n.getVisto()));
        return convertirADTO(notificacionRepository.save(n));
    }

    public Notificacion registrar(Notificacion n) { return notificacionRepository.save(n); }
    public List<NotificacionDTO> listar() { return notificacionRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList()); }
    public Notificacion actualizar(Integer id, Notificacion n) { n.setId(id); return notificacionRepository.save(n); }
    public void eliminar(Integer id) { notificacionRepository.deleteById(id); }

    private NotificacionDTO convertirADTO(Notificacion n) {
        return NotificacionDTO.builder()
                .id(n.getId())
                .descripcion(n.getDescripcion())
                .fecha(n.getFecha())
                .visto(n.getVisto())
                .publicacionId(n.getPublicacionId())
                .usuarioSigueForoId(n.getUsuarioSigueForo().getId())
                .foroId(n.getUsuarioSigueForo().getForo().getId())
                .build();
    }
}