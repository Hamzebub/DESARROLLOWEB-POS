package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Categoria;
import com.dcolores.pos.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Muestra el formulario y la lista desde PostgreSQL
    @GetMapping("/categorias")
    public String mostrarCategorias(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("nuevaCategoria", new Categoria());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias";
    }

    // Guarda la categoría en PostgreSQL
    @PostMapping("/categorias/guardar")
    public String guardarCategoria(
            @ModelAttribute("nuevaCategoria") Categoria categoria) {

        // El ID se genera automáticamente al guardar.
        categoria.setIdCategoria(null);
        categoria.setActivo(true);
        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }
    
}