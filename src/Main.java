import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class Main {
    public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            while (in.hasNextLine()) {
                if(in.nextLine().equals("NODE_COORD_SECTION")){
                    break;
                }
            }
            Grafo grafo = new Grafo();
            String[] vertice;
            do {
                Vertice v = new Vertice();
                vertice = in.nextLine().split(" ");
                if(vertice[0].equals("EOF")) {
                    break;
                }
                int tamanho = vertice.length;
                v.numero = Integer.parseInt(vertice[tamanho-3]);
                v.coordenada_x = Integer.parseInt(vertice[tamanho-2]);
                v.coordenada_y = Integer.parseInt(vertice[tamanho-1]);
                grafo.addVertice(v);
            } while (in.hasNextLine());
            vertice = null;
            int tamanhoGrafo = grafo.vertices.toArray().length;

            MatrizDistancias matriz = new MatrizDistancias();
            matriz.setNewMatrizSizes(tamanhoGrafo);
            for(Vertice vert : grafo.vertices){
                for(int index = vert.numero-1; index < tamanhoGrafo; index++) {
                    Vertice verticeDistance = grafo.getVertice(index);
                    double valorDistancia = Distancia(vert.coordenada_x, vert.coordenada_y, verticeDistance.coordenada_x, verticeDistance.coordenada_y);
                    matriz.setMatrizByIndex(vert.numero-1, index, valorDistancia);
                }
            }
            grafo.setVerticeVisitadoByIndice(0);

            int indexPrincipal = 0;
            for(int repeticoes = 0; repeticoes < grafo.vertices.size(); repeticoes++) {
                Vertice v = grafo.getVertice(indexPrincipal);
                int indexMenor = 0;
                double menor = 100000000.0;
                double[] linhaTeste = matriz.getMatrizLinha(v.numero-1);

                for (int indexFor = 0; indexFor < linhaTeste.length; indexFor++) {;
                    if (v.numero-1 == indexFor || linhaTeste[indexFor] == 0 ) {
                        continue;
                    }
                    if (linhaTeste[indexFor] < menor && grafo.getVertice(indexFor).visitado == 0) {
                        indexMenor = indexFor;
                        menor = linhaTeste[indexFor];
                    }
                }
                indexPrincipal = indexMenor;
                grafo.setVerticeByIndice(v.numero-1, indexMenor, menor);
                grafo.setVerticeVisitadoByIndice(indexMenor);
            }
            grafo.getCustos(matriz);
    }
    private static double Distancia(int cord_x_1, int cord_y_1, int cord_x_2, int cord_y_2) {
        return Math.sqrt(Math.pow(cord_x_2 - cord_x_1, 2) + Math.pow(cord_y_2 - cord_y_1, 2));
    }
}


class Grafo {
    List<Vertice> vertices = new ArrayList<>();
    public void addVertice(Vertice v){
        vertices.add(v);
    }

    public Vertice getVertice(int index) {
        return vertices.get(index);
    }

    public void setVerticeVisitadoByIndice(int index){
        Vertice v = this.vertices.get(index);
        v.visitado = 1;
        this.vertices.set(index, v);
    }

    public void setVerticeByIndice(int index, int next, double distance){
        Vertice v = this.vertices.get(index);
        v.nextIndex = next;
        v.distancia = distance;
        this.vertices.set(index, v);
    }
    public void getCustos(MatrizDistancias matriz){
        double sum = 0;
        for (Vertice vert: this.vertices) {
            System.out.println(vert.numero+" "+( vert.nextIndex +1)+" "+vert.distancia);
            if (vert.distancia < 100000000.0){
                sum += vert.distancia;
            } else if (vert.distancia == 100000000.0) {
                sum += matriz.getMatrizLinha(vert.nextIndex)[vert.numero-1];
            }
        }
        System.out.println(sum);
    }
}

class Vertice {
    int numero;
    int coordenada_x;
    int coordenada_y;
    int visitado = 0;
    int nextIndex;
    double distancia;
}

class MatrizDistancias {
    double[][] matriz;
    public void setNewMatrizSizes(int size) {
        matriz = new double[size][size];
    }

    public void setMatrizByIndex (int index_1, int index_2, double value) {
        matriz[index_1][index_2] = value;
        matriz[index_2][index_1] = value;
    }

    public double[] getMatrizLinha(int linha) {
        return matriz[linha];
    }

}