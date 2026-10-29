package com.dcolores.pos.repository;

import com.dcolores.pos.entity.Merma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MermaRepository extends JpaRepository<Merma, Long> {
}