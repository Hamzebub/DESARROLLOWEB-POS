package com.dcolores.pos.controller;

import com.dcolores.pos.repository.VentaRepository;
import com.dcolores.pos.repository.MermaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dcolores.pos.repository.CategoriaRepository;
import com.dcolores.pos.repository.ProductoRepository;

import java.util.Arrays;
import java.util.List;

@Controller
public class PosController {
	private final CategoriaRepository categoriaRepository;
	private final ProductoRepository productoRepository;
	public PosController(CategoriaRepository categoriaRepository,ProductoRepository productorepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository= productorepository;
    }

    @Autowired(required = false)
    private VentaRepository ventaRepository;

    @Autowired(required = false)
    private MermaRepository mermaRepository;

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
        return "ventapos";
    }

    // 2. VISTA DE MENÚ / DASHBOARD (TU MÓDULO 8)
    @GetMapping("/menu")
    public String mostrarMenu(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");

        // Obtenemos datos reales desde la BD si existen, si no, valores base
        Double ventasHoy = (ventaRepository != null) ? ventaRepository.obtenerTotalVentasDelDia() : 0.0;
        long totalMermas = (mermaRepository != null) ? mermaRepository.count() : 0;

        model.addAttribute("ventasDelDia", "S/ " + (ventasHoy != null ? String.format("%.2f", ventasHoy) : "0.00"));
        model.addAttribute("productosStock", "1,842"); // Lo maneja el módulo de Productos
        model.addAttribute("mermasDelMes", totalMermas + " items");
        model.addAttribute("ingresosMes", "S/ 48,720.00");

        // Lista de últimas ventas para la tabla inferior del Dashboard
        List<VentaMock> ultimasVentas = Arrays.asList(
            new VentaMock("10:45 AM", "Fideos Don Vittorio x2, Leche Gloria x1", "S/ 18.50", "Yape"),
            new VentaMock("10:32 AM", "Cuaderno Stanford A4, Lapicero Pilot", "S/ 12.00", "Efectivo"),
            new VentaMock("10:15 AM", "Arroz Costeño 5kg, Aceite Primor 1L", "S/ 38.90", "Tarjeta"),
            new VentaMock("09:50 AM", "Gaseosa Inka Kola 3L, Papa Lays Familiar", "S/ 21.50", "Yape")
        );
        model.addAttribute("ultimasVentas", ultimasVentas);

        return "menu";
    }

    // 3. VISTA DE INVENTARIO (De tus compañeros)
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
    }

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