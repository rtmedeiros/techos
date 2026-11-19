
package br.com.bugnacio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bugnacio.techos.exception.RegraDeNegocioException;
import br.com.bugnacio.techos.repository.ClienteRepository;
import br.com.bugnacio.techos.repository.EquipamentoRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepo;
    private final EquipamentoRepository equipamentoRepo;

    public ClienteService(ClienteRepository clienteRepo,
                          EquipamentoRepository equipRepo) {

        this.clienteRepo = clienteRepo;
        this.equipamentoRepo = equipRepo;
    }

    // Exclui um cliente pelo ID, respeitando as regras de negócio.
    @Transactional
    public void excluir(Long id) {

        // Verifica se o cliente existe.
        if (!clienteRepo.existsById(id)) {
            throw new RegraDeNegocioException(
                "Cliente não encontrado."
            );
        }

        // Verifica se existem equipamentos associados.
        if (equipamentoRepo.existsByCliente_Id(id)) {
            throw new RegraDeNegocioException(
                "Este cliente possui equipamentos associados e não pode ser excluído."
            );
        }

        // Exclui o cliente.
        clienteRepo.deleteById(id);
    }
}
