package br.com.bugnacio.techos.controller;

import java.util.Optional;

// Exceção lançada pelo Spring quando o banco recusa um dado
// (por exemplo, violação da regra "unique").
import org.springframework.dao.DataIntegrityViolationException;
// Sort: define a ordem das consultas (por qual campo ordenar).
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
// Model: "pastinha" para enviar dados do Controller para o HTML.
import org.springframework.ui.Model;
// BindingResult: guarda os erros de validação encontrados no formulário.
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// RedirectAttributes: "pastinha" cujo conteúdo sobrevive a um redirect.
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.bugnacio.techos.model.Equipamento;
import br.com.bugnacio.techos.model.TipoEquipamento;
import br.com.bugnacio.techos.repository.ClienteRepository;
import br.com.bugnacio.techos.repository.EquipamentoRepository;
// @Valid: manda o Spring conferir as regras da entidade (@NotBlank, @NotNull...).
import jakarta.validation.Valid;

// @Controller: avisa ao Spring que esta classe recebe requisições HTTP
// e devolve páginas. O Spring cria o objeto sozinho ao iniciar:
// nós nunca escrevemos "new EquipamentoController()".
@Controller
// Prefixo de URL de todos os métodos da classe.
// Exemplo: @GetMapping("/novo") responde em /equipamentos/novo.
@RequestMapping("/equipamentos")
public class EquipamentoController {

    // ---------- ATRIBUTOS (as ferramentas do controller) ----------

    // Repositório que acessa a tabela de equipamentos.
    // private: só esta classe usa. final: depois de receber o objeto, não troca mais.
    private final EquipamentoRepository repo;

    // Repositório que acessa a tabela de clientes (para preencher o select).
    private final ClienteRepository clienteRepo;

    // ---------- CONSTRUTOR E INJEÇÃO DE DEPENDÊNCIA ----------
    // Ao criar o controller, o Spring procura os repositórios que ele já
    // gerencia e entrega como parâmetros. Isso é a injeção de dependência.
    public EquipamentoController(EquipamentoRepository repo, ClienteRepository clienteRepo) {
        // this.repo = atributo da classe; repo = parâmetro recebido.
        // Guardamos o objeto para os outros métodos usarem.
        this.repo = repo;
        this.clienteRepo = clienteRepo;
    }

    // ============ READ: LISTAR (com busca opcional) ============

    // Responde a GET /equipamentos (e a /equipamentos?busca=dell).
    @GetMapping
    public String listar(
            // @RequestParam: lê o parâmetro "busca" da URL.
            // required = false: a busca é opcional (a URL pode vir sem ela).
            @RequestParam(value = "busca", required = false) String busca,
            Model model) {

        // Se veio um texto de busca e ele não está em branco...
        if (busca != null && !busca.isBlank()) {
            // ...procura equipamentos cujo marca OU modelo contenha o texto.
            model.addAttribute("equipamentos",
                    repo.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(busca, busca));
        } else {
            // ...senão, lista todos ordenados por marca e depois por modelo.
            model.addAttribute("equipamentos", repo.findAll(Sort.by("marca", "modelo")));
        }

        // Devolve o texto buscado para o campo de busca continuar preenchido.
        model.addAttribute("busca", busca);

        // Nome do arquivo HTML a exibir: templates/equipamentos/lista.html
        return "equipamentos/lista";
    }

    // ============ CREATE (1/2): FORMULÁRIO VAZIO ============

    // Responde a GET /equipamentos/novo.
    @GetMapping("/novo")
    public String novo(Model model) {
        // Objeto vazio para o formulário preencher.
        // O nome "equipamento" é o mesmo usado em th:object="${equipamento}".
        model.addAttribute("equipamento", new Equipamento());
        // Envia as listas dos selects (clientes e tipos).
        carregarCombos(model);
        // Exibe templates/equipamentos/form.html
        return "equipamentos/form";
    }

    // ============ CREATE (2/2) e UPDATE: SALVAR ============

