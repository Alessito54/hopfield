public class Shapes {

    // Tamaño de la cuadrícula: 10 filas x 10 columnas = 100 neuronas
    static final int FILAS = 10;
    static final int COLUMNAS = 10;
    static final int NEURONAS = FILAS * COLUMNAS;

    static final String[] NOMBRES_FIGURAS = {"Cuadrado", "Cruz", "X"};

    // Figuras escritas a mano: # = negro, . = blanco
    static final String[] CUADRADO = {
        "##########",
        "#........#",
        "#........#",
        "#........#",
        "#........#",
        "#........#",
        "#........#",
        "#........#",
        "#........#",
        "##########"
    };

    static final String[] CRUZ = {
        "....##....",
        "....##....",
        "....##....",
        "....##....",
        "##########",
        "##########",
        "....##....",
        "....##....",
        "....##....",
        "....##...."
    };

    static final String[] EQUIS = {
        "#........#",
        ".#......#.",
        "..#....#..",
        "...#..#...",
        "....##....",
        "....##....",
        "...#..#...",
        "..#....#..",
        ".#......#.",
        "#........#"
    };

    public static void main(String[] args) {
        // 1. Convertir las figuras a listas de 100 valores (+1 y -1)
        int[][] figuras = {
            convertir(CUADRADO),
            convertir(CRUZ),
            convertir(EQUIS)
        };

        // 2. Entrenar la red: calcular los pesos a partir de las figuras
        int[][] pesos = entrenarRed(figuras);

        // 3. Probar con cada figura dañada por ruido
        for (int f = 0; f < figuras.length; f++) {
            int[] figuraDanada = danarFigura(figuras[f]);

            System.out.println("Figura con ruido: " + NOMBRES_FIGURAS[f]);
            mostrarFigura(figuraDanada);

            // 4. Dejar que la red corrija la figura
            int[] figuraRecuperada = recuperarFigura(pesos, figuraDanada);

            // 5. Decidir cuál figura original se parece más al resultado
            int ganadora = buscarFiguraMasParecida(figuraRecuperada, figuras);
            int diferencias = contarDiferencias(figuraRecuperada, figuras[ganadora]);

            System.out.println("Resultado: " + NOMBRES_FIGURAS[ganadora]
                    + " (celdas diferentes: " + diferencias + ")");
            mostrarFigura(figuraRecuperada);
        }
    }

    // Convierte las filas de texto en una lista de 100 valores: '#' = +1, '.' = -1
    static int[] convertir(String[] filas) {
        int[] lista = new int[NEURONAS];
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (filas[fila].charAt(col) == '#') {
                    lista[fila * COLUMNAS + col] = 1;
                } else {
                    lista[fila * COLUMNAS + col] = -1;
                }
            }
        }
        return lista;
    }

    // Regla de Hebb: cada par de neuronas se refuerza si sus valores coinciden
    static int[][] entrenarRed(int[][] figuras) {
        int[][] pesos = new int[NEURONAS][NEURONAS];

        for (int f = 0; f < figuras.length; f++) {
            for (int i = 0; i < NEURONAS; i++) {
                for (int j = 0; j < NEURONAS; j++) {
                    // Una neurona no se conecta consigo misma
                    if (i != j) {
                        pesos[i][j] += figuras[f][i] * figuras[f][j];
                    }
                }
            }
        }
        return pesos;
    }

    // Invierte una de cada 7 celdas para simular ruido
    static int[] danarFigura(int[] figura) {
        int[] danada = figura.clone();
        for (int k = 0; k < NEURONAS; k++) {
            if (k % 7 == 0) {
                danada[k] = -danada[k];
            }
        }
        return danada;
    }

    // Repite la actualización hasta que la figura ya no cambie (máximo 20 veces)
    static int[] recuperarFigura(int[][] pesos, int[] entrada) {
        int[] actual = entrada.clone();
        boolean huboCambio = true;
        int vueltas = 0;

        while (huboCambio && vueltas < 20) {
            int[] siguiente = new int[NEURONAS];
            huboCambio = false;

            for (int i = 0; i < NEURONAS; i++) {
                // Suma ponderada de las entradas de la neurona i
                int suma = 0;
                for (int j = 0; j < NEURONAS; j++) {
                    suma = suma + pesos[i][j] * actual[j];
                }

                // Si la suma es 0 o positiva, la neurona queda en +1; si no, en -1
                if (suma >= 0) {
                    siguiente[i] = 1;
                } else {
                    siguiente[i] = -1;
                }

                if (siguiente[i] != actual[i]) {
                    huboCambio = true;
                }
            }

            actual = siguiente;
            vueltas = vueltas + 1;
        }
        return actual;
    }

    // Devuelve el índice de la figura original con menos diferencias
    static int buscarFiguraMasParecida(int[] figura, int[][] figuras) {
        int mejor = 0;
        for (int f = 1; f < figuras.length; f++) {
            if (contarDiferencias(figura, figuras[f]) < contarDiferencias(figura, figuras[mejor])) {
                mejor = f;
            }
        }
        return mejor;
    }

    // Cuenta en cuántas celdas dos figuras son distintas
    static int contarDiferencias(int[] a, int[] b) {
        int diferencias = 0;
        for (int k = 0; k < NEURONAS; k++) {
            if (a[k] != b[k]) {
                diferencias = diferencias + 1;
            }
        }
        return diferencias;
    }

    // Muestra la figura en consola: # = negro, . = blanco
    static void mostrarFigura(int[] figura) {
        for (int fila = 0; fila < FILAS; fila++) {
            for (int col = 0; col < COLUMNAS; col++) {
                if (figura[fila * COLUMNAS + col] == 1) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}