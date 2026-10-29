package com.dcolores.pos.repository;

import com.dcolores.pos.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    // Consulta para el Dashboard: Total de ventas del día
    @Query(value = "SELECT COALESCE(SUM(total), 0) FROM ventas WHERE DATE(fecha_hora) = CURRENT_DATE AND estado = 'Completado'", nativeQuery = true)
    Double obtenerTotalVentasDelDia();

    // Consulta para el Dashboard: Últimas ventas
    @Query(value = "SELECT * FROM ventas ORDER BY fecha_hora DESC LIMIT 5", nativeQuery = true)
    List<Venta> obtenerUltimasVentas();
}