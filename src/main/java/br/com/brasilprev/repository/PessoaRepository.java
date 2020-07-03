package br.com.brasilprev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.brasilprev.modelo.Pessoa;
import br.com.brasilprev.repository.helper.PessoaRepositoryQueries;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQueries {

	@Query("SELECT p FROM Pessoa p JOIN p.telefones t WHERE t.ddd = :ddd AND t.numero = :numero")
	Optional<Pessoa> findByTelefoneDddAndTelefoneNumero(@Param("ddd") String ddd, @Param("numero") String numero);

	@Query("SELECT p FROM Pessoa p WHERE p.cpf = :cpf")
	Optional<Pessoa> findByCpf(@Param("cpf") String cpf);
}
