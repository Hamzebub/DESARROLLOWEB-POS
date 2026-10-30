package com.dcolores.pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dcolores.pos.entity.Pagos;

public interface PagoRepository extends JpaRepository<Pagos, Integer> {
	List<Pagos> findByIdVentaIn(List<Long> idsVentas);
}
