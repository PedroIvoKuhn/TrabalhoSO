import entities.Competidor;
import entities.Kartodromo;

public class App {
    public static void main(String[] args) throws Exception {
        Kartodromo kartodromo = new Kartodromo(2, 2);

        Thread x = new Thread( new Competidor("\tfulano", 16, kartodromo));
        Thread y = new Thread( new Competidor("ciclano", 14, kartodromo));
        y.setPriority(10);
        System.out.println("Prioridade de x:" +x.getPriority() +" y: " + y.getPriority());

        x.start();
        y.start();


        x.join();
        y.join();
        kartodromo.relatório();
    }
}
