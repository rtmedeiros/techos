
package br.com.bugnacio.techos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bugnacio.techos.model.Equipamento;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // Pesquisa equipamentos pela marca ou modelo.
    List<Equipamento> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(
            String marca, String modelo);

    // Verifica se o número de série já existe.
    boolean existsByNumeroSerie(String numeroSerie);

    // Verifica se outro equipamento possui o mesmo número de série.
    boolean existsByNumeroSerieAndIdNot(String numeroSerie, Long id);

    // Verifica se existem equipamentos associados a um cliente.
    boolean existsByCliente_Id(Long id);
}
