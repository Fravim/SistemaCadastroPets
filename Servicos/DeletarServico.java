package Servicos;

import ModelosPets.Pet;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class DeletarServico {

    private final ServicoArquivo servicoArquivo;

    public DeletarServico() {
        this.servicoArquivo = new ServicoArquivo();
    }

    public void deletarPet(Scanner scanner) {
        System.out.println("\n--- EXCLUSÃO DE PET ---");

        // 1. Recupera as listas (Objetos e Caminhos dos arquivos)
        // Precisamos das duas listas sincronizadas para saber qual arquivo apagar
        List<Pet> pets = servicoArquivo.lerTodosOsPets();
        List<Path> arquivos = servicoArquivo.listarArquivos();

        if (pets.isEmpty()) {
            System.out.println("Não há pets cadastrados para excluir.");
            return;
        }

        // 2. Exibe a lista para escolha
        System.out.println("Selecione o número do Pet que deseja remover:");
        for (int i = 0; i < pets.size(); i++) {
            System.out.println((i + 1) + " - " + pets.get(i).getNome() + " (" + pets.get(i).getTipo() + ")");
        }
        System.out.println("0 - Cancelar");

        // 3. Captura a escolha
        int escolha = lerOpcaoSegura(scanner);

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (escolha < 1 || escolha > pets.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        // Recupera o pet selecionado para mostrar o nome na confirmação
        Pet petSelecionado = pets.get(escolha - 1);
        Path arquivoParaDeletar = arquivos.get(escolha - 1);

        // 4. Confirmação de Segurança (CRUCIAL)
        System.out.println("\nATENÇÃO: Você está prestes a excluir permanentemente o pet: " + petSelecionado.getNome());
        System.out.print("Tem certeza? Digite 'SIM' para confirmar: ");
        String confirmacao = scanner.nextLine().trim();

        if (confirmacao.equalsIgnoreCase("SIM")) {
            // 5. Chama o servico de arquivo para deletar fisicamente
            boolean sucesso = servicoArquivo.deletarArquivo(arquivoParaDeletar);

            if (sucesso) {
                System.out.println("Pet excluído com sucesso!");
            } else {
                System.out.println("Erro ao tentar excluir o arquivo. Verifique permissões.");
            }
        } else {
            System.out.println("Operação cancelada. O pet não foi excluído.");
        }
    }

    // Método auxiliar para evitar crash se digitar letras
    private int lerOpcaoSegura(Scanner scanner) {
        try {
            String entrada = scanner.nextLine();
            if (entrada.isEmpty()) return -1;
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}