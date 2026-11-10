
package br.com.bugnacio.techos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
 

// @Entitu: essa classe representa uma tabela no banco de daods
@Entity

public class Cliente {
    // @id: o campo que representa a chave primaria da tabela.
    // @GeneratedValue: o valor da chave primaria e gerado automaticamente.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBank: o nome do cliente não pode ser vazio.
    //@Size tamanho maximo, message: o texto que aparece ao lado do campo

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 100, message = "O nome do cliente não pode ter mais de 100 caracteres")
    private String nome;

    
    //TELEFONE
    
    @NotBlank(message = "Informe o telefone do cliente")
    
    private String telefone;
    
    
    //EMAIL
    @NotBlank(message = "Informe um email válido")
    private String email;


    // Cada atributo vira uma coluna na tabela. O nome da coluna é o mesmo do
    // atributo.
    
    
 

    // Construtor padrao (sem argumentos) necessario para o JPA
    public Cliente() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   

}
