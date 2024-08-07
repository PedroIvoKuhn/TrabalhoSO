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

        List<Competidor> criancas = new ArrayList<>();
        List<Competidor> adultos = new ArrayList<>();

        for (int i = 0; i < 300; i++) {  
            String nome = "Competidor " + (i + 1);
            int idade;
            if(5 < random.nextInt(10)){
                idade = random.nextInt(7, 15);
                criancas.add(new Competidor(nome, idade, kartodromo));
            }else{
                idade = random.nextInt(15, 71);
                adultos.add(new Competidor(nome, idade, kartodromo));
            }
        }

        //Faça algo que pare exatamente nesse tempo
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 16000) {
            int numChegaram = random.nextInt(12, 16);
            int numClientes = adultos.size() + criancas.size();
            if(numChegaram > numClientes){
                numChegaram = numClientes;
            }
            int numCrianca = random.nextInt(numChegaram + 1);
            if(numCrianca > criancas.size()){
                numCrianca = criancas.size();
            }
            System.out.println("------------- Atendidos nessa rodada: " + numChegaram);
            System.out.println("------------- Num Criança: " + numCrianca);
            System.out.println("------------- Num Adultos: " + (numChegaram - numCrianca));

            List<Competidor> chegando = new ArrayList<>();
            for (int i = 0; i < numChegaram; i++) {
                if (i < numCrianca && !criancas.isEmpty()) {
                    chegando.add(criancas.remove(0));
                } else if(!adultos.isEmpty()) {
                    chegando.add(adultos.remove(0));
                } else {
                    chegando.add(criancas.remove(0));
                }
            }

            for (Competidor competidor : chegando) {
                executor.execute(competidor);               
            }

            Thread.sleep(1000); //Tempo ate a proxima chegada de pessoas 
        }

        executor.shutdown();
        boolean terminated = executor.awaitTermination(0, TimeUnit.SECONDS);
        if (!terminated) {
            System.out.println("Algumas tarefas não foram concluídas a tempo.");
        }

        kartodromo.relatorio();
    }
}