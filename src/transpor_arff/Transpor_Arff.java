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
        if (args.length < 2) {
            
        }

        try {
            ta.transpor("teste3.txt", "teste4.arff");
        } catch (IOException ex) {
            Logger.getLogger(Transpor_Arff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void transpor(String oldFile, String newFile) throws FileNotFoundException, IOException {
        createHeader(oldFile, newFile);
        String linha;
        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
//                        porcentagem = (100 * i) / numeroColunas;
//                        System.out.print("\r" + porcentagem + "%");
                salvaLinhaDados(newFile, linha);
            }
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

    private int getNume(String oldFile) throws FileNotFoundException, IOException {
        int contador = 0;
        String linha = "";
        try (FileReader fr = new FileReader(oldFile); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                if (br.readLine().contains("@DATA")) {
                    linha = br.readLine();
                    if (linha.length() > 2) {
                        contador++;
                    }
                    while (br.ready()) {
                        linha = br.readLine();
                        if (linha.length() < 2) {
                            break;
                        }
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
