package com.dcolores.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcolores.pos.entity.Pagos;

public interface PagoRepository extends JpaRepository<Pagos, Integer> {

}
