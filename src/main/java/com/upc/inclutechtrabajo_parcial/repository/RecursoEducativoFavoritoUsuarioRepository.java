package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.RecursoEducativoFavoritoUsuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoEducativoFavoritoUsuarioRepository extends JpaRepository<RecursoEducativoFavoritoUsuario, Integer> {

    @Transactional
    void deleteByUsuarioIdAndRecursoId(Integer usuarioId, Integer recursoId);

    boolean existsByUsuarioIdAndRecursoId(Integer usuarioId, Integer recursoId);

    List<RecursoEducativoFavoritoUsuario> findByUsuarioId(Integer usuarioId);
}