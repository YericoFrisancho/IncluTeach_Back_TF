package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.ComentarioDTO;
import com.upc.inclutechtrabajo_parcial.model.Comentario;
import com.upc.inclutechtrabajo_parcial.repository.ComentarioRepository;
import com.upc.inclutechtrabajo_parcial.repository.PublicacionForoRepository; // Importar
import com.upc.inclutechtrabajo_parcial.repository.UsuarioRepository;     // Importar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {
    @Autowired private ComentarioRepository comentarioRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PublicacionForoRepository publicacionRepository;

    public ComentarioDTO registrar(ComentarioDTO dto) {
        Comentario comentario = new Comentario();
        comentario.setContenido(dto.getContenido());
        comentario.setFecha(LocalDateTime.now());

        if (dto.getUsuarioId() != null) {
            usuarioRepository.findById(dto.getUsuarioId()).ifPresent(comentario::setUsuario);
        }

        if (dto.getPublicacionId() != null) {
            publicacionRepository.findById(dto.getPublicacionId()).ifPresent(comentario::setPublicacion);
        }

        Comentario guardado = comentarioRepository.save(comentario);
        return convertirADTO(guardado);
    }

    public List<ComentarioDTO> listar() {
        return comentarioRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<ComentarioDTO> listarPorPublicacion(Integer publicacionId) {
        return comentarioRepository.findByPublicacionId(publicacionId)
                .stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public ComentarioDTO actualizar(Integer id, ComentarioDTO dto) {
        Comentario existente = comentarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        existente.setContenido(dto.getContenido());

        return convertirADTO(comentarioRepository.save(existente));
    }

    public void eliminar(Integer id) {
        comentarioRepository.deleteById(id);
    }

    private ComentarioDTO convertirADTO(Comentario c) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setId(c.getId());
        dto.setContenido(c.getContenido());
        dto.setFecha(c.getFecha());

        if (c.getUsuario() != null) {
            dto.setUsuarioId(c.getUsuario().getId());
            dto.setNombreUsuario(c.getUsuario().getUsername());
        }
        if (c.getPublicacion() != null) {
            dto.setPublicacionId(c.getPublicacion().getId());
            dto.setTituloPublicacion(c.getPublicacion().getTitulo());
        }
        return dto;
    }
}