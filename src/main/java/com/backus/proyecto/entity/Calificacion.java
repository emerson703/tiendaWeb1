package com.backus.proyecto.entity;

import jakarta.persistence.*;

@Entity
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único de la calificación

    @ManyToOne
    @JoinColumn(name = "idPedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente; // Relación con el cliente que hace la calificación (referencia al ID del cliente)
    private int calificacion; // Puntuación (por ejemplo, de 1 a 5)
    private String comentario; // Texto opcional que el cliente puede agregar

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Calificacion{" +
                "id=" + id +
                ", pedido=" + (pedido != null ? pedido.getIdPedido() : null) + // Acceso al ID del pedido
                ", cliente=" + (cliente != null ? cliente.getIdCliente() : null) + // Acceso al ID del cliente
                ", calificacion=" + calificacion +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
