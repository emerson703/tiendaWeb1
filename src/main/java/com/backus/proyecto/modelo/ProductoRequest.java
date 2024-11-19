package com.backus.proyecto.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ProductoRequest {
    private Integer idProducto;
    private String nombre;
    private double precioUnitario;
    private int cantidad;
    private double precioTotal;

    // Constructor adicional si lo necesitas, Lombok generará uno por defecto también

}
