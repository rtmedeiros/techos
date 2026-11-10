
package br.com.bugnacio.techos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.bugnacio.techos.repository.ClienteRepository;

@Controller
public class HomeController {

    private final ClienteRepository repo;

    public HomeController(ClienteRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalClientes", repo.count());
        model.addAttribute("totalEquipamentos", 10);
        model.addAttribute("osAbertas", 3);
        model.addAttribute("osConcluidas", 25);
        model.addAttribute("faturamento", 4850);

        return "dashboard";
    }
}
