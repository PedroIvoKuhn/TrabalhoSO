package entities;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Kartodromo{
    // colocar o tempo de um dia? para ficar esperando pelo recurso
    Random random = new Random();
    private int TEMPO_MAX_ESPERA = 1;
    private Semaphore karts;
    private Semaphore capacetes;
    private int clientesAtendidos;

    public Kartodromo(int numKarts, int numCapacetes) {
        this.karts = new Semaphore(numKarts, true);
        this.capacetes = new Semaphore(numCapacetes, true);
        this.clientesAtendidos = 0;
    }

    public boolean pegarKart(Competidor competidor){
        try {
            if (karts.tryAcquire(TEMPO_MAX_ESPERA, TimeUnit.SECONDS)) {
                competidor.setPossuiKart(true);
                System.out.println(competidor.getNome() + " pegou um kart");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean pegarCapacete(Competidor competidor){
        try {
            if (capacetes.tryAcquire(TEMPO_MAX_ESPERA, TimeUnit.SECONDS)) {
                competidor.setPossuiCapacete(true);
                System.out.println(competidor.getNome() + " pegou um capacete");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public void correndo(Competidor competidor){
        try {
            // A idade é o numero de segundos da volta
            System.out.println(competidor.getNome() + " Idade: " + competidor.getIdade()+ " " + " está correndo...");
            clientesAtendidos++;
            Thread.sleep(random.nextInt(500, 6000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liberarRecursos(Competidor competidor){
        if (competidor.possuiCapacete()) {
            capacetes.release();
        }

        if (competidor.possuiKart()) {
            karts.release();
        }
    }

    public void relatorio(){
        System.out.println("karts disponiveis: " + karts.availablePermits());
        System.out.println("capacetes disponiveis: " + capacetes.availablePermits());
        System.out.println("Clientes Atendidos: " + clientesAtendidos);
    }
}
