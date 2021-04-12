/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudok;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

/**
 *
 * @author paulo
 */
public class Sudoku {

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws ParseException {
        game();
    }

    public static void game() {

        String matriz[][];
        //vetor auxiliar para verificar uma posição padrão. Já definida no txt, para impedir que o jogador apague sua posição.
        String vetorAux[][];
        
        String caminho = defineNivel();
        matriz = initialize(caminho);
        vetorAux = initialize(caminho);
        Scanner in = new Scanner(System.in);
        boolean status = status(matriz);

        //enquanto status for diferente de true(no fim do while, chamo o método status())
        while (!(status == true)) {

            boolean linhacolunaValida;
            int auxExistente;
            String num = null;
            print(matriz);
            int linha, coluna;

            System.out.println("1 - Fazer uma jogada\n2 - Desfazer uma jogada");
            int jogada = in.nextInt();

            if (jogada == 1) {
                System.out.println("Digite a linha e a coluna separadas por espaço: ");
                linha = in.nextInt();
                coluna = in.nextInt();

                linhacolunaValida = verificaLinhaColuna(linha, coluna, matriz);
                auxExistente = verificaExistente(matriz, vetorAux, linha, coluna, jogada);
                if (linhacolunaValida == true && auxExistente == 1) {
                    System.out.println("Digite o número entre 1 a 9: ");
                    num = in.next();
                    step(linha, coluna, num, matriz, vetorAux, jogada);
                    status = status(matriz);

                }
            } else if (jogada == 2) {
                System.out.println("Digite a linha e a coluna separadas por espaço: ");
                linha = in.nextInt();
                coluna = in.nextInt();

                linhacolunaValida = verificaLinhaColuna(linha, coluna, matriz);
                auxExistente = verificaExistente(matriz, vetorAux, linha, coluna, jogada);
                if (linhacolunaValida == true && auxExistente == 1) {

                    step(linha, coluna, num, matriz, vetorAux, jogada);
                }
            } else {
                System.out.println("Jogada inválida! Redigite.");
            }

        }
    }

    public static String defineNivel() {
        Scanner in = new Scanner(System.in);
        String path = null;
        System.out.println("Escolha o nível do Sudoku:\n1 - Fácil\n2 - Médio\n3 - Difícil");
        
        int nivel = in.nextInt();

        switch (nivel) {
            case 1:
                path = "fácil.txt";
                break;
            case 2:
                path = "médio.txt";
                break;
            case 3:
                path = "dificil.txt";
                break;
            default:
                System.out.println("Valor inválido. Redigite");
                break;
        }
        return path;
    }

