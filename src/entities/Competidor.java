package entities;

import java.util.concurrent.Semaphore;

public class Competidor implements Runnable {
    private String nome;
    private int idade;
    private Kartodromo kartodromo;
    private boolean possuiKart;
    private boolean possuiCapacete;
    private static Semaphore competidoresAguardando = new Semaphore(0, true); // Semáforo para contar competidores esperando
    public static int totalClientes;

    public Competidor(String nome, int idade, Kartodromo kartodromo) {
        this.nome = nome;
        this.idade = idade;
        this.kartodromo = kartodromo;
        this.possuiKart = false;
        this.possuiCapacete = false;
    }

    public boolean possuiKart() {
        return possuiKart;
    }

    public void setPossuiKart(boolean possuiKart) {
        this.possuiKart = possuiKart;
    }

    public boolean possuiCapacete() {
        return possuiCapacete;
    }

    public void setPossuiCapacete(boolean possuiCapacete) {
        this.possuiCapacete = possuiCapacete;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public void run() {
        try {
            totalClientes++;
            System.out.println(totalClientes);
            if (getIdade() <= 14) {
                competidoresAguardando.release(); // Adiciona ao semáforo quando começa a esperar
            }

            long startTime = System.currentTimeMillis();

            while (true) {
                if (getIdade() <= 14) {
                    setPossuiCapacete(kartodromo.pegarCapacete(this));
                    setPossuiKart(kartodromo.pegarKart(this));
                } else {
                    // Espera enquanto há competidores menores de 15 anos esperando, com timeout de 8000ms
                    while (competidoresAguardando.availablePermits() > 0 &&
                            System.currentTimeMillis() - startTime < 2000) {
                        Thread.sleep(100); // Espera um pouco antes de verificar novamente
                    }
                    setPossuiKart(kartodromo.pegarKart(this));
                    setPossuiCapacete(kartodromo.pegarCapacete(this));
                }

                if (possuiKart() && possuiCapacete()) {
                    if (getIdade() <= 14) {
                        competidoresAguardando.acquire(); // Remove do semáforo quando adquire todos os recursos
                    }
                    kartodromo.correndo(this);
                    //System.out.println(nome + " esta liberando os recursos");
                    kartodromo.liberarRecursos(this);
                    break;
                } else {
                    //System.out.println(nome + " esta liberando os recursos");
                    kartodromo.liberarRecursos(this);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
