package com.dcolores.pos.entity;

import java.math.BigDecimal;

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
    private Integer id_pago;
	
	private Integer id_venta;
	
	public Integer getId_pago() {
		return id_pago;
	}
	public void setId_pago(Integer id_pago) {
		this.id_pago = id_pago;
	}
	public Integer getId_venta() {
		return id_venta;
	}
	public void setId_venta(Integer id_venta) {
		this.id_venta = id_venta;
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
