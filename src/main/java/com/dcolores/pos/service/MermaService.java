package com.dcolores.pos.service;

import com.dcolores.pos.entity.Merma;
import com.dcolores.pos.entity.Producto;
import com.dcolores.pos.repository.MermaRepository;
import com.dcolores.pos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MermaService {

    @Autowired
    private MermaRepository mermaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public Merma registrarMerma(Merma merma) {
        Producto producto = productoRepository.findById(merma.getProducto().getIdProducto())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
        if (producto.getStockActual() < merma.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Descontar stock
        producto.setStockActual(producto.getStockActual() - merma.getCantidad());
        productoRepository.save(producto);

        return mermaRepository.save(merma);
    }
}