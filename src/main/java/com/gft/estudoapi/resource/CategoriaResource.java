package com.gft.estudoapi.resource;

import com.gft.estudoapi.model.Categoria;
import com.gft.estudoapi.model.Pessoa;
import com.gft.estudoapi.repository.CategoriaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@Api(tags = "Categorias")
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @ApiOperation("Listar categorias")
    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @ApiOperation("Criar uma categoria")
    @PostMapping
    public ResponseEntity<Categoria> criar(@ApiParam(name = "corpo", value = "Representação de uma nova categoria") @Validated @RequestBody Categoria categoria, HttpServletResponse response) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(categoriaSalva.getCodigo()).toUri();

        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(categoriaSalva);
    }

    @ApiOperation("Buscar uma categoria")
    @GetMapping("/{codigo}")
    public Categoria buscarPeloCodigo(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo) {
        return categoriaRepository.findById(codigo).orElse(null);
    }

    @ApiOperation("Deletar uma categoria")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPeloCodigo(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo) {
        Categoria categoriaDeletada = categoriaRepository.getOne(codigo);
        categoriaRepository.delete(categoriaDeletada);
    }

}
