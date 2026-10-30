package com.dcolores.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcolores.pos.entity.Kardex;

public interface KardexRepository  extends JpaRepository<Kardex, Long>{

}
