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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class Transpor_Arff {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Transpor_Arff ta = new Transpor_Arff();
        try {
            ta.transpor("iris2.arff", "iristeste.arff");
        } catch (IOException ex) {
            Logger.getLogger(Transpor_Arff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void transpor(String oldFile, String newFile) throws FileNotFoundException, IOException {
        createHeader(oldFile, newFile);
        String linha;
        int numeroColunas = getNumeroColunas(oldFile);
        for (int i = 0; i < numeroColunas; i++) {
            StringBuilder dado = new StringBuilder();
            try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
                while (br.ready()) {
                    linha = br.readLine();
                    if (linha.contains("@DATA")) {
                        linha = br.readLine();
                        dado.append(linha.split(",")[i]);
                        while (br.ready()) {
                            linha = br.readLine();
                            dado.append(",").append(linha.split(",")[i]);
                        }
                        //dado.append("\n");
                        salvaLinhaDados(newFile, dado.toString());
                        break;
                    }

                }
                br.close();
                fr.close();
            }
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
            while (br.ready()) {
                linha = br.readLine();
                if (linha.contains("@DATA")) {
                    linha = br.readLine();
                    dados = linha.split(",");
                    return dados.length;
                }
            }
            br.close();
            fr.close();
        }
        return 0;
    }

    private void createHeader(String oldFile, String newFile) throws IOException {
        int numeroLinhas = getNumeroLinhas(oldFile);
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION teste").append("\n\n");
        for (int i = 0; i < numeroLinhas; i++) {
            sb.append("@ATTRIBUTE ");
            sb.append("K").append(i);
            sb.append("	REAL\n");
        }
        sb.append("\n\n");
        sb.append("@DATA");
        salvaLinhaDados(newFile, sb.toString());
    }

    private int getNumeroLinhas(String oldFile) throws FileNotFoundException, IOException {
        int contador = 0;
        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                if (br.readLine().contains("@DATA")) {
                    while (br.ready()) {
                        br.readLine();
                        contador++;
                    }
                }
            }
            br.close();
            fr.close();
        }
        return contador;
    }

}
