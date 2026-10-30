package com.dcolores.pos.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "kardex_movimientos")
public class Kardex {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_kardex")
    private Long idKardex;

	@ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
    

	@ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    

    // Tipo de movimiento (Ej: 'VENTA', 'COMPRA', 'ENTREGA_INICIAL', 'ANULACION')
	@Column(name = "tipo_movimiento")
    private String tipoMovimiento;

	
    private Integer cantidad;

	@Column(name = "stock_anterior")
    private Integer stockAnterior;

	@Column(name = "stock_posterior")
    private Integer stockPosterior;

	@Column(name = "referencia_origen")
    private String referenciaOrigen;

	@Column(name = "fecha_hora",insertable = false, updatable = false)
    private LocalDateTime fechaHora;

	

	public Long getIdKardex() {
		return idKardex;
	}

	public void setIdKardex(Long idKardex) {
		this.idKardex = idKardex;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getStockAnterior() {
		return stockAnterior;
	}

	public void setStockAnterior(Integer stockAnterior) {
		this.stockAnterior = stockAnterior;
	}

	public Integer getStockPosterior() {
		return stockPosterior;
	}

	public void setStockPosterior(Integer stockPosterior) {
		this.stockPosterior = stockPosterior;
	}

	public String getReferenciaOrigen() {
		return referenciaOrigen;
	}

	public void setReferenciaOrigen(String referenciaOrigen) {
		this.referenciaOrigen = referenciaOrigen;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	
}
