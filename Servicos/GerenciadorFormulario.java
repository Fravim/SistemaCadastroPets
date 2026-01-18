package Servicos;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorFormulario {
    private static final String CAMINHO_ARQUUIVO = "src/formulario.txt";
    public List<String> lerPerguntas() {
        List<String> perguntas = new ArrayList<>();
        Path Caminho = Paths.get(CAMINHO_ARQUUIVO);

        try (BufferedReader br = Files.newBufferedReader(Caminho, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    perguntas.add(linha);
                }
            }
        }
        catch (IOException e) {
            System.err.println("Erro ao ler o arquivo");
            System.err.println("Verifique os dados do arquivo: " + CAMINHO_ARQUUIVO);
            System.err.println("Detalhe do erro: " + e.getMessage());
        }
        return perguntas;
    }
    public void exibirFormularioNoTerminal() {
        List<String> perguntas = lerPerguntas();

        if (perguntas.isEmpty()) {
            System.out.println("O formulário está vazio ou não foi encontrado.");
        } else {
            System.out.println("--- FORMULÁRIO DE CADASTRO ---");
            for (String pergunta : perguntas) {
                System.out.println(pergunta);
            }
            System.out.println("------------------------------");
        }
    }



}
