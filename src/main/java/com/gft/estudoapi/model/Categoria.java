package com.gft.estudoapi.model;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.Objects;

@ApiModel(value = "Categoria", description = "Representa uma categoria")
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @NotNull
    private String nome;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return codigo.equals(categoria.codigo) &&
                nome.equals(categoria.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nome);
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
