package com.dcolores.pos.config;

import com.dcolores.pos.entity.Usuario;
import com.dcolores.pos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*@Component*/
public class DataInitializer /*implements CommandLineRunner*/ {
	/*
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }
    

    @Override
    public void run(String... args) {
    		
        if (repo.findByUsername("admin").isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername("admin");
            u.setPassword(encoder.encode("admin123"));
            u.setRol("ADMIN");
            //repo.save(u);
        }
        
    }
    */
}
