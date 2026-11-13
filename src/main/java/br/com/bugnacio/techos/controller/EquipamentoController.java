
package br.com.bugnacio.techos.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bugnacio.techos.model.Equipamento;
import br.com.bugnacio.techos.model.TipoEquipamento;
import br.com.bugnacio.techos.repository.ClienteRepository;
import br.com.bugnacio.techos.repository.EquipamentoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoRepository equipamentoRepo;
    private final ClienteRepository clienteRepo;

    // Construtor: recebe os dois repositories.
    public EquipamentoController(
            EquipamentoRepository equipamentoRepo,
            ClienteRepository clienteRepo) {

        this.equipamentoRepo = equipamentoRepo;
        this.clienteRepo = clienteRepo;
    }

    // READ: listar equipamentos.
    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "equipamentos",
                equipamentoRepo.findAll());

        return "equipamentos/lista";
    }

    // CREATE (1/2): abrir formulário vazio.
    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute("equipamento", new Equipamento());

        carregarCombos(model);

        return "equipamentos/form";
    }

    // CREATE (2/2): validar e salvar equipamento.
    @PostMapping("/salvar")
    public String salvar(
            @Valid @ModelAttribute("equipamento") Equipamento equipamento,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {

            carregarCombos(model);

            return "equipamentos/form";
        }

        equipamentoRepo.save(equipamento);

        redirect.addFlashAttribute(
                "mensagem",
                "Equipamento salvo com sucesso!");

        return "redirect:/equipamentos";
    }

    // UPDATE (1/2): buscar equipamento para editar.
    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Equipamento equipamento = equipamentoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Equipamento não encontrado: " + id));

        model.addAttribute("equipamento", equipamento);

        carregarCombos(model);

        return "equipamentos/form";
    }

    // UPDATE (2/2): atualizar equipamento.
    @PostMapping("/atualizar")
    public String atualizar(
            @Valid @ModelAttribute("equipamento") Equipamento equipamento,
            BindingResult result,
            Model model,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {

            carregarCombos(model);

            return "equipamentos/form";
        }

        equipamentoRepo.save(equipamento);

        redirect.addFlashAttribute(
                "mensagem",
                "Equipamento atualizado com sucesso!");

        return "redirect:/equipamentos";
    }

    // DELETE: excluir equipamento.
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirect) {

        Equipamento equipamento = equipamentoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Equipamento não encontrado: " + id));

        equipamentoRepo.delete(equipamento);

        redirect.addFlashAttribute(
                "mensagem",
                "Equipamento excluído com sucesso!");

        return "redirect:/equipamentos";
    }

    // Método auxiliar: carregar campos de seleção.
    private void carregarCombos(Model model) {

        model.addAttribute(
                "clientes",
                clienteRepo.findAll(Sort.by("nome")));

        model.addAttribute(
                "tipos",
                TipoEquipamento.values());
    }
}
