package entities;

public class Competidor implements Runnable {
    private String nome;
    private int idade;
    private Kartodromo kartodromo;
    private boolean possuiKart;
    private boolean possuiCapacete;

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
        while (true) {
            if (getIdade() <= 14) {
                setPossuiCapacete(kartodromo.pegarCapacete(this));
                setPossuiKart(kartodromo.pegarKart(this));
            } else {
                setPossuiKart(kartodromo.pegarKart(this));
                setPossuiCapacete(kartodromo.pegarCapacete(this));
            }
            
            if (possuiKart() && possuiCapacete()) {
                kartodromo.correndo(this);
                break;
            }else{
                System.out.println(nome + " esta liberando os recursos");
                kartodromo.liberarRecursos(this);
            }  
        }
        System.out.println(nome + " esta liberando os recursos");
        kartodromo.liberarRecursos(this);
    }
    
}
