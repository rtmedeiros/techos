package br.com.bugnacio.techos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bugnacio.techos.model.Equipamento;

// Pacote repository: classes que acessam o banco de dados.
//
// É uma INTERFACE: só dizemos O QUE queremos. O Spring Data JPA escreve o código
// (o COMO) sozinho quando o sistema inicia.
//
// extends JpaRepository<Equipamento, Long>:
//   Equipamento = entidade que este repositório gerencia
//   Long        = tipo do id dessa entidade
//
// Métodos prontos herdados, sem escrever nada:
//   save(obj)      INSERT (id nulo) ou UPDATE (id existente)
//   findAll()      SELECT de todos (aceita Sort para ordenar)
//   findById(id)   SELECT por id, retorna Optional
//   deleteById(id) DELETE
//   count()        SELECT COUNT(*)
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    // CONSULTA DERIVADA DO NOME DO MÉTODO: o Spring lê o nome e monta o SQL.
    //   findBy                      -> buscar onde
    //   Marca ContainingIgnoreCase  -> marca contém o texto, sem diferenciar maiúscula (LIKE %texto%)
    //   Or                          -> OU
    //   Modelo ContainingIgnoreCase -> modelo contém o texto
    // "Marca" e "Modelo" precisam ser iguais aos atributos da entidade.
    // 1º parâmetro vai para marca, 2º para modelo.
    List<Equipamento> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(String marca, String modelo);

    // existsBy: pergunta "existe algum registro com...?" e devolve true ou false.
    // Gera algo como: SELECT ... FROM equipamento WHERE numero_serie = ?
    // Usado ao CADASTRAR: se já existir esse número de série, é duplicado.
    boolean existsByNumeroSerie(String numeroSerie);

    // Mesma pergunta, mas IGNORANDO um id: "existe outro equipamento (id diferente
    // do informado) com este número de série?"
    // Usado ao EDITAR: o próprio equipamento tem esse número de série, e isso
    // não pode contar como duplicado.
    boolean existsByNumeroSerieAndIdNot(String numeroSerie, Long id);
}