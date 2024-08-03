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

        List<Competidor> criancas = new ArrayList<Competidor>();
        List<Competidor> adultos = new ArrayList<Competidor>();
        List<Competidor> chegando = new ArrayList<Competidor>();


        for (int i = 0; i < 100; i++) {
            String nome = "Competidor " + (i + 1);
            int idade;
            if(5 < random.nextInt(10)){
                idade = random.nextInt(7, 15);
                criancas.add(new Competidor(nome, idade, kartodromo));
            }else{
                idade = random.nextInt(15, 70);
                adultos.add(new Competidor(nome, idade, kartodromo));
            }
            numClientesNaoAtendidos++;
        }

        int i;
        int chegaram = 0;
        //for ( i = 0; i < numClientesNaoAtendidos; i++) {
            int numChegaram = random.nextInt(2, 15);
            int numCrianca = random.nextInt(numChegaram);
            System.out.println("------------- Atendidos nessa rodada: " + numChegaram);
            System.out.println("------------- Num Criança: " + numCrianca);
            System.out.println("------------- Num adultos: " + (numChegaram - numCrianca));
            for( i = 0; i < numChegaram; i++){
                if(i <= numCrianca && criancas.size() != 0){
                    chegando.add(criancas.remove(0));
                    chegaram ++;
                }else if(adultos.size() != 0){
                    chegando.add(adultos.remove(0));
                    chegaram ++;
                }
            }
            numClientesNaoAtendidos -= chegaram;
            int poolSize = chegaram; 
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

            for (Competidor x : chegando) {
                executorService.execute(x);
            }
        //}

        /*for (Competidor crianca : criancas) {
            executor.execute(crianca);
            Thread.sleep(random.nextInt(500));
        }

        for (Competidor adulto : adultos) {
            executor.execute(adulto);
            Thread.sleep(random.nextInt(500));
        }*/
        List<Runnable> NaoChegaram = executor.shutdownNow();
        executor.shutdown();

        
        executor.awaitTermination(5, TimeUnit.SECONDS);
        kartodromo.relatório();

    }
}

/*
    executa 5 threads simultaneamente        

        int poolSize = 5; // Número de threads no pool
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        // Submete cada tarefa ao ExecutorService
        for (MinhaTarefa tarefa : tarefas) {
            executorService.execute(tarefa);
        }


 *         // Simula a chegada de 100 clientes ao longo de 1000 segundos
        for (int i = 0; i < 100; i++) {
            String nome = "Competidor" + i;
            int idade = random.nextInt(50); // Idade entre 0 e 50 anos
            Competidor competidor = new Competidor(nome, idade, kartodromo);
            executor.execute(competidor);
            // Espera um tempo aleatório antes de criar o próximo cliente
            try {
                Thread.sleep(random.nextInt(10)); // Reduzido para teste rápido
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Espera a conclusão de todas as threads
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES); // Reduzido para teste rápido
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
 */