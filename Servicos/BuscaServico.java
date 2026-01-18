package Servicos;

import ModelosPets.Pet;     // <--- IMPORT
import ModelosPets.TipoPet; // <--- IMPORT
import java.text.Normalizer;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BuscaServico {

    private final ServicoArquivo servicoArquivo;

    // Códigos para colorir o terminal (Negrito)
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_RESET = "\u001B[0m";

    public BuscaServico() {
        this.servicoArquivo = new ServicoArquivo();
    }

    public void buscarPets(Scanner scanner) {
        System.out.println("\n--- BUSCA DE PETS ---");

        System.out.println("Filtre por TIPO (CACHORRO, GATO, OUTRO) ou ENTER para todos:");
        String filtroTipoStr = scanner.nextLine().toUpperCase().trim();

        System.out.println("Termo de busca (Nome, Raça ou Cidade) ou ENTER para ignorar:");
        String termoBusca = scanner.nextLine().trim();

        List<Pet> todosPets = servicoArquivo.lerTodosOsPets();

        if (todosPets.isEmpty()) {
            System.out.println("Nenhum pet cadastrado.");
            return;
        }

        List<Pet> encontrados = todosPets.stream()
                .filter(pet -> {
                    // 1. Filtro de Tipo
                    if (!filtroTipoStr.isEmpty()) {
                        try {
                            if (pet.getTipo() != TipoPet.valueOf(filtroTipoStr)) return false;
                        } catch (IllegalArgumentException e) {
                            // Se digitou tipo inválido, ignora esse filtro
                        }
                    }

                    // 2. Filtro de Texto (Nome, Raça, Cidade)
                    if (!termoBusca.isEmpty()) {
                        String termo = normalizar(termoBusca);
                        return normalizar(pet.getNome()).contains(termo) ||
                                normalizar(pet.getRaca()).contains(termo) ||
                                normalizar(pet.getCidade()).contains(termo);
                    }
                    return true;
                })
                .collect(Collectors.toList());

        exibirResultados(encontrados, termoBusca);
    }

    private void exibirResultados(List<Pet> pets, String termo) {
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet encontrado.");
            return;
        }

        System.out.println("\nEncontrado(s): " + pets.size());
        for (Pet p : pets) {
            System.out.println("--------------------------------");
            // Destaca o nome se bater com a busca
            String nomeExibicao = p.getNome();
            if (!termo.isEmpty() && normalizar(nomeExibicao).contains(normalizar(termo))) {
                nomeExibicao = ANSI_BOLD + nomeExibicao + ANSI_RESET;
            }

            System.out.println("Nome: " + nomeExibicao);
            System.out.println("Tipo: " + p.getTipo() + " | Raça: " + p.getRaca());
            System.out.println("Local: " + p.getCidade());
        }
    }

    private String normalizar(String texto) {
        if (texto == null) return "";
        String nfd = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(nfd).replaceAll("").toLowerCase();
    }
}