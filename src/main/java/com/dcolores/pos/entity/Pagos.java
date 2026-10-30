package com.dcolores.pos.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagos")
public class Pagos {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pago;
	
	@Column(name = "id_venta")
	private Long idVenta;
	
	public Long getId_pago() {
		return id_pago;
	}
	public void setId_pago(Long id_pago) {
		this.id_pago = id_pago;
	}
	
	public Long getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}
	public String getMetodo_pago() {
		return metodo_pago;
	}
	public void setMetodo_pago(String metodo_pago) {
		this.metodo_pago = metodo_pago;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getReferencia_transaccion() {
		return referencia_transaccion;
	}
	public void setReferencia_transaccion(String referencia_transaccion) {
		this.referencia_transaccion = referencia_transaccion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	private String metodo_pago;
    private BigDecimal monto;
    private String referencia_transaccion;
    private String estado;
    
}
