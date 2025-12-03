package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.Foro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ForoRepository extends JpaRepository<Foro, Integer> {

    @Query("SELECT f FROM Foro f WHERE f.categoria.id = :idCategoria")
    List<Foro> findByCategoriaId(@Param("idCategoria") Integer idCategoria);

    @Query("SELECT f FROM Foro f WHERE f.fechaCreacion BETWEEN :inicio AND :fin")
    List<Foro> findByRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT f FROM Foro f WHERE f.fechaCreacion >= :fechaDesde")
    List<Foro> findDesdeFecha(@Param("fechaDesde") LocalDateTime fechaDesde);

    @Query("SELECT f FROM Foro f ORDER BY f.fechaCreacion DESC")
    List<Foro> findRecientes();

    @Query("SELECT f FROM Foro f JOIN f.seguidores s WHERE s.usuario.id = :idUsuario")
    List<Foro> findForosSeguidosPorUsuario(@Param("idUsuario") Integer idUsuario);

    List<Foro> findByUsuario_Id(Integer usuarioId);

}
