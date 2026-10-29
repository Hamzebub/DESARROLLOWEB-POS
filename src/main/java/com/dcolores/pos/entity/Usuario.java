package com.dcolores.pos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(nullable = false)
    private String correo;
    
    @Column(nullable = false)
    private String password_hash;

    @Column(nullable = false)
    private String rol; // ADMIN, CAJERO, GERENTE
    
    @Column(nullable = false)
    private String telefono;
    
    private Boolean activo;


    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long id) { this.id_usuario = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getPassword_hash() {
		return password_hash;
	}
	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
    
    
    
    
}