    // Responde a POST /equipamentos/salvar (envio do formulário).
    // O mesmo método cria e atualiza: id nulo = novo; id preenchido = edição.
    @PostMapping("/salvar")
    public String salvar(
            // Antes de executar o método, o Spring cria um Equipamento e preenche
            // cada atributo com o campo do formulário de mesmo nome.
            // Exemplo: name="marca" -> equipamento.setMarca(valor).
            // @Valid confere as regras da entidade (@NotBlank, @NotNull...).
            @Valid Equipamento equipamento,
            // Guarda o resultado da validação. DEVE vir logo depois do
            // parâmetro com @Valid. Com erro, result.hasErrors() é true.
            BindingResult result,
            // Usado para reenviar as listas dos selects se voltarmos ao formulário.
            Model model,
            // O que for colocado aqui sobrevive ao redirect e aparece na próxima tela.
            RedirectAttributes redirect) {

        // ----- REGRA 1: CLIENTE OBRIGATÓRIO -----
        // O select usa name="cliente.id". Com a opção "Escolha o cliente" (valor vazio),
        // o Spring cria um Cliente VAZIO (com id nulo) em vez de deixar o cliente nulo.
        // Por isso o @NotNull da entidade não percebe o problema.
        // Aqui conferimos o id: se está nulo, nenhum cliente foi escolhido.
        if (equipamento.getCliente() != null && equipamento.getCliente().getId() == null) {
            // Descarta o Cliente vazio.
            equipamento.setCliente(null);
            // Só registra o erro se o @NotNull ainda não tiver registrado (evita erro repetido).
            if (!result.hasFieldErrors("cliente")) {
                // rejectValue(campo, código, mensagem): adiciona um erro manualmente
                // ao campo "cliente". O th:errors do HTML exibe essa mensagem.
                result.rejectValue("cliente", "obrigatorio", "Selecione um cliente");
            }
        }

        // ----- REGRA 2: NÚMERO DE SÉRIE ÚNICO -----
        String serie = equipamento.getNumeroSerie();
        // Só verifica se o campo foi preenchido (o vazio já é barrado pelo @NotBlank).
        if (serie != null && !serie.isBlank()) {
            // trim() remove espaços das pontas: "ABC123 " e "ABC123" contam como iguais.
            serie = serie.trim();
            equipamento.setNumeroSerie(serie);

            // Operador ternário (condição ? valor se true : valor se false).
            // Se o id é nulo (cadastro novo), pergunta se já existe esse número de série.
            // Se o id existe (edição), pergunta se existe em OUTRO equipamento,
            // senão o equipamento seria bloqueado por causa do próprio número.
            boolean duplicado = (equipamento.getId() == null)
                    ? repo.existsByNumeroSerie(serie)
                    : repo.existsByNumeroSerieAndIdNot(serie, equipamento.getId());

            if (duplicado) {
                // Registra o erro no campo "numeroSerie" para aparecer abaixo do input.
                result.rejectValue("numeroSerie", "duplicado",
                        "Já existe um equipamento com este número de série");
            }
        }

        // ----- HÁ ALGUM ERRO? (das anotações ou das regras acima) -----
        if (result.hasErrors()) {
            // Recarrega as listas dos selects (a página será montada de novo).
            carregarCombos(model);
            // Volta ao formulário SEM salvar e SEM redirect.
            // Assim os dados digitados continuam na tela e os erros aparecem.
            return "equipamentos/form";
        }

        // ----- SALVAR -----
        try {
            // id nulo = INSERT; id existente = UPDATE.
            // O Spring Data JPA decide sozinho.
            repo.save(equipamento);
        } catch (DataIntegrityViolationException e) {
            // Rede de segurança: se duas pessoas salvarem o mesmo número de série
            // quase ao mesmo tempo, a conferência acima não pega, mas a
            // restrição unique do banco pega e lança esta exceção.
            result.rejectValue("numeroSerie", "duplicado",
                    "Já existe um equipamento com este número de série");
            carregarCombos(model);
            return "equipamentos/form";
        }

        // Mensagem exibida uma única vez na próxima tela.
        // Em lista.html é lida com th:if="${mensagem}" e th:text="${mensagem}".
        redirect.addFlashAttribute("mensagem", "Equipamento salvo com sucesso!");

        // Redireciona para a lista. O navegador faz um novo GET /equipamentos.
        // Isso evita que o F5 reenvie o formulário e crie um registro duplicado.
        return "redirect:/equipamentos";
    }

    // ============ UPDATE (1/2): FORMULÁRIO PREENCHIDO ============

    // Responde a GET /equipamentos/5/editar. {id} é uma parte variável da URL.
    @GetMapping("/{id}/editar")
    public String editar(
            // @PathVariable: pega o valor de {id} da URL e coloca em "id".
            @PathVariable("id") Long id,
            Model model, RedirectAttributes redirect) {

        // findById devolve um Optional: uma "caixinha" que pode ter ou não o equipamento.
        // Isso evita o erro de trabalhar com null.
        Optional<Equipamento> equipamento = repo.findById(id);

        // Se a caixinha está vazia, o id não existe no banco.
        if (equipamento.isEmpty()) {
            redirect.addFlashAttribute("erro", "Equipamento não encontrado.");
            return "redirect:/equipamentos";
        }

        // get() tira o equipamento da caixinha e o envia para o formulário preencher.
        model.addAttribute("equipamento", equipamento.get());
        carregarCombos(model);
        return "equipamentos/form";
    }

    // ============ DELETE: EXCLUIR ============

    // POST (e não GET) porque excluir ALTERA dados.
    // Um link GET poderia ser acionado sem querer (ou por robôs).
    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect) {
        // Remove a linha com este id.
        repo.deleteById(id);
        redirect.addFlashAttribute("mensagem", "Equipamento excluído.");
        return "redirect:/equipamentos";
    }

    // ============ MÉTODO AUXILIAR: CARREGAR COMBOS ============

    // private: só é usado dentro desta classe.
    // Evita repetir as mesmas linhas em novo(), editar() e salvar().
    private void carregarCombos(Model model) {
        // Lista de clientes (ordenada por nome) para o select de clientes.
        model.addAttribute("clientes", clienteRepo.findAll(Sort.by("nome")));
        // values() devolve todos os valores do enum, para o select de tipos.
        model.addAttribute("tipos", TipoEquipamento.values());
    }
}