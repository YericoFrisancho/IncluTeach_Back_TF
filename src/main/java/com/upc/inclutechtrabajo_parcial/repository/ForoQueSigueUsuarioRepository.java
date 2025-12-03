package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.ForoQueSigueUsuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ForoQueSigueUsuarioRepository extends JpaRepository<ForoQueSigueUsuario, Integer> {
    @Transactional
    void deleteByUsuarioIdAndForoId(Integer usuarioId, Integer foroId);

    boolean existsByUsuarioIdAndForoId(Integer usuarioId, Integer foroId);

    Optional<ForoQueSigueUsuario> findByUsuarioIdAndForoId(Integer usuarioId, Integer foroId);
}