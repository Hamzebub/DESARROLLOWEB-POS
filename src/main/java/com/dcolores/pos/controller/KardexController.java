package com.dcolores.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.dcolores.pos.repository.KardexRepository;

@Controller
public class KardexController {
	private final KardexRepository kardexRepository;
	
	public KardexController(KardexRepository kardexRepository) {
        this.kardexRepository = kardexRepository;
    }
	
	@GetMapping("/kardex")
    public String mostrarKardex(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("kardexs", kardexRepository.findAllByOrderByIdKardexDesc());
        
        return "kardex";
    }
	
}
