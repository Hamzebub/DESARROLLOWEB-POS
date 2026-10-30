package com.dcolores.pos.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dcolores.pos.controller.PosController.VentaMock;
import com.dcolores.pos.model.VentaDashboardDTO;
import com.dcolores.pos.repository.MermaRepository;
import com.dcolores.pos.repository.VentaRepository;
import com.dcolores.pos.service.VentaService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class DashboardController {

	@Autowired(required = false)
    private VentaRepository ventaRepository;

    @Autowired(required = false)
    private MermaRepository mermaRepository;
    
    @Autowired(required = false)
    private VentaService ventaService;
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
        
        
        List<VentaDashboardDTO> listaDTO= ventaService.obtenerHistorialCompleto();
        model.addAttribute("ultimasVentasDTO", listaDTO);
        return "menu";
    }
}
