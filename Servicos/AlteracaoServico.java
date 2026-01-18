package Servicos;

import ModelosPets.Pet;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class AlteracaoServico {

    private final ServicoArquivo servicoArquivo;

    public AlteracaoServico() {
        this.servicoArquivo = new ServicoArquivo();
    }

    public void alterarPet(Scanner scanner) {
        System.out.println("\n--- ALTERAÇÃO DE PET ---");

        // 1. Listar para escolher
        List<Pet> pets = servicoArquivo.lerTodosOsPets();
        List<Path> arquivos = servicoArquivo.listarArquivos(); // Pega os arquivos na mesma ordem

        if (pets.isEmpty()) {
            System.out.println("⚠️ Não há pets cadastrados para alterar.");
            return;
        }

        System.out.println("Escolha o número do Pet para editar:");
        for (int i = 0; i < pets.size(); i++) {
            System.out.println((i + 1) + " - " + pets.get(i).getNome() + " (" + pets.get(i).getTipo() + ")");
        }

        // 2. Captura a escolha do usuário
        int escolha = lerOpcaoSegura(scanner);
        if (escolha < 1 || escolha > pets.size()) {
            System.out.println("❌ Opção inválida. Operação cancelada.");
            return;
        }

        // Recupera o Pet e o Arquivo correspondente
        Pet petSelecionado = pets.get(escolha - 1);
        Path arquivoOriginal = arquivos.get(escolha - 1);

        System.out.println("\nEditando Pet: " + petSelecionado.getNome());
        System.out.println("⚠️ Nota: Tipo e Sexo não podem ser alterados.");

        boolean editando = true;
        while (editando) {
            System.out.println("\n--- O QUE DESEJA ALTERAR? ---");
            System.out.println("1 - Nome (" + petSelecionado.getNome() + ")");
            System.out.println("2 - Idade (" + petSelecionado.getIdade() + ")");
            System.out.println("3 - Peso (" + petSelecionado.getPeso() + ")");
            System.out.println("4 - Raça (" + petSelecionado.getRaca() + ")");
            System.out.println("5 - Endereço (" + petSelecionado.getCidade() + "...)");
            System.out.println("6 - FINALIZAR E SALVAR");
            System.out.println("0 - Cancelar (sem salvar)");
            System.out.print("Opção: ");

            int opcao = lerOpcaoSegura(scanner);

            switch (opcao) {
                case 1:
                    System.out.println("Novo Nome (Apenas letras):");
                    String novoNome = scanner.nextLine().trim();
                    if (novoNome.matches("^[A-Za-z ]+$")) {
                        petSelecionado.setNome(novoNome);
                        System.out.println("✅ Nome alterado (em memória).");
                    } else {
                        System.out.println("❌ Nome inválido.");
                    }
                    break;
                case 2:
                    System.out.println("Nova Idade:");
                    try {
                        double idade = Double.parseDouble(scanner.nextLine().replace(",", "."));
                        if (idade >= 0 && idade <= 20) {
                            petSelecionado.setIdade(idade);
                            System.out.println("✅ Idade alterada.");
                        } else System.out.println("❌ Idade fora do limite (0-20).");
                    } catch (NumberFormatException e) { System.out.println("❌ Número inválido."); }
                    break;
                case 3:
                    System.out.println("Novo Peso:");
                    try {
                        double peso = Double.parseDouble(scanner.nextLine().replace(",", "."));
                        if (peso >= 0.5 && peso <= 60) {
                            petSelecionado.setPeso(peso);
                            System.out.println("✅ Peso alterado.");
                        } else System.out.println("❌ Peso fora do limite (0.5-60).");
                    } catch (NumberFormatException e) { System.out.println("❌ Número inválido."); }
                    break;
                case 4:
                    System.out.println("Nova Raça:");
                    String raca = scanner.nextLine().trim();
                    if (!raca.isEmpty()) {
                        petSelecionado.setRaca(raca);
                        System.out.println("✅ Raça alterada.");
                    }
                    break;
                case 5:
                    System.out.println("Novo Endereço Completo (Rua, Número, Cidade):");
                    String end = scanner.nextLine();
                    String[] partes = end.split(",");
                    if (partes.length == 3) {
                        petSelecionado.setRua(partes[0].trim());
                        petSelecionado.setNumero(partes[1].trim());
                        petSelecionado.setCidade(partes[2].trim());
                        System.out.println("✅ Endereço alterado.");
                    } else {
                        System.out.println("❌ Formato inválido. Use vírgulas.");
                    }
                    break;
                case 6: // SALVAR
                    // 1. Deleta o velho
                    servicoArquivo.deletarArquivo(arquivoOriginal);
                    // 2. Salva o novo (gera novo nome de arquivo se precisar)
                    servicoArquivo.salvarPet(petSelecionado);
                    System.out.println("💾 Alterações salvas com sucesso!");
                    editando = false;
                    break;
                case 0: // CANCELAR
                    System.out.println("Operação cancelada. Nada foi mudado.");
                    editando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private int lerOpcaoSegura(Scanner scanner) {
        try {
            String txt = scanner.nextLine();
            if (txt.isEmpty()) return -1;
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}