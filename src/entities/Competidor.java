package entities;

public class Competidor implements Runnable {
    private String nome;
    private int idade;
    private Kartodromo kartodromo;
    private boolean possuiKart;
    private boolean possuiCapacete;
    private boolean correu;
    private int tentativas;

    public Competidor(String nome, int idade, Kartodromo kartodromo) {
        this.nome = nome;
        this.idade = idade;
        this.kartodromo = kartodromo;
        this.possuiKart = false;
        this.possuiCapacete = false;
        this.correu = false;
        this.tentativas = 0;
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

    public boolean isCorreu() {
        return correu;
    }

    public void setCorreu(boolean correu) {
        this.correu = correu;
    }
    
    public int getTentativas() {
        return tentativas;
    }

    public void addTentativas() {
        this.tentativas++;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !isCorreu()) {
            try {
                if (getIdade() <= 14) {
                    setPossuiCapacete(kartodromo.pegarCapacete(this));
                    if (possuiCapacete()) {
                    setPossuiKart(kartodromo.pegarKart(this));
                    }
                } else {
                    setPossuiKart(kartodromo.pegarKart(this));
                    if (possuiKart()) {
                    setPossuiCapacete(kartodromo.pegarCapacete(this));
                    }   
                }
            
                if (possuiKart() && possuiCapacete()) {
                    kartodromo.correndo(this);
                    setCorreu(true);
                }

                if (getTentativas() == 25) {
                    Thread.currentThread().setPriority(9);
                }
                addTentativas();
            } catch (InterruptedException e) {
                kartodromo.liberarRecursos(this);
                Thread.currentThread().interrupt();
            }
        }
    }
}
