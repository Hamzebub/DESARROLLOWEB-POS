package com.dcolores.pos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cajas_sesiones")
public class Caja {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_caja")
    private Long idCaja;
	
	@ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
	
	@Column(name = "fecha_apertura", insertable = false, updatable = false)
    private LocalDateTime fechaApertura;
	
	@Column(name = "fecha_cierre", insertable = false, updatable = false)
    private LocalDateTime fechaCierre;
	
	@Column(name = "monto_inicial")
	private BigDecimal montoInicial;
	
	@Column(name = "ingresos_efectivo")
	private BigDecimal ingresosEfectivo;
	
	@Column(name = "ingresos_digitales")
	private BigDecimal ingresosDigitales;
	
	@Column(name = "ingresos_tarjeta")
	private BigDecimal ingresosTarjeta;
	
	@Column(name = "monto_esperado")
	private BigDecimal montoEsperado;
	@Column(name = "monto_real_contado")
	private BigDecimal montoRealContado;
	
	private BigDecimal diferencia;
	private String estado;
	private String observaciones;
	public Long getIdCaja() {
		return idCaja;
	}
	public void setIdCaja(Long idCaja) {
		this.idCaja = idCaja;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public LocalDateTime getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(LocalDateTime fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public LocalDateTime getFechaCierre() {
		return fechaCierre;
	}
	public void setFechaCierre(LocalDateTime fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	public BigDecimal getIngresosEfectivo() {
		return ingresosEfectivo;
	}
	public void setIngresosEfectivo(BigDecimal ingresosEfectivo) {
		this.ingresosEfectivo = ingresosEfectivo;
	}
	public BigDecimal getIngresosDigitales() {
		return ingresosDigitales;
	}
	public void setIngresosDigitales(BigDecimal ingresosDigitales) {
		this.ingresosDigitales = ingresosDigitales;
	}
	public BigDecimal getIngresosTarjeta() {
		return ingresosTarjeta;
	}
	public void setIngresosTarjeta(BigDecimal ingresosTarjeta) {
		this.ingresosTarjeta = ingresosTarjeta;
	}
	public BigDecimal getMontoEsperado() {
		return montoEsperado;
	}
	public void setMontoEsperado(BigDecimal montoEsperado) {
		this.montoEsperado = montoEsperado;
	}
	public BigDecimal getMontoRealContado() {
		return montoRealContado;
	}
	public void setMontoRealContado(BigDecimal montoRealContado) {
		this.montoRealContado = montoRealContado;
	}
	public BigDecimal getDiferencia() {
		return diferencia;
	}
	public void setDiferencia(BigDecimal diferencia) {
		this.diferencia = diferencia;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	
	
	
	
	

}
