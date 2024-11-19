package com.backus.proyecto.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table
@Data
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMarca")
    private Integer idMarca;
    private String nombre;
    private boolean visible;

}
