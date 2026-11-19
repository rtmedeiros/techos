package br.com.bugnacio.techos.model;

// ---------- IMPORTS DO JPA (ligação classe <-> tabela do banco) ----------
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// ---------- IMPORTS DO BEAN VALIDATION (regras de preenchimento) ----------
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// @Entity: avisa ao JPA que esta classe representa uma TABELA no banco.
// A classe Equipamento vira a tabela "equipamento"; cada objeto é uma linha.
@Entity
public class Equipamento {

    // @Id: este atributo é a chave primária (identifica cada linha de forma única).
    @Id
    // @GeneratedValue: o valor não é digitado por nós, o banco gera sozinho.
    // IDENTITY: usa o auto incremento do banco (1, 2, 3...).
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Long (e não long): aceita null. Um equipamento novo ainda não tem id.
    // É isso que o controller usa para saber se é INSERT (id nulo) ou UPDATE (id preenchido).
    private Long id;

    // @NotNull: o campo não pode ser nulo. Aqui, obriga a escolher um tipo.
    // "message" é o texto que aparece na tela quando a regra é violada.
    @NotNull(message = "Escolha o tipo")
    // @Enumerated(STRING): grava o NOME do tipo ("NOTEBOOK") no banco.
    // O padrão (ORDINAL) grava a posição (0, 1, 2...) e quebraria os dados
    // se alguém mudasse a ordem dos valores no enum.
    @Enumerated(EnumType.STRING)
    private TipoEquipamento tipo;

    // @NotBlank: não aceita null, texto vazio ("") nem só espaços ("   ").
    // Para String, é mais completo que @NotNull.
    @NotBlank(message = "Informe a marca")
    private String marca;

    @NotBlank(message = "Informe o modelo")
    private String modelo;

    // AJUSTE: o comentário antigo dizia "opcional", mas o campo é obrigatório e único.
    // @NotBlank: o número de série deve ser informado.
    @NotBlank(message = "Informe o número de série")
    // @Column(unique = true): cria no BANCO uma restrição que impede dois registros
    // com o mesmo número de série. Atenção: isso NÃO é validação do formulário.
    // O banco só reclama na hora de gravar (erro 500).
    // Por isso o controller também confere antes de salvar, para mostrar uma
    // mensagem amigável.
    @Column(unique = true)
    private String numeroSerie;

    // AJUSTE: @NotNull obriga a ter um cliente associado.
    @NotNull(message = "Selecione um cliente")
    // @ManyToOne: MUITOS equipamentos podem pertencer a UM cliente.
    @ManyToOne
    // @JoinColumn: cria no banco a coluna de chave estrangeira "cliente_id",
    // que guarda o id do cliente dono do equipamento.
    // nullable = false: o banco também recusa equipamento sem cliente
    // (segunda barreira de segurança).
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Construtor vazio: o JPA e o Spring precisam dele.
    // O Spring cria um Equipamento vazio e depois preenche com os dados do formulário.
    public Equipamento() {
    }

    // ---------- GETTERS E SETTERS ----------
    // private impede o acesso direto aos atributos (encapsulamento).
    // get: LÊ o valor. set: ALTERA o valor.
    // O Spring e o Thymeleaf usam estes métodos para preencher e exibir o formulário.
    // Exemplo: th:field="*{marca}" chama getMarca() para mostrar
    // e setMarca() para guardar o que foi digitado.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoEquipamento getTipo() { return tipo; }
    public void setTipo(TipoEquipamento tipo) { this.tipo = tipo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
