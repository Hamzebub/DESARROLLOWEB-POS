package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Categoria;
import com.dcolores.pos.repository.CategoriaRepository;
import com.dcolores.pos.repository.ProductoRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaController(CategoriaRepository categoriaRepository,ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    // Muestra el formulario y la lista desde PostgreSQL
    @GetMapping("/categorias")
    public String mostrarCategorias(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("nuevaCategoria", new Categoria());
        model.addAttribute("categorias", categoriaRepository.findAllByOrderByIdCategoriaAsc());
        return "categorias";
    }

    // Guarda la categoría en PostgreSQL
    @PostMapping("/categorias/guardar")
    public String guardarCategoria(
            @ModelAttribute("nuevaCategoria") Categoria categoria) {

        // El ID se genera automáticamente al guardar.
        //categoria.setIdCategoria(null);
        if (categoria.getActivo() == null) {
            categoria.setActivo(true);
        }

        categoriaRepository.save(categoria);
        return "redirect:/categorias";
    }
    
    @GetMapping("/categorias/editar/{id}")
    public String cargarCategoriaParaEditar(@PathVariable Integer id, Model model) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe la categoría con ID " + id));

        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("nuevaCategoria", categoria);
        model.addAttribute("categorias", categoriaRepository.findAllByOrderByIdCategoriaAsc());
        model.addAttribute("editando", true);

        return "categorias";
    }
    
    /*@PostMapping("/categorias/estado/{id}")
    public String cambiarEstadoCategoria(@PathVariable Integer id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        categoria.setActivo(!Boolean.TRUE.equals(categoria.getActivo()));
        categoriaRepository.save(categoria);

        return "redirect:/categorias";
    }*/
    @PostMapping("/categorias/estado/{id}")
    public String cambiarEstadoCategoria(@PathVariable Integer id,
    										RedirectAttributes redirectAttributes) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Categoría no encontrada"));

        // Bloquear solo cuando se intenta desactivar
        if (Boolean.TRUE.equals(categoria.getActivo())) {
            long cantidadProductos =
                    productoRepository.countByCategoria_IdCategoria(id);

            if (cantidadProductos > 0) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "No se puede desactivar esta categoría porque tiene "
                        + cantidadProductos + " producto(s) asociado(s)."
                );
                return "redirect:/categorias";
            }
        }

        categoria.setActivo(!Boolean.TRUE.equals(categoria.getActivo()));
        categoriaRepository.save(categoria);

        redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado.");
        return "redirect:/categorias";
    }
    
}