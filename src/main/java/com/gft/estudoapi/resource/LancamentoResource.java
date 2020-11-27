package com.gft.estudoapi.resource;

import com.gft.estudoapi.model.Lancamento;
import com.gft.estudoapi.model.Pessoa;
import com.gft.estudoapi.repository.LancamentoRepository;
import com.gft.estudoapi.repository.PessoaRepository;
import com.gft.estudoapi.repository.filter.LancamentoFilter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Api(tags = "Lançamentos")
@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MessageSource messageSource;

    @ApiOperation("Buscar lancamento")
    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo) {
        Lancamento lancamento = lancamentoRepository.findById(codigo).orElse(null);
        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @ApiOperation("Listar lancamentos")
    @GetMapping
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @ApiOperation("Criar lancamento")
    @PostMapping
    public ResponseEntity<Lancamento> criar(@ApiParam(name = "corpo", value = "Representação de um novo lançamento") @Validated @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Pessoa pessoa = pessoaRepository.getOne(lancamento.getPessoa().getCodigo());

        if (pessoa == null || pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativoException();
        }

        Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{codigo}")
                .buildAndExpand(lancamentoSalvo.getCodigo()).toUri();

        response.setHeader("Location", uri.toASCIIString());

        return ResponseEntity.created(uri).body(lancamentoSalvo);
    }

    @ExceptionHandler({PessoaInexistenteOuInativoException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativoException ex, WebRequest webRequest){
        String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.getCause().toString();
        List<com.gft.estudoapi.exceptionhandler.ExceptionHandler.Erro> erros = Arrays.asList(new com.gft.estudoapi.exceptionhandler.ExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolvedor));

        return ResponseEntity.badRequest().body(erros);
    }

    @ApiOperation("Deletar lancamento")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPeloCodigo(@ApiParam(value = "ID", example = "1") @PathVariable Long codigo) {
        Lancamento lancamentoDeletado = lancamentoRepository.getOne(codigo);
        lancamentoRepository.delete(lancamentoDeletado);
    }
}