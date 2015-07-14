/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transpor_arff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class CreateTAV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CreateTAV ta = new CreateTAV();

        List<String> argumentos = new ArrayList<>(Arrays.asList(args));
        if (argumentos.isEmpty()) {
            help();
        }
        Iterator itr = argumentos.iterator();
        while (itr.hasNext()) {
            String argumento = (String) itr.next();
            switch (argumento) {
                case "-l":
                    try {
                        ta.limpaDados((String) itr.next(), (String) itr.next());
                    } catch (IOException ex) {
                        Logger.getLogger(CreateTAV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case "-u":
                    try {
                        ta.unirDados((String) itr.next(), (String) itr.next());
                    } catch (IOException ex) {
                        Logger.getLogger(CreateTAV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

                case "-c":
                    try {
                        ta.getClasses((String) itr.next(), (String) itr.next());
                    } catch (IOException ex) {
                        Logger.getLogger(CreateTAV.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;

            }
        }
    }

    public static void help() {
        System.out.println("Faltou argumentos passar 3 argumentos:");
        System.out.println("-l sourceFile targetFile");
        System.out.println("-u sourceFile targetFile");
        System.out.println("-c sourceFile targetFile");
        System.exit(0);
    }

    public void unirDados(String oldFile, String newFile) throws FileNotFoundException, IOException {
        createHeader(oldFile, newFile);
        String linha;
        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                salvaLinhaDados(newFile, linha);
            }
            br.close();
            fr.close();
        }
    }

    public void getClasses(String sourceFile, String targetFile) throws FileNotFoundException, IOException {
        String linha;
        try (FileReader fr = new FileReader(sourceFile); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                if (linha.contains("att_class:")) {
                    linha = linha.substring(linha.lastIndexOf("(") + 1, linha.lastIndexOf(")")).replaceAll("\"", "");
                    String classes = "@ATTRIBUTE classe {" + linha + "}";
                    salvaLinhaDados(targetFile, classes);
                    break;
                }
            }
            br.close();
            fr.close();
        }
    }

    public void limpaDados(String oldFile, String newFile) throws FileNotFoundException, IOException {
        String linha;
        String classe = "";
        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                classe += linha.substring(linha.lastIndexOf(",") + 1, linha.length()) + ",";
                linha = linha.replaceAll("\".*\",|", "");
                linha = linha.substring(0, linha.lastIndexOf(","));
                salvaLinhaDados(newFile, linha);
            }
            salvaLinhaDados("classesAtributos.txt", classe);
            br.close();
            fr.close();
        }
    }

    private void salvaLinhaDados(String fileName, String dado) throws IOException {
        try (FileWriter fw = new FileWriter(fileName, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(dado);
            bw.newLine();
            bw.close();
            fw.close();
        }
    }

    private int getNumeroColunas(String oldFile) throws FileNotFoundException, IOException {
        String linha;
        String[] dados;
        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
            linha = br.readLine();
            dados = linha.split(",");
            br.close();
            fr.close();
            return dados.length;
        }
    }

    private void createHeader(String oldFile, String newFile) throws IOException {
        int colunas = getNumeroColunas(oldFile);
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION tabelaInvertida").append("\n\n");
        for (int i = 0; i < colunas; i++) {
            sb.append("@ATTRIBUTE ");
            sb.append("K").append(i);
            sb.append("	REAL\n");
        }
        sb.append("\n\n");
        sb.append("@DATA");
        salvaLinhaDados(newFile, sb.toString());
    }

   

}
