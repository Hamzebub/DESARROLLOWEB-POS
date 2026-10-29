package com.dcolores.pos.controller;

import com.dcolores.pos.entity.Categoria;
import com.dcolores.pos.entity.Producto;
import com.dcolores.pos.repository.CategoriaRepository;
import com.dcolores.pos.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Muestra el formulario vacío y la lista de productos
    @GetMapping("/productos")
    public String mostrarProductos(Model model) {
        cargarDatos(model, new Producto(), false);
        return "productos";
    }

    // Muestra el formulario lleno con el producto a editar
    @GetMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            return "redirect:/productos";
        }
        cargarDatos(model, producto, true);
        return "productos";
    }

    // Guarda un producto nuevo o actualiza uno existente
    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @ModelAttribute("nuevoProducto") Producto producto,
            @RequestParam("idCategoria") Integer idCategoria) {

        Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);

        // Si idProducto viene con valor, save() actualiza; si es null, inserta
        producto.setCategoria(categoria);
        if (producto.getActivo() == null) producto.setActivo(true);
        if (producto.getStockActual() == null) producto.setStockActual(0);
        if (producto.getStockMinimo() == null) producto.setStockMinimo(0);

        productoRepository.save(producto);
        return "redirect:/productos";
    }

    private void cargarDatos(Model model, Producto producto, boolean editando) {
        model.addAttribute("usuario", "Sra. Gloria");
        model.addAttribute("nuevoProducto", producto);
        model.addAttribute("editando", editando);
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
    }
}