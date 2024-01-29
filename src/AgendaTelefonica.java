import java.io.*;
import java.util.*;

public class AgendaTelefonica {

    private List<Contato> contatos;

    private Long proximoIdContato;

    private final String arquivoContatos = "agenda.txt";

    // Função que inicializa a lista de contatos, inicializa o ID do próximo contato como 1 e
    //Carrega os contatos do arquivo ao criar uma instância da agenda
    public AgendaTelefonica() {

        contatos = new ArrayList<>();

        proximoIdContato = 1L;

        carregarContatos();
    }

    // Método para adicionar um novo contato à agenda: Verifica se os números de telefone
    // já estão cadastrados em outros contatos, verifica se o ID do contato já está em uso
    // cria uma lista de objetos Telefone com os números informados, cria um novo contato e
    // adiciona à lista de contatos, incrementa o ID do próximo contato, Salva os contatos no arquivo
    public void adicionarContato(String nome, String sobreNome, List<String> numeros) {

        for (String numero : numeros) {
            if (telefoneJaCadastrado(numero)) {
                System.out.println("Já existe um contato cujo número é " + numero);
                return;
            }
        }

        if (contatoIdJaCadastrado(proximoIdContato)) {
            System.out.println("Já existe um contato cujo ID é " + proximoIdContato);
            return;
        }

        List<Telefone> telefones = new ArrayList<>();
        for (String numero : numeros) {
            telefones.add(new Telefone(proximoIdContato, numero));
        }

        contatos.add(new Contato(proximoIdContato, nome, sobreNome, telefones));

        proximoIdContato++;

        salvarContatos();
        System.out.println("O contato foi adicionado");
    }

