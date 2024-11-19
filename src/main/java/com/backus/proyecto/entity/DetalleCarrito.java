package com.backus.proyecto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "detalleCarrito")
@Data
public class DetalleCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleCarrito;
    @ManyToOne
    @JoinColumn(name = "idCarrito", nullable = false)
    private Carrito carrito;
    @ManyToOne
    @JoinColumn(name = "idProducto", nullable = false)
    private Producto producto;
    private int cantidad;
    private double precio;
    private double subtotal;

}
