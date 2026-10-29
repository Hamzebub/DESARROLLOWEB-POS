package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Categoria;
import com.dcolores.pos.entity.Producto;
import com.dcolores.pos.repository.CategoriaRepository;
import com.dcolores.pos.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository,
                              CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Muestra el formulario y la lista de productos
    @GetMapping("/productos")
    public String mostrarProductos(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("nuevoProducto", new Producto());
        //model.addAttribute("productos", productoRepository.findAll());
        //model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos";
    }

    // Guarda el producto
    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @ModelAttribute("nuevoProducto") Producto producto,
            @RequestParam("idCategoria") Integer idCategoria) {

        Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);

        producto.setIdProducto(null);
        producto.setCategoria(categoria);
        producto.setActivo(true);
        if (producto.getStockActual() == null) producto.setStockActual(0);
        if (producto.getStockMinimo() == null) producto.setStockMinimo(0);

        //productoRepository.save(producto);
        return "redirect:/productos";
    }
}
