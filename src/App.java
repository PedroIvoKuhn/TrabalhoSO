import java.util.Random;

import entities.Competidor;
import entities.Kartodromo;
import entities.enums.RelacaoTempo;

public class App {
    public static void main(String[] args) throws Exception {
        Kartodromo kartodromo = new Kartodromo(10, 10);
        Random random = new Random();

        for (int i = 0; i < 200; i++) {  
            String nome = "Competidor " + (i + 1);
            int idade;
            //if(5 < random.nextInt(10)){
            if(i < 100){
                idade = random.nextInt(7, 15);
            }else{
                idade = random.nextInt(15, 71);
            }
            kartodromo.addCompetidor(new Competidor(nome, idade, kartodromo));
        }

        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < RelacaoTempo.OITO_HORAS.getValor()) {
            kartodromo.vaoCorrer(random.nextInt(5, 16));
            //Tempo ate a proxima chegada de pessoas
            Thread.sleep(RelacaoTempo.TRINTA_MINUTOS.getValor()); 
        }

        System.out.print("\033[H\033[2J");  
        System.out.flush();
        kartodromo.fecharKartodromo();
        System.out.println("=============== FECHANDO KARTODROMO ===============");
        Thread.sleep(1000);
        kartodromo.relatorio();
    }
}