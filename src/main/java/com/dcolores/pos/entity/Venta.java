package com.dcolores.pos.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    private Long id_caja;
    
	@Column(name = "serie_comprobante")
    private String serieComprobante;
    @Column(name = "numero_comprobante")
    private String numeroComprobante;
    
    @Column(name = "tipo_comprobante")
    private String tipoComprobante;

    @Column(name = "fecha_hora", insertable = false, updatable = false)
    private LocalDateTime fechaHora;

    private BigDecimal total;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private String estado;
	public Long getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(Long id_venta) {
		this.idVenta = id_venta;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Long getId_caja() {
		return id_caja;
	}
	public void setId_caja(Long id_caja) {
		this.id_caja = id_caja;
	}
	public String getNumeroComprobante() {
		return numeroComprobante;
	}
	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
	}
	public String getTipoComprobante() {
		return tipoComprobante;
	}
	public void setTipoComprobante(String tipo_comprobante) {
		this.tipoComprobante = tipo_comprobante;
	}
	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getIgv() {
		return igv;
	}
	public void setIgv(BigDecimal igv) {
		this.igv = igv;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

    public String getSerieComprobante() {
		return serieComprobante;
	}
	public void setSerieComprobante(String serieComprobante) {
		this.serieComprobante = serieComprobante;
	}

    
    
    
}