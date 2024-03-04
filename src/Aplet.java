import javax.swing.*;
import java.awt.*;

public class Aplet  {
    private Thread hilo1;
    private Thread hilo2;
    private JButton btnComenzarProceso;
    private JButton btnInterrumpirHilo1;
    private JButton btnInterrumpirHilo2;
    private JButton btnFinalizarProceso;
    private ContadorHilo tarea1;
    private ContadorHilo tarea2;

    public Aplet() {

        JFrame frame = new JFrame("Gestion de Hilos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new FlowLayout());

        btnComenzarProceso = new JButton("Comenzar ");
        btnInterrumpirHilo1 = new JButton("Hilo 1");
        btnInterrumpirHilo2 = new JButton("Hilo 2");
        btnFinalizarProceso = new JButton("Finalizar ");

        panel.add(btnComenzarProceso);
        panel.add(btnInterrumpirHilo1);
        panel.add(btnInterrumpirHilo2);
        panel.add(btnFinalizarProceso);

        tarea1 = new ContadorHilo("Hilo 1");
        tarea2 = new ContadorHilo("Hilo 2");

        btnComenzarProceso.addActionListener(e -> {
            hilo1 = new Thread(tarea1);
            hilo2 = new Thread(tarea2);
            hilo1.start();
            hilo2.start();
            btnComenzarProceso.setEnabled(false);
        });

        btnInterrumpirHilo1.addActionListener(e -> hilo1.interrupt());
        btnInterrumpirHilo2.addActionListener(e -> hilo2.interrupt());

        btnFinalizarProceso.addActionListener(e -> {
            hilo1.interrupt();
            hilo2.interrupt();
            try {
                hilo1.join();
                hilo2.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.out.println(tarea1.getContador());
            System.out.println(tarea2.getContador());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Aplet::new);
    }

    private static class ContadorHilo implements Runnable {
        private final String nombre;
        private int contador = 0;

        public ContadorHilo(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    contador++;
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println(nombre + " finalizado. Contador: " + contador);
            }
        }

        public int getContador() {
            return contador;
        }
    }
}