    // Verificar se o ID do contato já está cadastrado
    private boolean contatoIdJaCadastrado(Long id) {
        for (Contato contato : contatos) {
            if (contato.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Verificar se um número de telefone já está cadastrado na agenda
    private boolean telefoneJaCadastrado(String numero) {
        for (Contato contato : contatos) {
            for (Telefone telefone : contato.getTelefones()) {
                if (telefone.getNumero().equals(numero)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Remove um contato da agenda e salva os contatos atualizados no arquivo
    public void removerContato(Long id) {
        contatos.removeIf(contato -> contato.getId().equals(id));

        salvarContatos();
        System.out.println("O contato foi removido");
    }

    // Edita um contato da agenda: busca pelo contato com o ID informado, Verifica se os
    // novos números já estão cadastrados em outros contatos, atualiza os dados do contato
    // com os novos valores informados e salva os contatos atualizados no arquivo
    public void editarContato(Long id, String nome, String sobreNome, List<String> novosNumeros) {

        for (Contato contato : contatos) {
            if (contato.getId().equals(id)) {
                for (String numero : novosNumeros) {
                    if (telefoneJaCadastrado(numero) && !telefonePertenceContato(id, numero)) {
                        System.out.println("Já existe um contato cujo número é" + numero);
                        return;
                    }
                }

                contato.setNome(nome);
                contato.setSobreNome(sobreNome);
                List<Telefone> novosTelefones = new ArrayList<>();
                for (String numero : novosNumeros) {
                    novosTelefones.add(new Telefone(contato.getId(), numero));
                }
                contato.setTelefones(novosTelefones);

                salvarContatos();
                System.out.println("Contato editado");
                return;
            }
        }
        System.out.println("Contato não encontrado");
    }

    // Verifica se um número de telefone pertence a um contato
    private boolean telefonePertenceContato(Long idContato, String numero) {
        for (Contato contato : contatos) {
            if (contato.getId().equals(idContato)) {
                for (Telefone telefone : contato.getTelefones()) {
                    if (telefone.getNumero().equals(numero)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Exibe todos os contatos da agenda
    public void exibirContatos() {
        if (contatos.isEmpty()) {
            System.out.println("Nenhum contato na agenda.");
            return;
        }
        System.out.println(">>>> Contatos <<<<");
        System.out.println("Id | Nome");
        for (Contato contato : contatos) {
            System.out.println(contato.getId() + " | " + contato.getNome() + " " + contato.getSobreNome());
        }
    }

    // Carrega os contatos do arquivo: formatação - divide a linha em partes usando a vírgula
    // como separador, a primeira parte é o ID do contato, a segunda parte é o nome do contato
    // a terceira parte é o sobrenome do contato, as partes restantes são os números de telefone do contato
    // cria um novo contato com os dados carregados e o adiciona à lista de contatos e atualiza o ID do
    // próximo contato, se necessário
    private void carregarContatos() {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoContatos))) {
            String linha;
            while ((linha = br.readLine()) != null) {

                String[] partes = linha.split(",");

                Long id = Long.parseLong(partes[0]);

                String nome = partes[1];

                String sobreNome = partes[2];

                List<Telefone> telefones = new ArrayList<>();
                for (int i = 3; i < partes.length; i++) {
                    telefones.add(new Telefone(id, partes[i]));
                }

                contatos.add(new Contato(id, nome, sobreNome, telefones));

                if (id >= proximoIdContato) {
                    proximoIdContato = id + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Foi impossível carregar os contatos do arquivo: " + e.getMessage());
        }
    }

    // Salva os contatos no arquivo: escreve uma linha no arquivo para cada contato
    private void salvarContatos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoContatos))) {
            for (Contato contato : contatos) {

                bw.write(contato.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("´Foi impossível salvar os contatos no arquivo: " + e.getMessage());
        }
    }

    // Método que controla o fluxo da Agenda para criar a classe, receber entradas
    // do usuário e armazenar a opção
    public static void main(String[] args) {

        AgendaTelefonica agenda = new AgendaTelefonica();

        Scanner scanner = new Scanner(System.in);

        int opcao;


        do {

            System.out.println("\n##################");
            System.out.println("##### AGENDA #####");
            System.out.println("##################");
            agenda.exibirContatos();
            System.out.println(">>>> Menu <<<<");
            System.out.println("1 - Adicionar Contato");
            System.out.println("2 - Remover Contato");
            System.out.println("3 - Editar Contato");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer do teclado


            switch (opcao) {
                case 1:

                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();
                    System.out.print("Sobrenome: ");
                    String sobreNome = scanner.nextLine();
                    System.out.print("Quantidade de números: ");
                    int quantidadeNumeros = scanner.nextInt();
                    scanner.nextLine();
                    List<String> numeros = new ArrayList<>();
                    for (int i = 0; i < quantidadeNumeros; i++) {
                        System.out.printf("Número %d (apenas números incluindo o ddd): ", i + 1);
                        numeros.add(scanner.nextLine());
                    }
                    agenda.adicionarContato(nome, sobreNome, numeros);
                    break;
                case 2:

                    System.out.print("ID do Contato a ser removido: ");
                    Long idRemover = scanner.nextLong();
                    agenda.removerContato(idRemover);
                    break;
                case 3:

                    System.out.print("ID do Contato a ser editado: ");
                    Long idEditar = scanner.nextLong();
                    scanner.nextLine();
                    System.out.print("Novo Nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Novo Sobrenome: ");
                    String novoSobreNome = scanner.nextLine();
                    System.out.print("Nova quantidade de números: ");
                    int novaQuantidadeNumeros = scanner.nextInt();
                    scanner.nextLine();
                    List<String> novosNumeros = new ArrayList<>();
                    for (int i = 0; i < novaQuantidadeNumeros; i++) {
                        System.out.printf("Novo Número %d: ", i + 1);
                        novosNumeros.add(scanner.nextLine());
                    }
                    agenda.editarContato(idEditar, novoNome, novoSobreNome, novosNumeros);
                    break;
                case 4:

                    System.out.println("Saindo...");
                    break;
                default:

                    System.out.println("Opção inválida!");
            }
        } while (opcao != 4);


        scanner.close();
    }
}

// Classe que representa um contato na agenda
class Contato {
    private Long id;
    private String nome;
    private String sobreNome;
    private List<Telefone> telefones;


    public Contato(Long id, String nome, String sobreNome, List<Telefone> telefones) {
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.telefones = telefones;
    }

    // Métodos de acesso aos atributos privados
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }


    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",").append(nome).append(",").append(sobreNome);
        for (Telefone telefone : telefones) {
            sb.append(",").append(telefone.getNumero());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + " " + sobreNome + ", Telefones: " + telefones;
    }
}


class Telefone {
    private Long id;
    private String numero;


    public Telefone(Long id, String numero) {
        this.id = id;
        this.numero = numero;
    }


    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return numero;
    }
}
