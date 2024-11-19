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
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPedido")
    private Integer idPedido;

    @Column(name = "fechaOperacion")
    private LocalDateTime fechaOperacion;

    @Column(name = "cantidadP")
    private int cantidadP;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id")
    private Repartidor repartidor;

    private String anonimoNombre;
    private String anonimoEmail;
    private String anonimoTelefono;
    private String metodoPago;
    private String estado;
    // Relación inversa con Detalle (un pedido puede tener muchos detalles)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detalle> detalles;

    public Pedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Integer idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDateTime getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(LocalDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public int getCantidadP() {
        return cantidadP;
    }

    public void setCantidadP(int cantidadP) {
        this.cantidadP = cantidadP;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public String getAnonimoNombre() {
        return anonimoNombre;
    }

    public void setAnonimoNombre(String anonimoNombre) {
        this.anonimoNombre = anonimoNombre;
    }

    public String getAnonimoEmail() {
        return anonimoEmail;
    }

    public void setAnonimoEmail(String anonimoEmail) {
        this.anonimoEmail = anonimoEmail;
    }

    public String getAnonimoTelefono() {
        return anonimoTelefono;
    }

    public void setAnonimoTelefono(String anonimoTelefono) {
        this.anonimoTelefono = anonimoTelefono;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }
}
