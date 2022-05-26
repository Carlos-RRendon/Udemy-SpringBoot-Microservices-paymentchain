/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.paymentchain.producto.controller;


import com.paymentchain.producto.entities.Producto;

import com.paymentchain.producto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 *
 * @author Lenovo
 */
@RestController
@RequestMapping("/producto")
public class ProductRestController {

    @Autowired
    ProductoRepository productoRepository;

    
    @GetMapping()
    public List<Producto> list() {
        return productoRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Producto get(@PathVariable long id) {
        Producto producto = productoRepository.findById(id).get();
        return  producto;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Producto input) {
        return null;
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody Producto input) {
        Producto save = productoRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }
    
}