    //definirei níveis neste método, através do path.      
    public static String[][] initialize(String path) {
    
        String[][] sudoku = new String[9][9];
        int auxRow = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            //esta sera usada para ler a linha.
            String line = br.readLine();

            //enquanto a linha do buffer nao for vazia
            while (line != null) {
                //atraves de line.split, será criado um array de 9, que é o tamanho da linha lida
                //aqui encontra-se o segredo, lerei apenas os VALORES. "splitando" pelo espaço(vazio) " ".
                //então, a cada while, terei um lineArray, que é basicamente um vetor "temporário".
                String[] lineArray = line.split(" ");
                //percorre o array que foi criado atraves da minha linha
                for (int i = 0; i < lineArray.length; i++) {
                    //confere se o valor do indice é diferente de '_', se for, convertemos e salvamos o valor da posição
                    //(levando em consideração nossa variavel auxiliar, 
                    //que será incrementada sempre que passar no while; economizando outro for(linha e coluna).
                    if (!lineArray[i].equals("_")) {
                        sudoku[auxRow][i] = lineArray[i];
                    } else {
                        sudoku[auxRow][i] = "_";
                    }
                }
                auxRow++;
                //agora lemos a linha para passar no while e continuar o laço
                line = br.readLine();

                //aos casos que forem iguais à "_", não faremos nada, pq por padrão o array é criado com valor 0(no caso de inteiros). 
            }
        } //pegar a mensagem(se ocorrer erro), principalmente quando o arquivo não existir. 
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return sudoku;
    }

    public static void print(String[][] matriz) {

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.printf("[%s]", matriz[i][j]);
            }
            System.out.println(": " + i);

        }

        System.out.println(" 0  1  2  3  4  5  6  7  8");

    }

    public static int step(int l, int c, String num, String[][] m, String[][] n, int decisao) {
        int validoC = verificaColuna(m, c, num);
        int validoL = verificaLinha(m, l, num);
        int verificaExistente = verificaExistente(m, n, l, c, decisao);
        int verificaRegiao = verificaRegiao(m, l, c, num);
        //conforme documento pdf;
        //se for possível(validoC e Valido L -> retorna 1); Se o valor já existir -> 0 
        //decisao -> fazer jogada(1)... 
        if (validoC == 1 && validoL == 1 && decisao == 1 && verificaRegiao == 1 && verificaExistente == 1) {
            m[l][c] = num;
            //retorno 1, mostrando que foi possível inserir na posição.
            return 1;
        } else if (decisao == 2 && verificaExistente == 1) {
            m[l][c] = "_";
            //retorno 1, informando que foi possível zerar a posição.
            return 1;
        } else {
            //retornando -1, informando que ocorreu algum erro.
            return -1;
        }

    }
    //verifico apenas se a posição digitada é válida. Passando como true ou false, porque terei apenas 
    //dois resultados possíveis

    public static boolean verificaLinhaColuna(int l, int c, String m[][]) {
        if (l >= m.length || c >= m[0].length) {
            System.out.println("Posição inválida. Redigite");
            return false;
        } else {
            return true;
        }
    }

    public static int verificaLinha(String m[][], int l, String num) {
        //Mesmo exemplo abaixo, porém, para a linha. O que mudará é apenas a coluna. A linha será a mesma.
        for (int i = 0; i < m.length; i++) {
            if (m[l][i].equals(num)) {
                System.out.println("Impossível preencher, pois o valor já existe nesta linha/vertical");
                return 0;
            }

        }
        return 1;
    }

    public static int verificaColuna(String m[][], int c, String num) {
        //Verifico a Coluna Vertical... Passando no meu if apenas o m[][C]... E o contador faz o trabalho(para as linhas)
        for (int i = 0; i < m[0].length; i++) {
            if (m[i][c].equals(num)) {
                System.out.println("Impossível preencher, pois o valor já existe nesta coluna/vertical");
                return 0;
            }
        }
        return 1;
    }
    //utilizando aritmética, consigo saber onde inicia-se a região. 

    public static int verificaRegiao(String m[][], int l, int c, String num) {
        //variaveis auxiliares, tem o tamanho do "inicial" + 2... Que seria o tamanho da região
        int auxL = (l / 3) * 3 + 2;
        int auxC = (c / 3) * 3 + 2;

        //dentro do meu for, faço a conta... E utilizo a minha variavel auxiliar como base,
        //o mais 2 (+2) é como se percorre de 0 a 2 posições.
        for (int iniLinha = (l / 3) * 3; iniLinha <= auxL; iniLinha++) {
            for (int iniColuna = (c / 3) * 3; iniColuna <= auxC; iniColuna++) {
                if (m[iniLinha][iniColuna].equals(num)) {
                    System.out.println("Impossível preencher, pois o valor já existe nesta região.");
                    return 0;
                }
            }
        }

        return 1;
    }

    public static int verificaExistente(String m[][], String n[][], int l, int c, int decisao) {
        if (!(n[l][c].equals("_"))) {
            System.out.println("Impossível alterar esta posição. Ela pertence ao Sudoku original. Valor: " + n[l][c]);
            return -1;
        } else if (!(m[l][c].equals("_")) && decisao == 1) {
            System.out.println("Posição já preenchida");
            return -1;
        } else {
            return 1;
        }
    }

    public static boolean status(String m[][]) {

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                if (m[i][j].equals("_")) {
                    return false;
                }
            }
        }
        System.out.println("PARABÉNS!!! VOCÊ FINALIZOU O JOGO(Coragem, ein). RESULTADO:");
        print(m);
        return true;
    }

}
