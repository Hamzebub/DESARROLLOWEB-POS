package com.dcolores.pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.dcolores.pos.entity.Kardex;

public interface KardexRepository  extends JpaRepository<Kardex, Long>{

	List<Kardex> findAllByOrderByIdKardexDesc();
}
