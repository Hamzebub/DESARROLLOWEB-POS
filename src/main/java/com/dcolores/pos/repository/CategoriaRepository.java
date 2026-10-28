package com.dcolores.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcolores.pos.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}