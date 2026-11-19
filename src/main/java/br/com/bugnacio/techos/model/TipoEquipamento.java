// package: "endereço" da classe no projeto.
// Fica na pasta model, junto com as outras classes que representam dados do sistema.
package br.com.bugnacio.techos.model;

// enum: tipo especial do Java que define uma lista FECHADA de valores possíveis.
// Só os valores escritos aqui existem. Não dá para inventar "TABLET" na hora de salvar.
// Por isso o usuário nunca digita o tipo: ele escolhe em um <select>,
// e o sistema nunca recebe um valor inválido.
public enum TipoEquipamento {

    // Cada nome é uma constante (por convenção, em MAIÚSCULAS), separada por vírgula.
    COMPUTADOR,
    NOTEBOOK,
    CELULAR,
    // O último valor não precisa de vírgula.
    IMPRESSORA
}