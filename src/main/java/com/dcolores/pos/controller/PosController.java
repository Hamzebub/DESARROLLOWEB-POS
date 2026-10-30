package com.dcolores.pos.controller;

import com.dcolores.pos.repository.VentaRepository;
import com.dcolores.pos.service.VentaService;
import com.dcolores.pos.repository.MermaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dcolores.pos.entity.Caja;
import com.dcolores.pos.entity.Venta;
import com.dcolores.pos.model.VentaRequestDTO;
import com.dcolores.pos.repository.CajaRepository;
import com.dcolores.pos.repository.CategoriaRepository;
import com.dcolores.pos.repository.ProductoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PosController {
	private final CategoriaRepository categoriaRepository;
	private final ProductoRepository productoRepository;
	private final CajaRepository cajaRepository;
	private final VentaService ventaService;
	public PosController(CategoriaRepository categoriaRepository,ProductoRepository productorepository
			,CajaRepository cajarepository
			,VentaService ventaService
			) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository= productorepository;
        this.cajaRepository = cajarepository;
        this.ventaService= ventaService;
    }

    

    @GetMapping("/")
    public String redirigir() {
        return "redirect:/login";
    }

    // 1. VISTA DE LOGIN
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin() {
        return "redirect:/menu"; 
    }
    
    @GetMapping("/ventas")
    public String mostrarMenuVentas(Model model) {
    		model.addAttribute("categorias", categoriaRepository.findAll());
    		model.addAttribute("productos", productoRepository.findAll());
    		Long idUsuario= 5L;
    		Optional<Caja> c = cajaRepository.findCajaAbiertaPorUsuario(idUsuario, "ABIERTA");
    		model.addAttribute("caja", c);
    		   		
    		
        return "ventapos";
    }
    
    @PostMapping("/ventas/guardar")
    @ResponseBody // <--- ¡La clave! Esto le dice a Spring que devuelva JSON puro y no busque una vista HTML
    public ResponseEntity<?> guardarVenta(@RequestBody VentaRequestDTO request) {
        try {
            ventaService.registrarVentaCompleta(request);
            return ResponseEntity.ok().body("{\"mensaje\": \"¡Venta registrada y stock actualizado con éxito!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    

    // 3. VISTA DE INVENTARIO (De tus compañeros)
    /*
    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        
        List<ProductoMock> productos = Arrays.asList(
            new ProductoMock("PROD-001", "Fideos Don Vittorio Corto 250g", "Abarrotes", 45, 10, "S/ 1.80", "S/ 2.50", "OK"),
            new ProductoMock("PROD-002", "Cuaderno Stanford Cuadriculado A4", "Librería", 4, 10, "S/ 4.50", "S/ 6.80", "Bajo"),
            new ProductoMock("PROD-003", "Aceite Vegetal Primor Premium 1L", "Abarrotes", 0, 12, "S/ 9.20", "S/ 12.50", "Agotado")
        );
        model.addAttribute("productos", productos);
        return "inventario";
    }*/

    // Renombramos estas clases internas a "Mock" para que NO colisionen con tu Entity Venta.java
    public static class VentaMock {
        private String hora;
        private String producto;
        private String total;
        private String medioPago;

        public VentaMock(String hora, String producto, String total, String medioPago) {
            this.hora = hora; this.producto = producto; this.total = total; this.medioPago = medioPago;
        }
        public String getHora() { return hora; }
        public String getProducto() { return producto; }
        public String getTotal() { return total; }
        public String getMedioPago() { return medioPago; }
    }

    public static class ProductoMock {
        private String codigo, nombre, categoria, pCompra, pVenta, estado;
        private int stockAct, stockMin;

        public ProductoMock(String codigo, String nombre, String categoria, int stockAct, int stockMin, String pCompra, String pVenta, String estado) {
            this.codigo = codigo; this.nombre = nombre; this.categoria = categoria;
            this.stockAct = stockAct; this.stockMin = stockMin; 
            this.pCompra = pCompra; this.pVenta = pVenta; this.estado = estado;
        }
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
        public String getCategoria() { return categoria; }
        public int getStockAct() { return stockAct; }
        public int getStockMin() { return stockMin; }
        public String getPCompra() { return pCompra; }
        public String getPVenta() { return pVenta; }
        public String getEstado() { return estado; }
    }
}