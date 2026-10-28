package com.dcolores.pos.controller;

//import com.dcolores.pos.repository.CategoriaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CategoriaController {

    //private final CategoriaRepository categoriaRepository;

    //public CategoriaController(CategoriaRepository categoriaRepository) {
    //    this.categoriaRepository = categoriaRepository;
    //}

    // Muestra el formulario y la lista desde PostgreSQL
    @GetMapping("/categorias")
    public String mostrarCategorias(Model model) {
        model.addAttribute("usuario", "Sra. Gloria");
        //model.addAttribute("nuevaCategoria", new Categoria());
        //model.addAttribute("categorias", categoriaRepository.findAll());
        return "categorias";
    }

    // Guarda la categoría en PostgreSQL
    //@PostMapping("/categorias/guardar")
    //public String guardarCategoria(
    //        @ModelAttribute("nuevaCategoria") Categoria categoria) {

        // El ID se genera automáticamente al guardar.
    //    categoria.setIdCategoria(null);
    //    categoriaRepository.save(categoria);
    //    return "redirect:/categorias";
    //}
    
    public static class Categoria {
        private int id_categoria;
        private String nombre;
        private String descripcion;
        private boolean activo;

        public Categoria(int id_categoria, String nombre, String descripcion, boolean activo) {
            this.id_categoria = id_categoria;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.activo = activo;
        }

        public int getId_categoria() { return id_categoria; }
        public String getNombre() {  return nombre; }
        public String getDescripcion() { return descripcion; }
        public boolean isActivo() { return activo; }
    }
}