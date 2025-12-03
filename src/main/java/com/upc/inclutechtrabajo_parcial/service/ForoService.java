package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.ForoDTO;
import com.upc.inclutechtrabajo_parcial.model.Foro;
import com.upc.inclutechtrabajo_parcial.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForoService {
    @Autowired
    private ForoRepository foroRepository;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private ForoQueSigueUsuarioRepository seguimientoRepository;


    public ForoDTO registrar(ForoDTO dto) {
        Foro foro = new Foro();
        foro.setTitulo(dto.getTitulo());
        foro.setDescripcion(dto.getDescripcion());
        foro.setFechaCreacion(LocalDateTime.now());

        if (dto.getUsuarioId() != null) {
            usuarioRepository.findById(dto.getUsuarioId())
                    .ifPresent(foro::setUsuario);
        }

        if (dto.getCategoriaId() != null) {
            categoriaRepository.findById(dto.getCategoriaId())
                    .ifPresent(foro::setCategoria);
        }

        return convertirADTO(foroRepository.save(foro));
    }

    public List<ForoDTO> listar() {
        return foroRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ForoDTO actualizar(Integer id, ForoDTO dto) {
        Foro foroExistente = foroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foro con ID " + id + " no encontrado"));

        foroExistente.setTitulo(dto.getTitulo());
        foroExistente.setDescripcion(dto.getDescripcion());

        if (dto.getCategoriaId() != null) {
            categoriaRepository.findById(dto.getCategoriaId())
                    .ifPresent(foroExistente::setCategoria);
        }

        return convertirADTO(foroRepository.save(foroExistente));
    }

    public void eliminar(Integer id) {
        if (!foroRepository.existsById(id)) {
            throw new RuntimeException("Foro con ID " + id + " no encontrado");
        }
        foroRepository.deleteById(id);
    }

    private ForoDTO convertirADTO(Foro foro) {
        ForoDTO dto = new ForoDTO();
        dto.setId(foro.getId());
        dto.setTitulo(foro.getTitulo());
        dto.setDescripcion(foro.getDescripcion());
        dto.setFechaCreacion(foro.getFechaCreacion());

        if (foro.getUsuario() != null) {
            dto.setUsuarioId(foro.getUsuario().getId());
            dto.setNombreUsuario(foro.getUsuario().getUsername());
        }
        if (foro.getCategoria() != null) {
            dto.setCategoriaId(foro.getCategoria().getId());
            dto.setNombreCategoria(foro.getCategoria().getNombre());
        }
        return dto;
    }

    public List<ForoDTO> listarPorUsuario(Integer idUsuario) {
        return foroRepository.findByUsuario_Id(idUsuario).stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<ForoDTO> listarPorCategoria(Integer idCategoria) {
        return foroRepository.findByCategoriaId(idCategoria).stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<ForoDTO> listarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        return foroRepository.findByRangoFechas(inicio, fin).stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<ForoDTO> listarDesdeFecha(LocalDateTime fechaDesde) {
        return foroRepository.findDesdeFecha(fechaDesde).stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<ForoDTO> listarRecientes() {
        return foroRepository.findRecientes().stream()
                .map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<ForoDTO> listarForosSeguidosPorUsuario(Integer idUsuario) {
        return seguimientoRepository.findAll().stream()
                .filter(s -> s.getUsuario().getId().equals(idUsuario))
                .map(seguimiento -> {
                    ForoDTO dto = convertirADTO(seguimiento.getForo());

                    boolean tieneNoti = Boolean.TRUE.equals(seguimiento.getNotificacionesActivas());

                    dto.setNotificacionesActivas(tieneNoti);

                    return dto;
                })
                .collect(Collectors.toList());
    }
}