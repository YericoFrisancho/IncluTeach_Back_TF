package com.upc.inclutechtrabajo_parcial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private Integer rolId;
    private Integer id;

}