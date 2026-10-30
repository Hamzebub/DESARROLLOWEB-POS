package com.dcolores.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcolores.pos.entity.DetalleVenta;



public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

}
