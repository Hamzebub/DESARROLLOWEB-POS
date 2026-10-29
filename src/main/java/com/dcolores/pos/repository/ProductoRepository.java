package com.dcolores.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcolores.pos.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}
