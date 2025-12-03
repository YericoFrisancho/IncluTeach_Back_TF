package com.upc.inclutechtrabajo_parcial.service;

import com.upc.inclutechtrabajo_parcial.dto.CategoriaDTO;
import com.upc.inclutechtrabajo_parcial.model.Categoria;
import com.upc.inclutechtrabajo_parcial.repository.CategoriaRepository;
import com.upc.inclutechtrabajo_parcial.repository.UsuarioRepository; // Importar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Categoria registrar(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        if (dto.getUsuarioId() != null) {
            usuarioRepository.findById(dto.getUsuarioId())
                    .ifPresent(categoria::setUsuario);
        }

        return categoriaRepository.save(categoria);
    }

    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO actualizar(Integer id, CategoriaDTO dto) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría con ID " + id + " no encontrada"));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());

        Categoria actualizado = categoriaRepository.save(existente);
        return convertirADTO(actualizado);
    }

    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría con ID " + id + " no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .usuarioId(categoria.getUsuario() != null ? categoria.getUsuario().getId() : null)
                .build();
    }
}