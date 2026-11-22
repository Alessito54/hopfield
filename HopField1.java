import java.util.ArrayList;
import java.util.Arrays;

public class HopField1 {

    public static void main(String[] args) {
        // Declaración de las matrices a memorizar
        int[][] m1 = {
                { 1, 1 },
                { 1, 0 }
        };

        int[][] m2 = {
                { 0, 0 },
                { 0, 1 }
        };


        

        int[] patron = { -1, -1, -1, -1 };

        ArrayList<Object> list = new ArrayList<>(); // Lista para guardar matrices y vectores

        System.out.println("\nMatriz 1:");
        imprimirMatriz(m1);

        System.out.println("\nMatriz 2:");
        imprimirMatriz(m2);

        // 1. Cambiamos las matrices a matrices bipolares
        m1 = convertirBipolar(m1);
        m2 = convertirBipolar(m2);

        System.out.println("\nMatriz 1 (Bipolar):");
        imprimirMatriz(m1);

        System.out.println("\nMatriz 2 (Bipolar):");
        imprimirMatriz(m2);

        // 2. Calculamos W
        System.out.println("\nCalcular pesos W");

        int[] v1 = aplanamiento(m1);
        int[] v2 = aplanamiento(m2);

        list.add(transpuestaVector(v1));
        list.add(v1);
        list.add(transpuestaVector(v2));
        list.add(v2);

        int[][] w = calcularPesos(list);
        imprimirMatriz(w);

        System.out.println("\nPatrón de entrada:");
        imprimirVector(patron);

        boolean bandera = true;
        int iteraciones = 0;

        while (bandera) {

            iteraciones++;

           
            int[] anteriorIteracion = patron.clone();

            // Multiplicamos el estado actual por W
            int[] patronR = multiplicarMatrizVector(patron, w);

            // Aplicamos la función de activación
            patron = funcionActivacion(patronR);

            System.out.println("\nIteración: " + iteraciones);
            imprimirVector(patron);

            if (Arrays.equals(patron, anteriorIteracion)) {
                bandera = false;

                System.out.println("\nSe encontró un estado estable en la iteracion " + iteraciones);

            }
        }

    }

    public static void imprimirMatriz(int matriz[][]) {

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static int[] funcionActivacion(int[] vector) {
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] <= 0)
                vector[i] = -1;

            if (vector[i] > 0)
                vector[i] = 1;
        }
        return vector;
    }

    // si es 0 se convierte en -1 si es uno se mantiene en 1
    public static int[][] convertirBipolar(int matriz[][]) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] <= 0) {
                    matriz[i][j] = -1;
                }
                if (matriz[i][j] > 0) {
                    matriz[i][j] = 1;
                }

            }
        }
        return matriz;
    }

    public static int[][] calcularPesos(ArrayList<Object> list) {
        ArrayList<int[][]> listaMatrices = new ArrayList<>(); // lista para fuardar multiplicaciones de matrices

        for (int i = 0; i < list.size(); i += 2) {

            listaMatrices.add(multiplicacion(
                    (int[]) list.get(i + 1),
                    (int[][]) list.get(i)));

        }

        return diagonalCero(sumarMatrices(listaMatrices));

    }

    // multiplacion de vector con vector transpuesto (columna x fila)
    public static int[][] multiplicacion(int[] vector, int[][] matriz) {

        int[][] resultado = new int[vector.length][matriz[0].length];
        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                resultado[i][j] = vector[i] * matriz[0][j];
            }
        }

        return resultado;
    }

    // multiplicacion de vector con matriz
    public static int[] multiplicarMatrizVector(int[] vector, int[][] matriz) {

        int[] resultado = new int[matriz.length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                resultado[i] += matriz[i][j] * vector[j];
            }
        }

        return resultado;
    }

    public static int[][] sumarMatrices(ArrayList<int[][]> listaMatrices) {

        int filas = listaMatrices.get(0).length;
        int columnas = listaMatrices.get(0)[0].length;

        int[][] resultado = new int[filas][columnas];

        for (int k = 0; k < listaMatrices.size(); k++) {
            int[][] matriz = listaMatrices.get(k);

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    resultado[i][j] += matriz[i][j];
                }
            }
        }

        return resultado;
    }

    // convierto el arreglo en columnas de una matriz
    public static int[][] transpuestaVector(int vector[]) {
        int[][] matriz = new int[1][vector.length];
        for (int i = 0; i < vector.length; i++) {
            matriz[0][i] = vector[i];
        }
        return matriz;
    }

    public static int[][] diagonalCero(int[][] matriz) {

        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i] = 0;
        }

        return matriz;
    }

    public static void imprimirVector(int vector[]) {
        for (int i = 0; i < vector.length; i++) {
            System.out.print(vector[i] + "\t");
        }
    }

    // Convierte la matriz a un vector
    public static int[] aplanamiento(int matriz[][]) {
        int total = 0;
        for (int i = 0; i < matriz.length; i++) { // sacar espacios de la matriz
            total += matriz[i].length;
        }
        int[] vector = new int[total];
        int k = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                vector[k] = matriz[i][j];
                k++;
            }

        }
        return vector;

    }
}
