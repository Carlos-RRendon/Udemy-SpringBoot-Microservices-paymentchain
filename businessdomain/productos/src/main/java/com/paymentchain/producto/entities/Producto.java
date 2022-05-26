package com.paymentchain.producto.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String code;

}