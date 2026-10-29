package com.dcolores.pos.controller;

import com.dcolores.pos.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @GetMapping("/listaventa")
    public String mostrarListaVentas(Model model) {
        model.addAttribute("ventas", ventaRepository.findAll());
        return "listaventa";
    }
}