import java.util.*;

// Main game class
public class CurlysMultiPieceMino {
    private List<Ficha> pozo;
    private List<Ficha> mesa;
    private List<Ficha> jugador1;
    private List<Ficha> jugador2;
    private int puntosJugador1;
    private int puntosJugador2;

    public CurlysMultiPieceMino() {
        pozo = new ArrayList<>();
        mesa = new ArrayList<>();
        jugador1 = new ArrayList<>();
        jugador2 = new ArrayList<>();
        puntosJugador1 = 0;
        puntosJugador2 = 0;
    }

    public void inicializarJuego() {
        generarFichas();
        Collections.shuffle(pozo);

        for (int i = 0; i < 10; i++) {
            jugador1.add(pozo.remove(0));
            jugador2.add(pozo.remove(0));
        }
    }

    private void generarFichas() {
        // Fichas de Dominó
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                pozo.add(new Ficha(i, j));
            }
        }

        // Fichas de Tridominó
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                for (int k = j; k <= 6; k++) {
                    pozo.add(new FichaTridomino(i, j, k));
                }
            }
        }
    }

    public void jugar() {
        inicializarJuego();
        Scanner scanner = new Scanner(System.in);

        // Determinar el primer jugador
        boolean turnoJugador1 = determinarPrimerJugador();

        while (!jugador1.isEmpty() && !jugador2.isEmpty() && !pozo.isEmpty()) {
            mostrarMesa();

            List<Ficha> jugadorActual = turnoJugador1 ? jugador1 : jugador2;
            int puntosActual = turnoJugador1 ? puntosJugador1 : puntosJugador2;
            String jugadorNombre = turnoJugador1 ? "Jugador 1" : "Jugador 2";

            // Mostrar las fichas del jugador
            System.out.println(jugadorNombre + ", tus fichas:");
            for (int i = 0; i < jugadorActual.size(); i++) {
                System.out.println((i + 1) + ":\n" + jugadorActual.get(i) + "\n");
            }

            // Solicitar al jugador que seleccione una ficha
            System.out.println("Selecciona una ficha (1 a " + jugadorActual.size() + ") o ingresa 0 para robar del pozo:");
            int eleccion = scanner.nextInt();

            if (eleccion == 0) {
                if (!pozo.isEmpty()) {
                    Ficha nuevaFicha = pozo.remove(0);
                    jugadorActual.add(nuevaFicha);
                    System.out.println("Robaste: " + nuevaFicha);
                } else {
                    System.out.println("El pozo está vacío.");
                }
            } else if (eleccion > 0 && eleccion <= jugadorActual.size()) {
                Ficha fichaSeleccionada = jugadorActual.get(eleccion - 1);

                if (mesa.isEmpty() || puedeColocarFicha(fichaSeleccionada)) {
                    mesa.add(fichaSeleccionada);
                    jugadorActual.remove(fichaSeleccionada);
                    puntosActual += sumaPuntos(fichaSeleccionada);

                    if (turnoJugador1) {
                        puntosJugador1 = puntosActual;
                    } else {
                        puntosJugador2 = puntosActual;
                    }

                    System.out.println("Colocaste: " + fichaSeleccionada);
                } else {
                    System.out.println("No puedes colocar esa ficha. Intenta nuevamente.");
                }
            } else {
                System.out.println("Opción inválida. Intenta nuevamente.");
            }

            turnoJugador1 = !turnoJugador1;
        }

        determinarGanador();
    }

    private void mostrarMesa() {
        System.out.println("Mesa de juego:");
        for (Ficha ficha : mesa) {
            System.out.println(ficha + "\n=================\n");
        }
    }

    private boolean puedeColocarFicha(Ficha ficha) {
        if (mesa.isEmpty()) {
            return true; // Si la mesa está vacía, cualquier ficha puede colocarse
        }

        Ficha ultimaFicha = mesa.get(mesa.size() - 1);

        if (ficha instanceof FichaTridomino) {
            FichaTridomino fichaTri = (FichaTridomino) ficha;
            return (ultimaFicha.getLado1() == fichaTri.getLado1() ||
                    ultimaFicha.getLado1() == fichaTri.getLado2() ||
                    ultimaFicha.getLado1() == fichaTri.getLado3() ||
                    ultimaFicha.getLado2() == fichaTri.getLado1() ||
                    ultimaFicha.getLado2() == fichaTri.getLado2() ||
                    ultimaFicha.getLado2() == fichaTri.getLado3());
        } else {
            // Ficha de dominó
            return (ultimaFicha.getLado1() == ficha.getLado1() ||
                    ultimaFicha.getLado1() == ficha.getLado2() ||
                    ultimaFicha.getLado2() == ficha.getLado1() ||
                    ultimaFicha.getLado2() == ficha.getLado2());
        }
    }

    private int sumaPuntos(Ficha ficha) {
        if (ficha instanceof FichaTridomino) {
            return ficha.getLado1() + ficha.getLado2() + ((FichaTridomino) ficha).getLado3();
        }
        return ficha.getLado1() + ficha.getLado2();
    }

    private void determinarGanador() {
        System.out.println("Juego terminado.");
        System.out.println("Puntos Jugador 1: " + puntosJugador1);
        System.out.println("Puntos Jugador 2: " + puntosJugador2);

        if (puntosJugador1 > puntosJugador2) {
            System.out.println("¡Jugador 1 gana!");
        } else if (puntosJugador2 > puntosJugador1) {
            System.out.println("¡Jugador 2 gana!");
        } else {
            System.out.println("¡Es un empate!");
        }
    }

    private boolean determinarPrimerJugador() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Jugador 1, selecciona una ficha (1 a " + jugador1.size() + ") para competir por el inicio:");
        int eleccion1 = scanner.nextInt();
        Ficha fichaJugador1 = jugador1.get(eleccion1 - 1);

        System.out.println("Jugador 2, selecciona una ficha (1 a " + jugador2.size() + ") para competir por el inicio:");
        int eleccion2 = scanner.nextInt();
        Ficha fichaJugador2 = jugador2.get(eleccion2 - 1);

        // Mostrar las fichas seleccionadas
        System.out.println("Jugador 1 seleccionó: " + fichaJugador1);
        System.out.println("Jugador 2 seleccionó: " + fichaJugador2);

        // Sumar los puntos de cada ficha
        int puntosJugador1 = sumaPuntos(fichaJugador1);
        int puntosJugador2 = sumaPuntos(fichaJugador2);

        System.out.println("Puntos de Jugador 1: " + puntosJugador1);
        System.out.println("Puntos de Jugador 2: " + puntosJugador2);

        // Determinar quién comienza
        if (puntosJugador1 > puntosJugador2) {
            mesa.add(fichaJugador1); // Ficha del jugador 1 va a la mesa
            jugador1.remove(fichaJugador1); // Remover la ficha del jugador 1
            System.out.println("Jugador 1 comienza el juego.");
            return true; // El jugador 1 comienza
        } else {
            mesa.add(fichaJugador2); // Ficha del jugador 2 va a la mesa
            jugador2.remove(fichaJugador2); // Remover la ficha del jugador 2
            System.out.println("Jugador 2 comienza el juego.");
            return false; // El jugador 2 comienza
        }
    }

    public static void main(String[] args) {
        CurlysMultiPieceMino juego = new CurlysMultiPieceMino();
        juego.jugar();
    }
}
