package Servicos;

import ModelosPets.Pet;
import ModelosPets.SexoPet;
import ModelosPets.TipoPet;

import java.util.List;
import java.util.Scanner;

public class CadastroServico {

    private final GerenciadorFormulario gerenciador;

    public CadastroServico() {
        this.gerenciador = new GerenciadorFormulario();
    }

    public Pet cadastrarNovoPet(Scanner scanner) {
        List<String> perguntas = gerenciador.lerPerguntas();

        // Se algo deu errado na leitura do arquivo
        if (perguntas.size() < 7) {
            System.out.println("ERRO: O formulário não contem todas as perguntas necessárias.");
            return null;
        }

        Pet pet = new Pet();
        System.out.println("\n--- INICIANDO CADASTRO ---");

        // 1. TIPO (Pergunta índice 0)
        // Loop de validação: enquanto não digitar certo, não sai daqui.
        while (true) {
            System.out.println(perguntas.get(0));
            String entrada = scanner.nextLine().toUpperCase().trim();
            try {
                // Tenta converter String em Enum
                pet.setTipo(TipoPet.valueOf(entrada));
                break; // Se deu certo, sai do loop
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo inválido! Digite: CACHORRO ou GATO.");
            }
        }

        // 2. SEXO (Pergunta índice 1)
        while (true) {
            System.out.println(perguntas.get(1));
            String entrada = scanner.nextLine().toUpperCase().trim();
            try {
                pet.setSexo(SexoPet.valueOf(entrada));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Sexo inválido! Digite: MACHO ou FEMEA.");
            }
        }

        // 3. NOME (Pergunta índice 2)
        while (true) {
            System.out.println(perguntas.get(2));
            String entrada = scanner.nextLine().trim();

            // Regex: ^[A-Za-z ]+$ significa "Começo ao fim, apenas letras e espaços"
            if (entrada.matches("^[A-Za-z ]+$")) {
                pet.setNome(entrada);
                break;
            } else {
                System.out.println("Nome inválido! Use apenas letras (sem números ou símbolos).");
            }
        }

        // 4. IDADE (Pergunta índice 3)
        while (true) {
            System.out.println(perguntas.get(3));
            String entrada = scanner.nextLine().replace(",", ".").trim();
            try {
                double idade = Double.parseDouble(entrada);
                if (idade > 20) {
                    System.out.println("Erro: A idade máxima permitida é 20 anos.");
                } else if (idade < 0) {
                    System.out.println("Erro: Idade não pode ser negativa.");
                } else {
                    // Regra: Idade menor que 1 -> converter para 0.x (já feito pelo double)
                    pet.setIdade(idade);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }

        // 5. PESO (Pergunta índice 4)
        while (true) {
            System.out.println(perguntas.get(4));
            String entrada = scanner.nextLine().replace(",", ".").trim();
            try {
                double peso = Double.parseDouble(entrada);
                if (peso >= 0.5 && peso <= 60.0) {
                    pet.setPeso(peso);
                    break;
                } else {
                    System.out.println("Erro: O peso deve estar entre 0.5kg e 60kg.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }



        // 6. RAÇA (Pergunta índice 5)
        System.out.println(perguntas.get(5));
        String raca = scanner.nextLine().trim();
        if (raca.isEmpty()) {
            pet.setRaca(Pet.NAO_INFORMADO);
        } else if (raca.matches("^[A-Za-z ]+$")) { // Validando se não tem números
            pet.setRaca(raca);
        } else {
            System.out.println("Caracteres inválidos detectados. Salvando como NÃO INFORMADO.");
            pet.setRaca(Pet.NAO_INFORMADO);
        }

        // 7. ENDEREÇO (Pergunta índice 6)
        System.out.println(perguntas.get(6));
        String enderecoCompleto = scanner.nextLine().trim();

        if (enderecoCompleto.isEmpty()) {
            pet.setRua(Pet.NAO_INFORMADO);
            pet.setNumero(Pet.NAO_INFORMADO);
            pet.setCidade(Pet.NAO_INFORMADO);
        } else {
            // Tenta separar por vírgula (Rua, Numero, Cidade)
            String[] dadosEndereco = enderecoCompleto.split(",");
            if (dadosEndereco.length == 3) {
                pet.setRua(dadosEndereco[0].trim());
                pet.setNumero(dadosEndereco[1].trim());
                pet.setCidade(dadosEndereco[2].trim());
            } else {
                // Se o usuário não usou vírgulas, salvamos tudo na rua para não perder dados
                pet.setRua(enderecoCompleto);
                pet.setNumero(Pet.NAO_INFORMADO);
                pet.setCidade(Pet.NAO_INFORMADO);
            }
        }

        System.out.println("Cadastro realizado na memória com sucesso!");
        return pet;
    }
}