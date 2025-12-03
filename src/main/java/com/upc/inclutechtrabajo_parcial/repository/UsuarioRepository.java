package com.upc.inclutechtrabajo_parcial.repository;

import com.upc.inclutechtrabajo_parcial.model.PublicacionForo;
import com.upc.inclutechtrabajo_parcial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.rol.nombre) = LOWER(:nombreRol)")
    List<Usuario> findByRolNombreIgnoreCase(@Param("nombreRol") String nombreRol);


    @Query("SELECT DISTINCT u FROM Usuario u JOIN RecursoEducativoFavoritoUsuario f ON f.usuario.id = u.id")
    List<Usuario> findUsuariosConFavoritos();

    @Query("SELECT DISTINCT u FROM Usuario u JOIN ForoQueSigueUsuario fq ON fq.usuario.id = u.id")
    List<Usuario> findUsuariosQueSiguenForos();


    @Query("SELECT r.nombre as rol, COUNT(u) as cantidad FROM Usuario u JOIN u.rol r GROUP BY r.nombre")
    List<Map<String, Object>> obtenerDistribucionRoles();

    @Query("SELECT r.nombre as label, COUNT(u) as cantidad FROM Usuario u JOIN u.rol r GROUP BY r.nombre")
    List<Map<String, Object>> obtenerEstadisticasRoles();
}
