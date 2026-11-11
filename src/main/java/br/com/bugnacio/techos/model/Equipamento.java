
package br.com.bugnacio.techos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tipo do equipamento (Enum).
    @NotNull(message = "Selecione o tipo do equipamento")
    @Enumerated(EnumType.STRING)
    private TipoEquipamento tipo;

    // Marca do equipamento.
    @NotBlank(message = "Informe a marca")
    @Size(max = 100, message = "A marca pode ter no máximo 100 caracteres")
    private String marca;

    // Modelo do equipamento.
    @NotBlank(message = "Informe o modelo")
    @Size(max = 100, message = "O modelo pode ter no máximo 100 caracteres")
    private String modelo;

    // Número de série (opcional).
    @Size(max = 100, message = "O número de série pode ter no máximo 100 caracteres")
    private String numeroSerie;

    // Relacionamento com Cliente.
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Construtor padrão.
    public Equipamento() {
    }

    // Getters e Setters.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEquipamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipamento tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
