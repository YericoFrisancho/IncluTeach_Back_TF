package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.PublicacionForoDTO;
import com.upc.inclutechtrabajo_parcial.model.ForoQueSigueUsuario;
import com.upc.inclutechtrabajo_parcial.model.Notificacion;
import com.upc.inclutechtrabajo_parcial.model.PublicacionForo;
import com.upc.inclutechtrabajo_parcial.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublicacionForoService {
    @Autowired
    private PublicacionForoRepository publicacionRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private ForoQueSigueUsuarioRepository seguimientoRepo;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ForoRepository foroRepository;

    public PublicacionForoDTO registrar(PublicacionForoDTO dto) {
        PublicacionForo publicacion = new PublicacionForo();
        publicacion.setTitulo(dto.getTitulo());
        publicacion.setContenido(dto.getContenido());
        publicacion.setFecha(LocalDateTime.now());

        if (dto.getUsuarioId() != null) {
            usuarioRepository.findById(dto.getUsuarioId())
                    .ifPresent(publicacion::setUsuario);
        }
        if (dto.getForoId() != null) {
            foroRepository.findById(dto.getForoId())
                    .ifPresent(publicacion::setForo);
        }

        PublicacionForo guardado = publicacionRepository.save(publicacion);

        crearNotificacionesMasivas(guardado);

        return convertirADTO(guardado);
    }

    private void crearNotificacionesMasivas(PublicacionForo p) {
        if (p.getForo() == null) return;

        List<ForoQueSigueUsuario> seguidores = seguimientoRepo.findAll().stream()
                .filter(s ->
                        s.getForo().getId().equals(p.getForo().getId()) &&
                                Boolean.TRUE.equals(s.getNotificacionesActivas())
                )
                .collect(Collectors.toList());

        for (ForoQueSigueUsuario s : seguidores) {
            if (!s.getUsuario().getId().equals(p.getUsuario().getId())) {
                Notificacion n = Notificacion.builder()
                        .descripcion("Nueva publicación en: " + p.getForo().getTitulo())
                        .fecha(LocalDateTime.now())
                        .visto(false)
                        .publicacionId(p.getId())
                        .usuarioSigueForo(s)
                        .build();
                notificacionRepository.save(n);
            }
        }
    }

    public List<PublicacionForoDTO> listar() {
        return publicacionRepository.findAll().stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    public PublicacionForoDTO actualizar(Integer id, PublicacionForoDTO dto) {
        PublicacionForo p = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID " + id));

        p.setTitulo(dto.getTitulo());
        p.setContenido(dto.getContenido());

        return convertirADTO(publicacionRepository.save(p));
    }

    public void eliminar(Integer id) {
        if (!publicacionRepository.existsById(id)) {
            throw new RuntimeException("Publicación no encontrada");
        }
        publicacionRepository.deleteById(id);
    }

    public List<PublicacionForoDTO> filtrarPorUsuario(Long id) {
        return publicacionRepository.findByUsuarioId(id).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<PublicacionForoDTO> filtrarPorForo(Long id) {
        return publicacionRepository.findByForoId(id).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<PublicacionForoDTO> buscarPorTitulo(String s) {
        return publicacionRepository.findByTituloContaining(s).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    private PublicacionForoDTO convertirADTO(PublicacionForo p) {
        PublicacionForoDTO dto = new PublicacionForoDTO();
        dto.setId(p.getId());
        dto.setTitulo(p.getTitulo());
        dto.setContenido(p.getContenido());
        dto.setFecha(p.getFecha());

        if (p.getUsuario() != null) {
            dto.setUsuarioId(p.getUsuario().getId());
            dto.setNombreUsuario(p.getUsuario().getUsername());
        }
        if (p.getForo() != null) {
            dto.setForoId(p.getForo().getId());
            dto.setTituloForo(p.getForo().getTitulo());
        }
        return dto;
    }

    public List<Map<String, Object>> obtenerEstadisticasForos() {
        return publicacionRepository.obtenerEstadisticasForos();
    }

    public List<Map<String, Object>> obtenerTopUsuarios() {
        return publicacionRepository.obtenerTopUsuarios();
    }

    public PublicacionForoDTO obtenerPorId(Integer id) {
        PublicacionForo p = publicacionRepository.findById(id).orElse(null);

        if (p == null) return null;

        return convertirADTO(p);
    }
}