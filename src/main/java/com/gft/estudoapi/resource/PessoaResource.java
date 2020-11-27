package com.gft.estudoapi.resource;

import com.gft.estudoapi.model.Categoria;
import com.gft.estudoapi.model.Pessoa;
import com.gft.estudoapi.repository.PessoaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@Api(tags = "Pessoas")
@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private PessoaRepository pessoaRepository;

    @ApiOperation("Criar uma pessoa")
    @PostMapping
    public ResponseEntity<Pessoa> criar(@ApiParam(name = "corpo", value = "Representação de uma nova pessoa") @Validated @RequestBody Pessoa pessoa, HttpServletResponse httpServletResponse){
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(pessoaSalva.getCodigo()).toUri();
        httpServletResponse.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(pessoaSalva);
    }

    @ApiOperation("Buscar uma pessoa")
    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPeloCodigo(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo) {
        Pessoa pessoa = pessoaRepository.findById(codigo).orElse(null);
        return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
    }

    @ApiOperation("Listar pessoas")
    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @ApiOperation("Deletar uma pessoa")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPeloCodigo(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo) {
        Pessoa pessoaDeletada = pessoaRepository.getOne(codigo);
        pessoaRepository.delete(pessoaDeletada);
    }

    @ApiOperation("Atualizar uma pessoa")
    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo, @ApiParam(name = "corpo", value = "Representação de uma nova pessoa") @Validated @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaRepository.getOne(codigo);
        if (pessoaSalva == null) {
            throw new EmptyResultDataAccessException(1);
        }
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        pessoaRepository.save(pessoaSalva);
        return ResponseEntity.ok(pessoaSalva);
    }
}