package App;

import ModelosPets.Pet;
import Servicos.*; // Importa CadastroService, BuscaService, etc.

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    // ATENÇÃO: Verifique se os nomes abaixo batem com os arquivos na pasta 'Servicos'
    // Nos passos anteriores usamos "Service" no final, não "Servico".
    private static final CadastroServico cadastroServico = new CadastroServico();
    private static final ServicoArquivo servicoArquivo = new ServicoArquivo();
    private static final BuscaServico buscaServico = new BuscaServico();       // Ajustado de Servico -> Service
    private static final AlteracaoServico alteracaoServico = new AlteracaoServico(); // Ajustado de Servico -> Service
    private static final DeletarServico deletarServico = new DeletarServico();     // Ajustado de DeletarServico -> DelecaoService

    public static void main(String[] args) {

        boolean iniciado = true;

        System.out.println("=== SISTEMA DE GERENCIAMENTO DE PETS ===");

        while ( iniciado ) {
            exibirOpcoesMenu();
            int opcao = lerOpcaoUser();

            switch ( opcao ) {
                case 1:
                    Pet novoPet = cadastroServico.cadastrarNovoPet(sc);
                    if (novoPet != null) {
                        servicoArquivo.salvarPet(novoPet);
                    }
                    break;
                case 2:
                    alteracaoServico.alterarPet(sc);
                    break;
                case 3:
                    deletarServico.deletarPet(sc);
                    break;
                case 4:
                    // IMPLEMENTAÇÃO RÁPIDA DA LISTAGEM
                    System.out.println("\n--- LISTA DE PETS ---");
                    List<Pet> lista = servicoArquivo.lerTodosOsPets();
                    if (lista.isEmpty()) {
                        System.out.println("Não há pets cadastrados.");
                    } else {
                        for (Pet p : lista) {
                            System.out.println("• " + p.getNome() + " (" + p.getTipo() + ") - " + p.getCidade());
                        }
                    }
                    break;
                case 5:
                    buscaServico.buscarPets(sc);
                    break;
                case 6:
                    // A lógica de encerrar está aqui, mas vamos refinar no Passo 8
                    System.out.println("Encerrando o sistema...");
                    iniciado = false;
                    break;
                default:
                    System.out.println("Opção inválida! Digite um valor entre 1 e 6");
            }

            if (iniciado) {
                System.out.println("\nPressione ENTER para voltar ao menu...");
                sc.nextLine();
            }
        }

        // Boa prática: Fechar o scanner ao sair do while
        sc.close();
        System.out.println("Sistema finalizado. Até logo!");
    }

    private static void exibirOpcoesMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1 - Cadastrar novo pet");
        System.out.println("2 - Alterar pet");
        System.out.println("3 - Deletar pet");
        System.out.println("4 - Listar pets");
        System.out.println("5 - Buscar pets por critério");
        System.out.println("6 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcaoUser() {
        try {
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) return -1;
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}