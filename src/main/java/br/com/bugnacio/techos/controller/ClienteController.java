
package br.com.bugnacio.techos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bugnacio.techos.model.Cliente;
import br.com.bugnacio.techos.repository.ClienteRepository;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    // Repository responsável por acessar o banco de dados.
    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) {
        this.repo = repo;
    }

    // READ: Lista os clientes e permite pesquisar pelo nome.
    @GetMapping
    public String listar(
            @RequestParam(value = "busca", required = false) String busca,
            Model model) {

        if (busca != null && !busca.isEmpty()) {
            model.addAttribute("clientes",
                    repo.findByNomeContainingIgnoreCase(busca));
        } else {
            model.addAttribute("clientes", repo.findAll());
        }

        return "clientes/lista";
    }

    // CREATE: Abre o formulário para cadastrar um cliente.
    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("cliente", new Cliente());

        return "clientes/form";
    }

    // CREATE / UPDATE: Salva um cliente novo ou atualizado.
    @PostMapping("/salvar")
    public String salvar(
            Cliente cliente,
            BindingResult result,
            RedirectAttributes redirect) {

        // Verifica se o telefone já está cadastrado.
        boolean jaExiste;

        if (cliente.getId() == null) {
            jaExiste = repo.existsByTelefone(cliente.getTelefone());
        } else {
            jaExiste = repo.existsByTelefoneAndIdNot(
                    cliente.getTelefone(), cliente.getId());
        }

        if (jaExiste) {
            result.rejectValue(
                    "telefone",
                    "duplicado",
                    "Já existe um cliente com este telefone");
        }

        // Se houver erros, retorna ao formulário.
        if (result.hasErrors()) {
            return "clientes/form";
        }

        // Salva o cliente no banco de dados.
        repo.save(cliente);

        redirect.addFlashAttribute(
                "mensagem",
                "Cliente salvo com sucesso!");

        return "redirect:/clientes";
    }

    // DELETE: Exclui um cliente pelo ID.
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirect) {

        repo.deleteById(id);

        redirect.addFlashAttribute(
                "mensagem",
                "Cliente excluído com sucesso!");

        return "redirect:/clientes";
    }

    // UPDATE: Busca os dados do cliente para editar.
    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Cliente cliente = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente não encontrado: " + id));

        model.addAttribute("cliente", cliente);

        return "clientes/form";
    }

}
