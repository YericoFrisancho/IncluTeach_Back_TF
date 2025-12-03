package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.PublicacionForo;
import com.upc.inclutechtrabajo_parcial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PublicacionForoRepository extends JpaRepository<PublicacionForo, Integer> {
    Optional<Object> findByUsuario(Usuario usuario);
    @Query("SELECT p FROM PublicacionForo p WHERE p.usuario.id = :idUsuario")
    List<PublicacionForo> findByUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM PublicacionForo p WHERE p.foro.id = :idForo")
    List<PublicacionForo> findByForoId(@Param("idForo") Long idForo);

    @Query("SELECT p FROM PublicacionForo p WHERE LOWER(p.titulo) LIKE LOWER(CONCAT('%', :palabra, '%'))")
    List<PublicacionForo> findByTituloContaining(@Param("palabra") String palabra);

    @Query("SELECT f.titulo as label, COUNT(p) as cantidad FROM PublicacionForo p JOIN p.foro f GROUP BY f.titulo")
    List<Map<String, Object>> obtenerEstadisticasForos();

    @Query("SELECT u.username as label, COUNT(p) as cantidad FROM PublicacionForo p JOIN p.usuario u GROUP BY u.username ORDER BY cantidad DESC")
    List<Map<String, Object>> obtenerTopUsuarios();
}
