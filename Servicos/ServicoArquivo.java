package Servicos;

import ModelosPets.Pet;
import ModelosPets.SexoPet;
import ModelosPets.TipoPet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServicoArquivo {

    private static final String DIRETORIO_PETS = "petsCadastrados";

    /**
     * Responsável por salvar o objeto ModeloPet.Pet em um arquivo .txt
     */
    public void salvarPet(Pet pet) {
        try {
            // 1. Garante que a pasta existe. Se não existir, cria.
            Path caminhoDiretorio = Paths.get(DIRETORIO_PETS);
            if (!Files.exists(caminhoDiretorio)) {
                Files.createDirectories(caminhoDiretorio);
            }

            // 2. Gera o nome do arquivo: YYYYMMDDTHHMM-NOME.TXT
            String nomeArquivo = gerarNomeArquivo(pet.getNome());
            Path caminhoArquivo = caminhoDiretorio.resolve(nomeArquivo);

            // 3. Escreve os dados (BufferedWriter é eficiente para escrita de texto)
            try (BufferedWriter writer = Files.newBufferedWriter(caminhoArquivo, StandardCharsets.UTF_8)) {

                // Escrevemos linha por linha, na ordem do formulário original
                writer.write(pet.getTipo().toString());
                writer.newLine();

                writer.write(pet.getSexo().toString());
                writer.newLine();

                writer.write(pet.getNome());
                writer.newLine();

                writer.write(String.valueOf(pet.getIdade()));
                writer.newLine();

                writer.write(String.valueOf(pet.getPeso()));
                writer.newLine();

                writer.write(pet.getRaca());
                writer.newLine();

                // Regra: Endereço salvo em uma única linha
                String enderecoCompleto = pet.getRua() + ", " + pet.getNumero() + ", " + pet.getCidade();
                writer.write(enderecoCompleto);

                // Nota: Não precisamos de writer.newLine() na última linha se não houver mais nada
            }

            System.out.println("Arquivo gerado com sucesso: " + caminhoArquivo.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    /**
     * Gera o nome formatado conforme a regra: YYYYMMDDTHHMM-NOMEEMAIUSCULO.TXT
     */
    private String gerarNomeArquivo(String nomePet) {
        // Pega data e hora atual
        LocalDateTime agora = LocalDateTime.now();

        // Formatador para YYYYMMDDTHHMM
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");
        String dataFormatada = agora.format(formatador);

        // Limpa o nome para evitar caracteres inválidos no nome do arquivo (opcional mas recomendado)
        // Aqui vamos seguir a regra estrita: NOME EM MAIUSCULO
        String nomeLimpo = nomePet.toUpperCase();

        return dataFormatada + "-" + nomeLimpo + ".txt";
    }
// ... (mantenha o método salvarPet e gerarNomeArquivo iguais) ...

    public List<Pet> lerTodosOsPets() {
        List<Pet> pets = new ArrayList<>();
        Path diretorio = Paths.get(DIRETORIO_PETS);

        if (!Files.exists(diretorio)) return pets;

        try (Stream<Path> caminhos = Files.list(diretorio)) {
            // AQUI ESTÁ O TRUQUE: .sorted()
            List<Path> arquivosOrdenados = caminhos
                    .filter(Files::isRegularFile)
                    .sorted() // Garante ordem alfabética
                    .collect(Collectors.toList());

            for (Path arquivo : arquivosOrdenados) {
                Pet pet = converterArquivoParaPet(arquivo);
                if (pet != null) pets.add(pet);
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar: " + e.getMessage());
        }
        return pets;
    }
    private Pet converterArquivoParaPet(Path caminhoArquivo) {
        try {
            List<String> linhas = Files.readAllLines(caminhoArquivo, StandardCharsets.UTF_8);

            // Validação de linhas
            if (linhas.size() < 7) {
                System.out.println("Arquivo ignorado (incompleto): " + caminhoArquivo.getFileName());
                return null;
            }

            // DEBUG 3: Tentar converter e avisar se falhar
            TipoPet tipo = TipoPet.valueOf(linhas.get(0)); // Pode dar erro se o texto não for EXATAMENTE igual ao Enum
            SexoPet sexo = SexoPet.valueOf(linhas.get(1));
            String nome = linhas.get(2);
            double idade = Double.parseDouble(linhas.get(3));
            double peso = Double.parseDouble(linhas.get(4));
            String raca = linhas.get(5);

            String[] dadosEnd = linhas.get(6).split(",");
            String rua = dadosEnd.length > 0 ? dadosEnd[0].trim() : "";
            String numero = dadosEnd.length > 1 ? dadosEnd[1].trim() : "";
            String cidade = dadosEnd.length > 2 ? dadosEnd[2].trim() : "";

            return new Pet(tipo, sexo, nome, idade, peso, raca, rua, numero, cidade);

        } catch (NumberFormatException e) {
            System.err.println("Erro de Número no arquivo " + caminhoArquivo.getFileName() + ": " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de Enum no arquivo " + caminhoArquivo.getFileName() + ": " + e.getMessage());
            // Isso acontece se no txt estiver "Cachorro" e o Enum for "CACHORRO" (case sensitive)
        } catch (Exception e) {
            System.err.println("Erro genérico no arquivo " + caminhoArquivo.getFileName());
            e.printStackTrace(); // Mostra o erro completo no terminal
        }
        return null;
    }
    public List<Path> listarArquivos() {
        Path diretorio = Paths.get(DIRETORIO_PETS);
        try (Stream<Path> caminhos = Files.list(diretorio)) {
            return caminhos
                    .filter(Files::isRegularFile)
                    .sorted() // MESMA ordenação do método de cima
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    public boolean deletarArquivo(Path caminho) {
        try {
            return Files.deleteIfExists(caminho);
        } catch (IOException e) {
            System.err.println("Erro ao deletar arquivo antigo: " + e.getMessage());
            return false;
        }
    }
}