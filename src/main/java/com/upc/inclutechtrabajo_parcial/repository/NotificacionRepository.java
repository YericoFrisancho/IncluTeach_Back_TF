package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    @Query("SELECT n FROM Notificacion n JOIN FETCH n.usuarioSigueForo fsu JOIN FETCH fsu.foro f WHERE fsu.usuario.id = :userId ORDER BY n.fecha DESC")
    List<Notificacion> listarPorUsuario(@Param("userId") Integer userId);

    @Query("SELECT COUNT(n) FROM Notificacion n JOIN n.usuarioSigueForo fsu WHERE fsu.usuario.id = :userId AND n.visto = false")
    long contarNoLeidas(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notificacion n WHERE n.usuarioSigueForo.id IN (SELECT fsu.id FROM ForoQueSigueUsuario fsu WHERE fsu.usuario.id = :userId)")
    void deleteByUsuarioId(@Param("userId") Integer userId);
}