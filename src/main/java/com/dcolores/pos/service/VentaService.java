package com.dcolores.pos.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;

import com.dcolores.pos.entity.*;
import com.dcolores.pos.model.VentaRequestDTO;
import com.dcolores.pos.repository.*;

import jakarta.transaction.Transactional;

@Service
public class VentaService {

	private final KardexRepository kardexRepository;
	private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, 
                        DetalleVentaRepository detalleVentaRepository, 
                        PagoRepository pagoRepository,
                        ProductoRepository productoRepository, KardexRepository kardexRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.pagoRepository = pagoRepository;
        this.productoRepository = productoRepository;
		this.kardexRepository = kardexRepository;
    }
    
    @Transactional
    public String generarSiguienteNumeroComprobante(String serieComprobante) {
    		// Valor por defecto si es la primera vez que se usa la serie
    		int siguienteCorrelativo = 1; 

        // 1. Buscar la última venta registrada con esa serie exacta
        Optional<Venta> ultimaVentaOpt = ventaRepository.findFirstBySerieComprobanteOrderByIdVentaDesc(serieComprobante);

        if (ultimaVentaOpt.isPresent()) {
            Venta ultima = ultimaVentaOpt.get();
            
            if (ultima.getNumeroComprobante() != null) {
                try {
                    // Extraer el valor numérico actual y sumarle 1
                    int numeroActual = Integer.parseInt(ultima.getNumeroComprobante());
                    siguienteCorrelativo = numeroActual + 1;
                } catch (NumberFormatException e) {
                    siguienteCorrelativo = 1; // Fallback por seguridad
                }
            }
        }

        // 2. Formatear a 8 dígitos con ceros a la izquierda (Ej: 1 -> 00000001)
        return String.format("%08d", siguienteCorrelativo);
    }
    
    @Transactional // <--- ¡Vital! Si algo falla, hace rollback automático de todo.
    public void registrarVentaCompleta(VentaRequestDTO dto) {
        
    		String numeroComprobante=generarSiguienteNumeroComprobante(dto.getVentas().getSerie_comprobante());
        // 1. Guardar la Cabecera de la Venta
        Venta venta = new Venta();
        venta.setUsuario(new Usuario());
        venta.getUsuario().setId_usuario(dto.getVentas().getId_usuario());
        venta.setId_caja(dto.getVentas().getId_caja());
        venta.setSerieComprobante(dto.getVentas().getSerie_comprobante());
        venta.setNumeroComprobante(numeroComprobante);
        venta.setTipoComprobante(dto.getVentas().getTipo_comprobante());
        venta.setSubtotal(dto.getVentas().getSubtotal());
        venta.setIgv(dto.getVentas().getIgv());
        venta.setTotal(dto.getVentas().getTotal());
        venta.setEstado(dto.getVentas().getEstado());
        
        Venta ventaGuardada = ventaRepository.save(venta); // Se genera el ID de la venta (BIGSERIAL)
        String comprobante = venta.getSerieComprobante()+"-"+ venta.getNumeroComprobante();

        // 2. Guardar los Detalles de Venta y descontar Stock
        for (VentaRequestDTO.DetalleVentaDTO detDTO : dto.getDetalles_venta()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setId_Venta(ventaGuardada.getIdVenta()); // Relación con la venta creada
            detalle.setProducto(new Producto());
            detalle.getProducto().setIdProducto(detDTO.getId_producto());
            detalle.setCantidad(detDTO.getCantidad());
            detalle.setPrecio_unitario(detDTO.getPrecio_unitario());
            detalle.setSubtotal(detDTO.getSubtotal());
            
            detalleVentaRepository.save(detalle);

            // obtner producto
            Producto producto = productoRepository.findById(detDTO.getId_producto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detDTO.getId_producto()));
            
            //actualizar stock
            int stockAnterior = producto.getStockActual();
            int nuevoStock = stockAnterior - detDTO.getCantidad();
            producto.setStockActual(nuevoStock);
            productoRepository.save(producto);
            
            //actualizar kardex
            Kardex kardex = new Kardex();
            Producto p = new Producto();
            p.setIdProducto(producto.getIdProducto());
            kardex.setProducto(p);
            
            Usuario u = new Usuario();
            u.setId_usuario(dto.getVentas().getId_usuario());
            kardex.setUsuario(u);// Cajero que hizo la venta
            kardex.setTipoMovimiento("VENTA"); // O 'SALIDA_VENTA'
            kardex.setCantidad(detDTO.getCantidad());
            kardex.setStockAnterior(stockAnterior);
            kardex.setStockPosterior(nuevoStock);
            kardex.setReferenciaOrigen("Venta de comprobante: " + comprobante);
            kardexRepository.save(kardex);
            
        }

        // 3. Guardar los Pagos (Soporte Multi-Pago: Efectivo, Yape, Tarjeta)
        for (VentaRequestDTO.PagoDTO pagoDTO : dto.getPagos()) {
            Pagos pago = new Pagos();
            pago.setId_venta(ventaGuardada.getIdVenta()); // Relación con la venta creada
            pago.setMetodo_pago(pagoDTO.getMetodo_pago());
            pago.setMonto(pagoDTO.getMonto());
            pago.setReferencia_transaccion(comprobante);
            pago.setEstado(pagoDTO.getEstado());
            
            pagoRepository.save(pago);
        }
    }
    
}
