package br.com.bugnacio.techos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bugnacio.techos.model.Cliente;
import br.com.bugnacio.techos.repository.ClienteRepository;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/clientes")
public class ClienteController {

    // O controller precisa do repository para buscar os clientes no banco de dados.
    private final ClienteRepository repo;

    public ClienteController(ClienteRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", repo.findAll());
        return "clientes/lista";
    }

    // Get /clientes/novo: mostra o formulário para criar um novo cliente.
    @GetMapping("/novo")
    public String novo(Model model) {

        // A tela clientes/form espera um objeto chamado "cliente".
        // Mandamos um cliente vazio (new Cliente()).
        model.addAttribute("cliente", new Cliente());

        // Sem @ResponseBody: o texto devolvido é o nome da tela.
        // Abre: src/main/resources/templates/clientes/form.html
        return "clientes/form";
    }

     //Método para inserir cliente
    @PostMapping("/salvar")
    public String salvar(Cliente cliente, RedirectAttributes redirect) {

        // Salva o cliente.
        repo.save(cliente);

        redirect.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        
        return "redirect:/clientes";
    }

    //método para deletar cliente
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        repo.deleteById(id);
        redirect.addFlashAttribute("mensagem", 
        
        "Cliente excluído com sucesso!");
        return "redirect:/clientes";
    }

//método para editar cliente
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id,Model model) {
        Cliente cliente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + id));
        model.addAttribute("cliente", cliente);
        
        return "clientes/form";
    }

    

}
