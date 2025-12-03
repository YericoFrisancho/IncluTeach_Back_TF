package com.upc.inclutechtrabajo_parcial.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoEducativoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer categoriaId;
    private Integer usuarioId;

    private String nombreCategoria;
    private String nombreUsuario;

    private boolean esFavorito;
}