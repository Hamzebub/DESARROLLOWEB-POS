package com.dcolores.pos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaDashboardDTO {
	private Long idVenta;
    private String serieComprobante;
    private String numeroComprobante;
    private LocalDateTime fechaHora;
    private BigDecimal total;
    private String estado;
    private String productosConcatenados;
    private List<DetalleDItemDTO> detalles;
    private List<PagoDItemDTO> pagos;
    
 // Sub-DTO para los productos
    public static class DetalleDItemDTO {
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal subtotal;
        
        public DetalleDItemDTO(String nombreProducto, Integer cantidad, BigDecimal subtotal) {
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
        }
        // Getters
        public String getNombreProducto() { return nombreProducto; }
        public Integer getCantidad() { return cantidad; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
    
    public static class PagoDItemDTO {
        private String metodoPago;
        private BigDecimal monto;

        public PagoDItemDTO(String metodoPago, BigDecimal monto) {
            this.metodoPago = metodoPago;
            this.monto = monto;
        }
        // Getters
        public String getMetodoPago() { return metodoPago; }
        public BigDecimal getMonto() { return monto; }
    }

	public Long getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Long idVenta) {
		this.idVenta = idVenta;
	}

	public String getSerieComprobante() {
		return serieComprobante;
	}

	public void setSerieComprobante(String serieComprobante) {
		this.serieComprobante = serieComprobante;
	}

	public String getNumeroComprobante() {
		return numeroComprobante;
	}

	public void setNumeroComprobante(String numeroComprobante) {
		this.numeroComprobante = numeroComprobante;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<DetalleDItemDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleDItemDTO> detalles) {
		this.detalles = detalles;
	}

	public List<PagoDItemDTO> getPagos() {
		return pagos;
	}

	public void setPagos(List<PagoDItemDTO> pagos) {
		this.pagos = pagos;
	}

	public String getProductosConcatenados() {
		return productosConcatenados;
	}

	public void setProductosConcatenados(String productosConcatenados) {
		this.productosConcatenados = productosConcatenados;
	}
	
	
	
    
    
}
