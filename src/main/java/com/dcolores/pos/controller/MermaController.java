package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Merma;
import com.dcolores.pos.repository.MermaRepository;
import com.dcolores.pos.repository.ProductoRepository;
import com.dcolores.pos.service.MermaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MermaController {

    @Autowired
    private MermaRepository mermaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MermaService mermaService;

    @GetMapping({"/merma", "/mermas"})
    public String mostrarMermas(Model model) {
        model.addAttribute("mermas", mermaRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("nuevaMerma", new Merma());
        return "merma";
    }

    @PostMapping("/merma/guardar")
    public String guardarMerma(Merma merma) {
        mermaService.registrarMerma(merma);
        return "redirect:/merma";
    }
}