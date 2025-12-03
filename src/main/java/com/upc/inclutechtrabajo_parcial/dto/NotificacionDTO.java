package com.upc.inclutechtrabajo_parcial.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionDTO {
    private Integer id;
    private String descripcion;

    private LocalDateTime fecha;
    private Boolean visto;
    private Integer publicacionId;
    private Integer usuarioSigueForoId;
    private Integer foroId;
}