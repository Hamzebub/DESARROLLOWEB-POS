package com.dcolores.pos.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;
	
	@Column(name = "id_Venta")
	private Long idVenta;
	@ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
		
    private Integer cantidad;
    private BigDecimal precio_unitario;
    private BigDecimal subtotal;
	public Integer getId_detalle() {
		return id_detalle;
	}
	public void setId_detalle(Integer id_detalle) {
		this.id_detalle = id_detalle;
	}
	
	
	public Long getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecio_unitario() {
		return precio_unitario;
	}
	public void setPrecio_unitario(BigDecimal precio_unitario) {
		this.precio_unitario = precio_unitario;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
    
    
}
