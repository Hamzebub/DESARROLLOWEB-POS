package com.dcolores.pos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dcolores.pos.entity.Caja;

public interface CajaRepository extends JpaRepository<Caja, Integer>{

	@Query("SELECT c FROM Caja c WHERE c.usuario.id_usuario = :idUsuario AND c.estado = :estado")
    Optional<Caja> findCajaAbiertaPorUsuario(
        @Param("idUsuario") Long idUsuario, 
        @Param("estado") String estado
    );
}
