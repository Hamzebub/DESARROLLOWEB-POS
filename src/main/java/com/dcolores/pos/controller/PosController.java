package com.dcolores.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class PosController {

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
        // Redirige al menú principal tras iniciar sesión
        return "redirect:/menu"; 
    }

    // 2. VISTA DE MENÚ / DASHBOARD
    @GetMapping("/menu")
    public String mostrarMenu(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("ventasDelDia", "S/ 3,245.50");
        model.addAttribute("productosStock", "1,842");
        model.addAttribute("mermasDelMes", "23 items");
        model.addAttribute("ingresosMes", "S/ 48,720.00");

        List<Venta> ultimasVentas = Arrays.asList(
            new Venta("10:45 AM", "Fideos Don Vittorio x2, Leche Gloria x1", "S/ 18.50", "Yape"),
            new Venta("10:32 AM", "Cuaderno Stanford A4, Lapicero Pilot", "S/ 12.00", "Efectivo"),
            new Venta("10:15 AM", "Arroz Costeño 5kg, Aceite Primor 1L", "S/ 38.90", "Tarjeta"),
            new Venta("09:50 AM", "Gaseosa Inka Kola 3L, Papa Lays Familiar", "S/ 21.50", "Yape")
        );
        model.addAttribute("ultimasVentas", ultimasVentas);

        return "menu";
    }

    // 3. VISTA DE INVENTARIO
    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        
        List<Producto> productos = Arrays.asList(
            new Producto("PROD-001", "Fideos Don Vittorio Corto 250g", "Abarrotes", 45, 10, "S/ 1.80", "S/ 2.50", "OK"),
            new Producto("PROD-002", "Cuaderno Stanford Cuadriculado A4", "Librería", 4, 10, "S/ 4.50", "S/ 6.80", "Bajo"),
            new Producto("PROD-003", "Aceite Vegetal Primor Premium 1L", "Abarrotes", 0, 12, "S/ 9.20", "S/ 12.50", "Agotado"),
            new Producto("PROD-004", "Leche Gloria Azul Lata 400g", "Abarrotes", 8, 24, "S/ 3.20", "S/ 4.20", "Bajo"),
            new Producto("PROD-005", "Pliego de Cartulina Blanca", "Librería", 50, 20, "S/ 0.40", "S/ 0.80", "OK")
        );
        model.addAttribute("productos", productos);
        
        return "inventario";
    }

    // Clases internas para simular los objetos
    public static class Venta {
        private String hora;
        private String producto;
        private String total;
        private String medioPago;

        public Venta(String hora, String producto, String total, String medioPago) {
            this.hora = hora; this.producto = producto; this.total = total; this.medioPago = medioPago;
        }
        public String getHora() { return hora; }
        public String getProducto() { return producto; }
        public String getTotal() { return total; }
        public String getMedioPago() { return medioPago; }
    }

    public static class Producto {
        private String codigo, nombre, categoria, pCompra, pVenta, estado;
        private int stockAct, stockMin;

        public Producto(String codigo, String nombre, String categoria, int stockAct, int stockMin, String pCompra, String pVenta, String estado) {
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
    
 // 3. VISTA DE CATEGORIAS
    @GetMapping("/categorias")
    public String categorias() {
        return "categorias";
    }
    
}