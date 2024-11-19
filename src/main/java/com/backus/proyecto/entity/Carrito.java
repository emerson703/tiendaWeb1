package com.backus.proyecto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "carrito")
@Data
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarrito")
    private Integer idCarrito;

    @Column(name = "fechaOperacion")
    private LocalDateTime fechaOperacion;

    @Column(name = "cantidadP")
    private int cantidadP;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    private String metodoPago;
    private String estado;

    // Relación inversa con Detalle (un pedido puede tener muchos detalles)
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCarrito> detalleCarrito;

    public Carrito(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }
}
