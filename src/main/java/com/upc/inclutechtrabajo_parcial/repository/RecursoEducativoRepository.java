package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.RecursoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface RecursoEducativoRepository extends JpaRepository<RecursoEducativo, Integer> {
    List<RecursoEducativo> findByCategoriaId(Integer id);

    @Query("SELECT c.nombre as label, COUNT(r) as cantidad FROM RecursoEducativo r JOIN r.categoria c GROUP BY c.nombre")
    List<Map<String, Object>> obtenerEstadisticasCategorias();
}
