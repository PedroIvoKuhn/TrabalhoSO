package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Kartodromo{
    // 8 horas =  16s //  1h = 2s
    Random random = new Random();
    private int TEMPO_MAX_ESPERA = 5;
    private Semaphore karts;
    private Semaphore capacetes;
    private int clientesAtendidos;
    private int clientesNaoAtendidos;

    private List<Competidor> filaCriancas = new ArrayList<Competidor>();
    private List<Competidor> filaAdultos = new ArrayList<Competidor>();
    private List<Competidor> filaCorrer = new ArrayList<Competidor>();
    

    public Kartodromo(int numKarts, int numCapacetes) {
        this.karts = new Semaphore(numKarts, true);
        this.capacetes = new Semaphore(numCapacetes, true);
        this.clientesAtendidos = 0;
    }

    public boolean pegarKart(Competidor competidor){
        try {
            if (karts.tryAcquire(TEMPO_MAX_ESPERA, TimeUnit.SECONDS)) {
                competidor.setPossuiKart(true);
                //System.out.println(competidor.getNome() + " Idade:" + competidor.getIdade() + " pegou um kart");
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
                //System.out.println(competidor.getNome() + " Idade:" + competidor.getIdade() + " pegou um capacete");
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
            //System.out.println(competidor.getNome() + " Idade: " + competidor.getIdade()+ " " + " está correndo...");
            clientesAtendidos++;
            Thread.sleep(random.nextInt(800, 1200));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liberarRecursos(Competidor competidor){
        if (competidor.possuiCapacete())
            capacetes.release();
        if (competidor.possuiKart())
            karts.release();
    }

    public void addCompetidor(Competidor competidor){
        if (competidor.getIdade() < 15) {
            filaCriancas.add(competidor);
        }else{
            filaAdultos.add(competidor);
        }
        clientesNaoAtendidos++;
    }

    public void vaoCorrer(int chegaram, ExecutorService executor){
        int maxCompetidores = filaAdultos.size() + filaCriancas.size();
        if (chegaram > maxCompetidores)
            chegaram = maxCompetidores;

        int numCriancas = random.nextInt(chegaram + 1);
        for (int i = 0; i < chegaram; i++) {
            if (i < numCriancas) {
                filaCorrer.add((filaCriancas.size() != 0) ? filaCriancas.remove(0) 
                                                          : filaAdultos.remove(0));
            } else {
                filaCorrer.add((filaAdultos.size() != 0) ? filaAdultos.remove(0) 
                                                         : filaCriancas.remove(0));
            }
        }

        System.out.print("\033[H\033[2J");  
        System.out.flush();
        System.out.println("------------- Atendidos nessa rodada: " + chegaram);
        System.out.println("------------- Num Crianças: " + numCriancas);
        System.out.println("------------- Num Adultos: " + (chegaram - numCriancas));
        System.out.println("------------- Num Fila: " + filaCorrer.size());
        relatorio();

        for (int i = 0; i < filaCorrer.size(); i++) {
            executor.execute(filaCorrer.remove(0));
            clientesAtendidos++;
            clientesNaoAtendidos--;
        }
    }

    public void relatorio(){
        System.out.println("Karts disponiveis: " + karts.availablePermits());
        System.out.println("Capacetes disponiveis: " + capacetes.availablePermits());
        System.out.println("Total de Crianças: " + filaCriancas.size());
        System.out.println("Total de Adultos: " + filaAdultos.size());
        System.out.println("Clientes Atendidos: " + clientesAtendidos);
        System.out.println("Clientes não Atendidos: " + clientesNaoAtendidos);
    }
}
