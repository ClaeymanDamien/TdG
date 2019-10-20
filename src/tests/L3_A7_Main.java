package tests;
import read_and_process_graph.*;

import java.util.Scanner;

public class L3_A7_Main {

    public static void main(String[] args) {
        int exit = 0;
        int choice = 0;
        int graph_number;
        String s = new String();
        Scanner sc = new Scanner(System.in);
        System.out.println();
        do
        {
            // Interaction avec l'utilisateur pour choisir le bon graph
            System.out.println("Quel graph voulez vous exploiter ? Indiquez son numéro");
            graph_number = sc.nextInt();
            System.out.println("Vous avez choisi le graph : " + graph_number);
            s = System.getProperty("user.dir") + "/graphs/L3-A11-" + graph_number + ".txt";
            System.out.println("Graph en entrée : " + s);
            // Mise en mémoire du graph
            L3_A7_Graph graph = new L3_A7_Graph(s,graph_number);
            System.out.println("Matrice d'ajacence : ");
            graph.print_a();
            System.out.println("Matrice de valeur : ");
            graph.print_v();
            // Détection d'arretes négatives
            if (graph.as_negative_edge())
                graph.bellman();
            else {
                // Choix de l'algorithme voulu dans le cas d'arretes positives
                System.out.println("Quel algorithme voulez-vous utiliser ? 0 = Bellman - 1 = Dijkstra");
                choice = sc.nextInt();
                if (choice == 0)
                    graph.bellman();
                else
                    graph.dijkstra();
            }
            System.out.println("Voulez vous re-executer le programme ? Oui = 0 - Non = 1");
            exit = sc.nextInt();
        }while (exit == 0);
        
        sc.close();
    }
}
