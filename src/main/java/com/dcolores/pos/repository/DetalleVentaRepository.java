package com.dcolores.pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcolores.pos.entity.DetalleVenta;



public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

	List<DetalleVenta> findByIdVentaIn(List<Long> idsVentas);
}
