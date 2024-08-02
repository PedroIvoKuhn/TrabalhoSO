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

        /*
         * arraylist de competidor
         * 
         *  for para criar os competidores, crianças e adultos
         *      duas filas ou uma?
         *      crianças têm prioridade mas adultos tambem tem que correr
         * 
         *  manda executar em lotes
         *      fazer um for, esperando por um tempo aleatorio e mandar executar
         *      
         */

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