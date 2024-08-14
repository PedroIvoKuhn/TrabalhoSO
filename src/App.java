import java.util.Random;

import entities.Competidor;
import entities.Kartodromo;

public class App {
    public static void main(String[] args) throws Exception {
        Kartodromo kartodromo = new Kartodromo(10, 10);
        Random random = new Random();

        // Gerando competidores para o kartodromo
        for (int i = 0; i < 200; i++) {  
            String nome = "Competidor " + (i + 1);
            int idade;
            if(5 < random.nextInt(10)){
                idade = random.nextInt(7, 15);
            }else{
                idade = random.nextInt(15, 71);
            }
            kartodromo.addCompetidor(new Competidor(nome, idade, kartodromo));
        }

        //Faça algo que pare exatamente nesse tempo
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 16000) {
            kartodromo.vaoCorrer(random.nextInt(5, 16));

            Thread.sleep(1000); //Tempo ate a proxima chegada de pessoas 
        }

        System.out.print("\033[H\033[2J");  
        System.out.flush();
        kartodromo.fecharKartodromo();
        System.out.println("======== FECHANDO KARTODROMO ========");
        Thread.sleep(1000);
        kartodromo.relatorio();
    }
}