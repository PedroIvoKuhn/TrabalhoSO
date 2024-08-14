package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Kartodromo{
    // 8 horas =  16s //  1h = 2s // 30m = 1s // 15m = 500ms // 1m = 333ms
    Random random = new Random();
    private int TEMPO_MAX_ESPERA = 333;
    private Semaphore karts;
    private Semaphore capacetes;
    private Semaphore clientesAtendidos = new Semaphore(0);
    private Semaphore clientesNaoAtendidos= new Semaphore(0);
    private Semaphore clientesCorrendo= new Semaphore(0);
    private Semaphore clientesTentandoCorrer= new Semaphore(0);
    private Semaphore numeroDeCriancasNaoAtendidas= new Semaphore(0);
    private Semaphore numeroDeAdultosNaoAtendidas= new Semaphore(0);

    private List<Thread> filaCriancas = new ArrayList<Thread>();
    private List<Thread> filaAdultos = new ArrayList<Thread>();
    private List<Thread> filaCorrer = new ArrayList<Thread>();    
    private List<Thread> filaTentandoCorrendo = new ArrayList<Thread>();    

    public Kartodromo(int numKarts, int numCapacetes) {
        this.karts = new Semaphore(numKarts, true);
        this.capacetes = new Semaphore(numCapacetes, true);
    }

    public boolean pegarKart(Competidor competidor) throws InterruptedException{
        if (karts.tryAcquire(TEMPO_MAX_ESPERA, TimeUnit.MILLISECONDS)) {
            competidor.setPossuiKart(true);
            return true;
        }
        liberarRecursos(competidor);
        return false;
    }

    public boolean pegarCapacete(Competidor competidor) throws InterruptedException{
        if (capacetes.tryAcquire(TEMPO_MAX_ESPERA, TimeUnit.MILLISECONDS)) {
            competidor.setPossuiCapacete(true);
            return true;
        }
        liberarRecursos(competidor);
        return false;
    }

    public void correndo(Competidor competidor) throws InterruptedException{
        clientesAtendidos.release();
        clientesNaoAtendidos.acquire();
        clientesTentandoCorrer.acquire();
        clientesCorrendo.release();
        if (competidor.getIdade() < 15) {
            numeroDeCriancasNaoAtendidas.acquire();
        } else {
            numeroDeAdultosNaoAtendidas.acquire();
        }
        Thread.sleep(random.nextInt(800, 1200));
        clientesCorrendo.acquire();
        liberarRecursos(competidor);
    }

    public void liberarRecursos(Competidor competidor){
        if (competidor.possuiCapacete()){   
            competidor.setPossuiCapacete(false); 
            capacetes.release();
        }
        if (competidor.possuiKart()){
            competidor.setPossuiKart(false);
            karts.release();
        }
    }

    public void addCompetidor(Competidor competidor){
        if (competidor.getIdade() < 15) {
            numeroDeCriancasNaoAtendidas.release();
            Thread thread = new Thread(competidor);
            thread.setPriority(7);
            filaCriancas.add(thread);
        }else{
            numeroDeAdultosNaoAtendidas.release();
            filaAdultos.add(new Thread(competidor));
        }
    }

    public void vaoCorrer(int chegaram){
        int maxCompetidores = filaAdultos.size() + filaCriancas.size();
        if (chegaram > maxCompetidores){
            chegaram = maxCompetidores;
        }
        
        //int numCriancas = random.nextInt(chegaram + 1);
        int numCriancas = chegaram/2;
        for (int i = 0; i < chegaram; i++) {
            if (i < numCriancas) {
                filaCorrer.add((filaCriancas.size() != 0) ? filaCriancas.remove(0) 
                : filaAdultos.remove(0));
            } else {
                filaCorrer.add((filaAdultos.size() != 0) ? filaAdultos.remove(0) 
                : filaCriancas.remove(0));
            }
            clientesNaoAtendidos.release();
        }

        System.out.print("\033[H\033[2J");  
        System.out.flush();
        System.out.println("------------- Atendidos nessa rodada: " + chegaram);
        System.out.println("------------- Num Crianças: " + numCriancas);
        System.out.println("------------- Num Adultos: " + (chegaram - numCriancas));
        System.out.println("------------- Num Fila: " + filaCorrer.size());
        System.out.println("------------- Num Correndo: " + clientesCorrendo.availablePermits());
        relatorio();

        for (int i = 0; i < filaCorrer.size(); i++) {
            Thread thread = filaCorrer.remove(0);
            thread.start();
            filaTentandoCorrendo.add(thread);
            clientesTentandoCorrer.release();
        }
    }

    public void relatorio(){
        System.out.println("====================================================");
        System.out.println("Karts disponiveis: " + karts.availablePermits());
        System.out.println("Capacetes disponiveis: " + capacetes.availablePermits());
        System.out.println("Clientes Atendidos: " + clientesAtendidos.availablePermits());
        System.out.println("Clientes não Atendidos: " + clientesNaoAtendidos.availablePermits());
        System.out.println("Crianças não atendidas: " + numeroDeCriancasNaoAtendidas.availablePermits());
        System.out.println("Adultos não atendidos: " + numeroDeAdultosNaoAtendidas.availablePermits());
        System.out.println("Clientes tentando correr: " + clientesTentandoCorrer.availablePermits());
        System.out.println("====================================================");

    }

    public void fecharKartodromo(){
        for (Thread competidor : filaTentandoCorrendo) {
            competidor.interrupt();
        }
    }
}
