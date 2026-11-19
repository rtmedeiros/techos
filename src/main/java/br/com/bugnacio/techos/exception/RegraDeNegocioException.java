package br.com.bugnacio.techos.exception;

// Classe de exceção personalizada para regras de negócio.
//para representar erros específicos do sistema, como validações de dados ou restrições de negócio.

//construtor que recebe uma mensagem de erro e chama o construtor da classe pai (RuntimeException) com essa mensagem.

public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message) {

    //super(): chama o construtor da classe pai (RuntimeException) com a mensagem de erro fornecida.
    //enviando a mensagem recebida
    //assim, a exceção passa a carregar a descrição do erro que ocorreu, permitindo que o sistema trate a exceção de forma adequada.

        super(message);
    }

}
