package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.RecursoEducativoDTO;
import com.upc.inclutechtrabajo_parcial.model.RecursoEducativo;
import com.upc.inclutechtrabajo_parcial.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecursoEducativoService {
    @Autowired private RecursoEducativoRepository recursoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private RecursoEducativoFavoritoUsuarioRepository favoritoRepo;

    public RecursoEducativoDTO registrar(RecursoEducativoDTO dto) {
        RecursoEducativo recurso = new RecursoEducativo();
        recurso.setTitulo(dto.getTitulo());
        recurso.setDescripcion(dto.getDescripcion());

        if (dto.getCategoriaId() != null) categoriaRepository.findById(dto.getCategoriaId()).ifPresent(recurso::setCategoria);
        if (dto.getUsuarioId() != null) usuarioRepository.findById(dto.getUsuarioId()).ifPresent(recurso::setUsuario);

        return convertirADTO(recursoRepository.save(recurso));
    }

    public List<RecursoEducativoDTO> listar() {
        return recursoRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<RecursoEducativoDTO> listarFavoritos(Integer usuarioId) {
        return favoritoRepo.findByUsuarioId(usuarioId).stream()
                .map(fav -> {
                    RecursoEducativoDTO dto = convertirADTO(fav.getRecurso());
                    dto.setEsFavorito(true);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public RecursoEducativoDTO actualizar(Integer id, RecursoEducativoDTO dto) {
        RecursoEducativo r = recursoRepository.findById(id).orElseThrow();
        r.setTitulo(dto.getTitulo());
        r.setDescripcion(dto.getDescripcion());
        if(dto.getCategoriaId() != null) categoriaRepository.findById(dto.getCategoriaId()).ifPresent(r::setCategoria);

        return convertirADTO(recursoRepository.save(r));
    }

    public void eliminar(Integer id) {
        recursoRepository.deleteById(id);
    }

    private RecursoEducativoDTO convertirADTO(RecursoEducativo r) {
        RecursoEducativoDTO dto = new RecursoEducativoDTO();
        dto.setId(r.getId());
        dto.setTitulo(r.getTitulo());
        dto.setDescripcion(r.getDescripcion());
        if (r.getCategoria() != null) {
            dto.setCategoriaId(r.getCategoria().getId());
            dto.setNombreCategoria(r.getCategoria().getNombre());
        }
        if (r.getUsuario() != null) {
            dto.setUsuarioId(r.getUsuario().getId());
            dto.setNombreUsuario(r.getUsuario().getUsername());
        }
        return dto;
    }

    public List<RecursoEducativoDTO> listarPorCategoria(Integer catId) {
        return recursoRepository.findByCategoriaId(catId).stream().map(this::convertirADTO).collect(Collectors.toList());
    }
    public List<Map<String, Object>> obtenerEstadisticasCategorias() {
        return recursoRepository.obtenerEstadisticasCategorias();
    }

    public RecursoEducativoDTO obtenerPorId(Integer id) {
        RecursoEducativo recurso = recursoRepository.findById(id).orElse(null);
        if (recurso == null) return null;
        return convertirADTO(recurso);
    }
}