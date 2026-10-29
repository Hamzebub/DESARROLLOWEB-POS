package com.dcolores.pos.model;

import java.math.BigDecimal;
import java.util.List;

public class VentaRequestDTO {
	private VentaDTO ventas;
    private List<DetalleVentaDTO> detalles_venta;
    private List<PagoDTO> pagos;

    // Getters y Setters
    public VentaDTO getVentas() { return ventas; }
    public void setVentas(VentaDTO ventas) { this.ventas = ventas; }
    public List<DetalleVentaDTO> getDetalles_venta() { return detalles_venta; }
    public void setDetalles_venta(List<DetalleVentaDTO> detalles_venta) { this.detalles_venta = detalles_venta; }
    public List<PagoDTO> getPagos() { return pagos; }
    public void setPagos(List<PagoDTO> pagos) { this.pagos = pagos; }

	
	public static class VentaDTO {
        private Long id_usuario;
        private Long id_caja;
        private String numero_comprobante;
        private String tipo_comprobante;
        private BigDecimal subtotal;
        private BigDecimal igv;
        private BigDecimal total;
        private String estado;
        
        // Getters y Setters aquí...
        public Long getId_usuario() { return id_usuario; }
        public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }
        public Long getId_caja() { return id_caja; }
        public void setId_caja(Long id_caja) { this.id_caja = id_caja; }
        public String getNumero_comprobante() { return numero_comprobante; }
        public void setNumero_comprobante(String numero_comprobante) { this.numero_comprobante = numero_comprobante; }
        public String getTipo_comprobante() { return tipo_comprobante; }
        public void setTipo_comprobante(String tipo_comprobante) { this.tipo_comprobante = tipo_comprobante; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
        public BigDecimal getIgv() { return igv; }
        public void setIgv(BigDecimal igv) { this.igv = igv; }
        public BigDecimal getTotal() { return total; }
        public void setTotal(BigDecimal total) { this.total = total; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
	
	public static class DetalleVentaDTO {
        private Long id_producto;
        private Integer cantidad;
        private BigDecimal precio_unitario;
        private BigDecimal subtotal;
        
        // Getters y Setters
        public Long getId_producto() { return id_producto; }
        public void setId_producto(Long id_producto) { this.id_producto = id_producto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecio_unitario() { return precio_unitario; }
        public void setPrecio_unitario(BigDecimal precio_unitario) { this.precio_unitario = precio_unitario; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }
	
	public static class PagoDTO {
        private String metodo_pago;
        private BigDecimal monto;
        private String referencia_transaccion;
        private String estado;
        
        // Getters y Setters
        public String getMetodo_pago() { return metodo_pago; }
        public void setMetodo_pago(String metodo_pago) { this.metodo_pago = metodo_pago; }
        public BigDecimal getMonto() { return monto; }
        public void setMonto(BigDecimal monto) { this.monto = monto; }
        public String getReferencia_transaccion() { return referencia_transaccion; }
        public void setReferencia_transaccion(String referencia_transaccion) { this.referencia_transaccion = referencia_transaccion; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}
