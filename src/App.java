import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import entities.Competidor;
import entities.Kartodromo;

public class App {
    public static void main(String[] args) throws Exception {
        Kartodromo kartodromo = new Kartodromo(10, 10);
        ExecutorService executor = Executors.newCachedThreadPool();
        Random random = new Random();
        int numClientesNaoAtendidos = 0;

        List<Competidor> criancas = new ArrayList<>();
        List<Competidor> adultos = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            String nome = "Competidor " + (i + 1);
            int idade;
            if(5 < random.nextInt(10)){
                idade = random.nextInt(7, 15);
                criancas.add(new Competidor(nome, idade, kartodromo));
            }else{
                idade = random.nextInt(15, 71);
                adultos.add(new Competidor(nome, idade, kartodromo));
            }
            numClientesNaoAtendidos++;
        }

        while (numClientesNaoAtendidos > 0) {
            int numChegaram = random.nextInt(2, 16);
            if(numChegaram > numClientesNaoAtendidos){
                numChegaram = numClientesNaoAtendidos;
            }
            int numCrianca = random.nextInt(numChegaram + 1);
            System.out.println("------------- Atendidos nessa rodada: " + numChegaram);
            System.out.println("------------- Num Criança: " + numCrianca);
            System.out.println("------------- Num Adultos: " + (numChegaram - numCrianca));

            List<Competidor> chegando = new ArrayList<>();
            for (int i = 0; i < numChegaram; i++) {
                if (i < numCrianca && !criancas.isEmpty()) {
                    chegando.add(criancas.remove(0));
                } else if(!adultos.isEmpty()) {
                    chegando.add(adultos.remove(0));
                }
            }

            for (Competidor competidor : chegando) {
                executor.execute(competidor);               
            }

            numClientesNaoAtendidos -= chegando.size();
            Thread.sleep(2000); //Tempo ate a proxima chegada de pessoas 
            System.out.println("----------------------------------------------------------------------" + numClientesNaoAtendidos);
        }

       
        executor.shutdown();
        while (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            System.out.println("Aguardando a conclusão de todas as tarefas...");
        }

        kartodromo.relatorio();
    }
}