package com.upc.inclutechtrabajo_parcial.dto;
import lombok.Data;

@Data
public class RegisterRequest {
    private String nombre;
    private String username;
    private String email;
    private String password;
    private Integer rolId;
}