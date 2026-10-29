package com.dcolores.pos.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "kardex_movimientos")
public class Kardex {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_kardex;

    
    private Long id_producto;

    
    private Long id_usuario;

    // Tipo de movimiento (Ej: 'VENTA', 'COMPRA', 'ENTREGA_INICIAL', 'ANULACION')
    private String tipo_movimiento;

    
    private Integer cantidad;

    
    private Integer stock_anterior;

    
    private Integer stock_posterior;

    
    private String referencia_origen;

    
    private LocalDateTime fecha_hora;
}
