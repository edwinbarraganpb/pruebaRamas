import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    private final int TOTAL_CARTAS = 10;
    private final int MARGEN = 10;
    private final int DISTANCIA = 40;
    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = MARGEN + TOTAL_CARTAS * DISTANCIA;
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i].mostrar(pnl, posicion, MARGEN);
            posicion -= DISTANCIA;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String resultado = "No se encontaron grupos";
        // iniciar los contadores
        int[] contadores = new int[13];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }
        // obtener los resultados
        boolean hayGrupos = false;
        for (int c : contadores) {
            if (c >= 2) {
                hayGrupos = true;
            }
        }
        if (hayGrupos) {
            resultado = "Se encontraron los siguientes grupos:\n";
            int p = 0;
            for (int c : contadores) {
                if (c >= 2) {
                    resultado += Grupo.values()[c] +
                            " de " + NombreCarta.values()[p] + "\n";
                }
                p++;
            }
        }

        return resultado;
    }

    public String getGrupos() {
        String resultado = "";

        // 1. Identificar cartas en grupos (pares, ternas, etc.)
        int[] contadores = new int[13];  // 13 nombres
        boolean[] usadas = new boolean[TOTAL_CARTAS];  // Marcar cartas usadas en grupos o escaleras

        for (int i = 0; i < TOTAL_CARTAS; i++) {
            contadores[cartas[i].getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int i = 0; i < 13; i++) {
            if (contadores[i] >= 2) {
                hayGrupos = true;
            }
        }

        if (hayGrupos) {
            resultado += "Se encontraron los siguientes grupos:\n";
            for (int i = 0; i < 13; i++) {
                if (contadores[i] >= 2) {
                    resultado += Grupo.values()[contadores[i]] + " de " + NombreCarta.values()[i] + "\n";

                    // Marcar cartas usadas en grupo
                    int marcadas = 0;
                    for (int j = 0; j < TOTAL_CARTAS && marcadas < contadores[i]; j++) {
                        if (!usadas[j] && cartas[j].getNombre().ordinal() == i) {
                            usadas[j] = true;
                            marcadas++;
                        }
                    }
                }
            }
        }

        // 2. Identificar escaleras por pinta
        for (Pinta pinta : Pinta.values()) {
            boolean[] presentes = new boolean[13];
            int[] indicesCartas = new int[13];

            for (int i = 0; i < TOTAL_CARTAS; i++) {
                if (cartas[i].getPinta() == pinta && !usadas[i]) {
                    int idx = cartas[i].getNombre().ordinal();
                    presentes[idx] = true;
                    indicesCartas[idx] = i;
                }
            }

            int consecutivas = 0;
            for (int i = 0; i < 13; i++) {
                if (presentes[i]) {
                    consecutivas++;
                    if (consecutivas >= 3) {
                        // Marcar cartas usadas en escalera
                        for (int j = i - consecutivas + 1; j <= i; j++) {
                            usadas[indicesCartas[j]] = true;
                        }
                        resultado += "Escalera de " + pinta + ": ";
                        for (int j = i - consecutivas + 1; j <= i; j++) {
                            resultado += NombreCarta.values()[j] + " ";
                        }
                        resultado += "\n";
                        break; // Solo una escalera por pinta
                    }
                } else {
                    consecutivas = 0;
                }
            }
        }

        // 3. Calcular puntaje de cartas no usadas
        int puntaje = 0;
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            if (!usadas[i]) {
                NombreCarta nombre = cartas[i].getNombre();
                switch (nombre) {
                    case AS:
                    case JACK:
                    case QUEEN:
                    case KING:
                        puntaje += 10;
                        break;
                    default:
                        puntaje += nombre.ordinal() + 1;
                        break;
                }
            }
        }

        resultado += "Puntaje de cartas no usadas: " + puntaje;

        return resultado;
    }



}
