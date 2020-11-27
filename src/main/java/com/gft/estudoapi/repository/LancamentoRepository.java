package com.gft.estudoapi.repository;

import com.gft.estudoapi.model.Lancamento;
import com.gft.estudoapi.repository.lancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
}
