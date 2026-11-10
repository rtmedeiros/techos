package br.com.bugnacio.techos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bugnacio.techos.model.Cliente;

//Interface (Não é classe): Não escrevemos o corpo da classe, o Spring Data JPA faz isso para nós.
//A interface JpaRepository já possui métodos prontos para salvar, buscar, atualizar e deletar registros
// no banco de dados
public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    //Verifica se existe algum cliente com o mesmo telefone
    //Retorna true se existir, false se não existir
    //Utilizado para evitar duplicidade de telefone no banco de dados

    boolean existsByTelefone(String telefone);

        //Verifica se o telefone existe para outro cliente (id diferente)
    boolean existsByTelefoneAndIdNot(String telefone, Long id);


    //Busca clientes pelo nome, ignorando maiúsculas e minúsculas
    //Exemplo se pesquisar por joao, taambém econtra João da Silva, Joao, joaO, etc
    List<Cliente> findByNomeContainingIgnoreCase(String nome);

}
