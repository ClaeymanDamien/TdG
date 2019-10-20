package read_and_process_graph;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class L3_A7_Graph {
    private int a_map[][];
    private int v_map[][];
    private int nb_summit;
    private int graphNumber;
    private static final int NO_NEXT = -2;
    private static final int PROCESS_FINISH = -1;

    public L3_A7_Graph(String input_file_path) {
        file_to_graph(input_file_path);
    }
    
    public L3_A7_Graph(String input_file_path, int graphNumber) {
        file_to_graph(input_file_path);
        this.graphNumber = graphNumber;
    }

    /**
     * Fonction permettant de mettre en mémoire un graph passer en parametres
     * @param input_file_path Chemin amenant au fichier
     */
    private void file_to_graph(String input_file_path) {
        try {
            String[] splited;
            // Stockage de toutes les lignes du fichier dans une liste de String
            List<String> allLines = Files.readAllLines(Paths.get(input_file_path));
            System.out.println("Voici sa structure au sein du fichier :");
            // Affichage du graph dans sa forme texte
            for (String s : allLines)
                System.out.println(s);
            // Récupération du nombre de sommets
            System.out.println(allLines.get(0));
            nb_summit = Integer.parseInt(allLines.get(0));
            // Allocation des matrices d'adjacences et de valeurs en fonction du nombre de sommets
            a_map = new int[nb_summit][nb_summit];
            v_map = new int[nb_summit][nb_summit];
            // Pour des soucis de simplicité, on enlève la premiere ligne du fichier qui comporte le nombre de sommets
            allLines.remove(0);
            // Pour chacunes des lignes du fichiers
            for (String s : allLines) {
                splited = s.split(" "); // On sépare les élements selont les espaces et on remplit les matrices
                a_map[Integer.parseInt(splited[0])][Integer.parseInt(splited[2])] = 1;
                v_map[Integer.parseInt(splited[0])][Integer.parseInt(splited[2])] = Integer.parseInt(splited[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print_a()
    {
        print_array(a_map);
    }

    public void print_v()
    {
        print_array(v_map);
    }

    private void print_array(int[][] map) {
        System.out.print("\t\t ");
        for (int k = 0; k < nb_summit; k++)
        {
            System.out.printf("%5d", k);
        }
        System.out.println();
        for (int l = 0; l < nb_summit; l++)
        {
            if (l > 2)
                System.out.print("============");
            else
                System.out.print("\t");
        }
        System.out.println();
        for (int i = 0; i < nb_summit; i++)
        {
            System.out.printf("%5d\t|", i);
            for (int j = 0; j < nb_summit; j++)
            {
                if (map[i][j] != 0)
                    System.out.printf("%5d", map[i][j]);
                else
                    System.out.printf("%5c", '.');
            }
            System.out.println();
        }
    }

    /**
     * Fonction permettant de déterminer si le graph possède des arêtes négatives
     * @return True si le graphe possède des arrètes négative, false sinon
     */
    public boolean      as_negative_edge() {
        for (int i = 0; i < v_map.length; i++)
        {
            for (int j = 0; j < v_map[0].length; j++)
            {
                if (v_map[i][j] < 0)
                    return (true);
            }
        }
        return (false);
    }

    /**
     * Fonction permettant de résoudre l'algorithme de dijkstra
     * PROCESS_FINISH = .
     * -2 = infini
     */
    public void         dijkstra() {
        int             prev_cost;
        int             smalest;
        String          current_prev;
        int             current_value;
        int             d_v_map[][] = new int[nb_summit][nb_summit];
        String          d_p_map[][] = new String[nb_summit][nb_summit];
        List<Integer>   order = new ArrayList<>();
        Scanner         sc = new Scanner(System.in);
        int rootSummit;

        System.out.println("Veuillez entrer le sommet à partir du quel vous voulez partir (entre 0 et "+(nb_summit-1)+").");
        do {
        	rootSummit = sc.nextInt();
        }while(rootSummit < 0 || rootSummit > nb_summit-1);
        order.add(rootSummit);
        
        for (int i = 0; i < nb_summit; i++)         // Initialisation
        {
            if (i == order.get(0))  // Si c'est le même sommet que le sommet de départ
                d_v_map[0][i] = PROCESS_FINISH;
            else {
                // Permet de savoir si il existe un chemin
                if ((d_v_map[0][i] = a_map[order.get(0)][i] == 0 ? NO_NEXT : v_map[order.get(0)][i]) != PROCESS_FINISH)
                    d_p_map[0][i] = "_" + order.get(0).toString();
            }
        }
        for (int k = 1; k < nb_summit; k++) {   // Boucle principale
            if ((smalest = findSmalest(d_v_map[k - 1], order.get(k - 1))) != -1)    // Récupère l'index de la transition la plus petite.
                order.add(smalest); // Ce sera notre prochain sommet a traiter
            else    // Si il n'y en a plus, nous avons finis de faire dijkstra
                break ;
            prev_cost = d_v_map[k - 1][order.get(k)];   // Récupère le coup le plus faible précédent, permet de faciliter la compréhension du code pour la suite.
            for (int j = 0; j < nb_summit; j++){    // Parcours de chaque sommet
                current_prev = "";
                d_p_map[k][j] = "";
                if (order.contains(j))  // Si le sommet a déjà été traité il est définit comme finit
                    current_value = PROCESS_FINISH;
                else if (a_map[order.get(k)][j] == 0) { // Si il n'existe pas de transitions
                    // Si il existe déjà une transition nous la recopions, si ce n'est pas le cas, le sommet est définit comme sans sucesseurs.
                    current_value = d_v_map[k - 1][j] > 0 ? d_v_map[k - 1][j] : NO_NEXT;
                    // Définit le nom du prédecesseur
                    current_prev = d_p_map[k - 1][j].split("_")[1];
                }
                else {  // Il existe une transition, réalisation du calcul
                    if (d_v_map[k - 1][j] == NO_NEXT)   // Si à l'itération précédente il n'y avait pas sucesseurs.
                        current_value = prev_cost + v_map[order.get(k)][j];
                    else if (v_map[order.get(k)][j] + d_v_map[k - 1][order.get(k)] < d_v_map[k - 1][j]) // Si nous avons trouver un chemin plus cours.
                        current_value = v_map[order.get(k)][j] + prev_cost;
                    else if (v_map[order.get(k)][j] + d_v_map[k - 1][order.get(k)] == d_v_map[k - 1][j]){ // Si nous avons trouver un chemin équivalent
                        current_value = d_v_map[k - 1][j];
                        current_prev = "" + order.get(k - 1);
                    }
                    else    // Si aucun chemin n'est améliorant, nous repassons par le même chemin que précédement.
                        current_value = d_v_map[k - 1][j] + prev_cost;
                }
                // Ajouts des variables dans la map
                d_v_map[k][j] = current_value;
                d_p_map[k][j] += "_" + (current_prev.equals("") ? order.get(k).toString() :  current_prev);
            }
        }
        print_dijkstra(d_v_map, d_p_map, order, rootSummit);
        resolve_dijkstra(d_v_map, d_p_map, order, rootSummit);
       }

    private void resolve_dijkstra(int[][] d_v_map, String[][] d_p_map, List<Integer> order, int rootSummit) {
        int current_sommet;
        int last_pos;
        int value_of_way = 0;
        int way;
        List<Integer> chemin = new ArrayList<>();   // Variable permettant de stocker le chemin le plus court trouvé à chaque itérations

        FileWriter fw = null;
		PrintWriter out = null;
		
        try {
        	
        	fw = new FileWriter("L3-A7-trace"+graphNumber+"_"+rootSummit+".txt", true);
			out = new PrintWriter(fw);
			
        	for (current_sommet = 0; current_sommet < nb_summit; current_sommet++) {    // Parcours de tous les sommets
                System.out.println("Sommet " + current_sommet);
                out.println("Sommet " + current_sommet);
                // Si nous arrivons à une position dans le tableau de -1 nous avons finis les calculs
                last_pos = back_process_last_summit(d_v_map, current_sommet, order);
                if ((last_pos == -2))
                {
                    System.out.println("Il n'y a pas de chemin vers le sommet " + current_sommet);
                    out.println("Il n'y a pas de chemin vers le sommet " + current_sommet);
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    out.println();
                    out.println();
                    out.println();
                    continue;
                }
                chemin.add(current_sommet); // Ajout du sommet actuellement traité
                if (last_pos != -1)
                    value_of_way = d_v_map[last_pos][current_sommet];
                way = current_sommet;
                // Tant que nous avons pas finit le calcul
                while (last_pos != -1) {
                    // Ajout du sommet précédent dans le chemin possible
                    chemin.add(Integer.parseInt(d_p_map[last_pos][way].split("_")[1]));
                    // Recherche du sommet qui va précédé
                    way = chemin.get(chemin.size() - 1);
                    //  Recherche de la position optimiser dans la colonne choisie
                    last_pos = back_process_last_summit(d_v_map, way, order);
                }
                // On inverse le chemin pour l'afficher
                Collections.reverse(chemin);
                System.out.println("Le chemin le plus court est : ");
                out.println("Le chemin le plus court est : ");
                System.out.println("-->" + chemin.toString());
                out.println("-->" + chemin.toString());
                System.out.println("Pour un poids de " + value_of_way);
                out.println("Pour un poids de " + value_of_way);
                System.out.println();
                System.out.println();
                System.out.println();
                out.println();
                out.println();
                chemin.clear(); // Netoyage pour la prochaine itération
                value_of_way = 0;
            }
		} 
        catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
    }

    // Fonction permettant de retrouver la dernière ligne dans la quelle se trouve l'information dans le tableau de djikstra
    private int back_process_last_summit(int[][] d_v_map, int current_sommet, List<Integer> order) {
        int i;

        for (i = order.size() - 1; i >= 0; i--) {
            if (d_v_map[i][current_sommet] == NO_NEXT)
                return (NO_NEXT);
            if (d_v_map[i][current_sommet] != PROCESS_FINISH)
                break;
        }
        return (i);
    }

    /**
     * Fonction permettant de trouver le minimum d'un tableau d'int
     * @param arr1 Tableau dans lequel s'éffectue la recherche
     * @param k Taille du tableau
     * @return  Valeur minimum, retourne -1 si rien n'est trouvé.
     */
    private static int findSmalest(int [] arr1, int k){//start method

        int index = 0;
        int min = Integer.MAX_VALUE;
        for (int i=0; i<arr1.length; i++){
            if (arr1[i] < min && arr1[i] >= 0 && k != i){
                min = arr1[i];
                index = i;
            }
        }
        if (min == Integer.MAX_VALUE)
            return (-1);
        return index ;

    }

    /**
     * Fonction permettant d'afficher le tableau de dijkstra
     * @param d_v_map   Matrice stockant les valeurs des chemins
     * @param d_p_map   Matrice sotckant les prédécesseurs
     * @param order     Liste des chemins traités dans l'ordre
     */
    private void print_dijkstra(int d_v_map[][], String d_p_map[][], List<Integer> order, int rootSummit) {
        StringBuilder line_name;
        String cell = "";
        FileWriter fw = null;
		PrintWriter out = null;
		
        try {
        	fw = new FileWriter("L3-A7-trace"+graphNumber+"_"+rootSummit+".txt", false);
			out = new PrintWriter(fw);
			
        	System.out.print("\t ");
        	out.print("\t ");
            for (int k = 0; k < nb_summit; k++)
            {
                System.out.printf("%5d", k);
                out.printf("%5d", k);
            }
            System.out.println();
            out.println();
            for (int l = 0; l < nb_summit; l++)
            {
                if (l > 2) {
                	System.out.print("============");
                	out.print("============");
                }
                else {
                	System.out.print("    ");
                	out.print("    ");
                }
            }
            System.out.println();
            out.println();
            for (int i = 0; i < nb_summit; i++)
            {
                if (i == order.size())
                    break ;
                line_name = new StringBuilder();
                for (int m = 0; m <= i; m++)
                    line_name.append(order.get(m));
                System.out.printf("%5s\t|", line_name.toString());
                out.printf("%5s\t|", line_name.toString());
                for (int j = 0; j < nb_summit; j++) {
                    if (d_v_map[i][j] >= 0)
                        cell = String.valueOf(d_v_map[i][j]) + d_p_map[i][j];
                    else if (d_v_map[i][j] == NO_NEXT)
                        cell = "i";
                    else if (d_v_map[i][j] == PROCESS_FINISH)
                        cell = ".";
                    System.out.printf("%5s", cell);
                    out.printf("%5s", cell);
                }
                System.out.println();
                out.println();
            }
            System.out.println();
            out.println();
        }
        catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
        

    }

    /*
     * Algorithme de Bellman
     */
    public void         bellman() {
        System.out.println("Execution de l'algorithme de bellman.");

        L3_A7_Summit [][] 	bellmanArray = new L3_A7_Summit[nb_summit+1][nb_summit];
        int 			rootSummit;
        int 			currentWeight;
        Integer 			iteration=null;
        Scanner 		sc = new Scanner(System.in);

        System.out.println("Sommet de départ (choisir entre 0 et "+ (nb_summit-1) +"): ");
        
        do {
        	rootSummit = sc.nextInt();
        }while(rootSummit < 0 || rootSummit > nb_summit-1);
        
        

        //Initialisation K=0
        for(int i=0; i<nb_summit; i++ ) {
            bellmanArray[0][i] = (i == rootSummit) ? new L3_A7_Summit(i, 0, i, true) : new L3_A7_Summit(i, false);
        }

        //K=1 à K=n
        for(int k = 1; k<nb_summit+1; k++)
        {
            //Check des successeurs
            for(int j = 0; j<nb_summit; j++) {
                //Si c'est un successeur
                if(bellmanArray[k - 1][j].getSuccessor()) {
                    //on regarde la matrice d'adjacence pour voir vers qu'elle sommet il pointe
                    for(int l = 0; l<nb_summit; l++) {
                        //S'il y a un chemin vers le point
                        if(a_map[j][l] == 1) {

                            //Poid du chemin en partant du nouveau sommet d'origine
                            currentWeight = bellmanArray[k-1][j].getWeight() + v_map[j][l];
                            
                            //Si ce n'est pas infinie
                            if (bellmanArray[k-1][l].getWeight() != null || bellmanArray[k][l] != null) {
                                //Copie du sommet n-1 pour n
                                if(bellmanArray[k][l] == null) {
                                    bellmanArray[k][l] = new L3_A7_Summit(
                                            l,
                                            bellmanArray[k-1][l].getWeight(),
                                            bellmanArray[k-1][l].getOriginSummit(),
                                            true
                                    );
                                }

                                //Si c'est améliorant on modifie le chemin en métant un nouveau sommet d'origine
                                if(currentWeight < bellmanArray[k][l].getWeight()) {
                                    bellmanArray[k][l].setWeight(currentWeight);
                                    bellmanArray[k][l].getOriginSummit().clear();
                                    bellmanArray[k][l].getOriginSummit().add(j);
                                }
                                //Si le chemin à la même poid on l'ajoute au sommet d'origine
                                else if(currentWeight == bellmanArray[k][l].getWeight()) {
                                    //Ne contient pas déjà la valeur
                                    if(!bellmanArray[k][l].getOriginSummit().contains(j))
                                        bellmanArray[k][l].getOriginSummit().add(j);
                                }
                            }
                            else {
                                //Si c'est infinie on met le premier chemin qu'on trouve
                                bellmanArray[k][l] = new L3_A7_Summit(l,currentWeight, j, true);
                            }
                        }
                    }
                }
            }
            //On remet des infini si on a pas encore trouvé de chemin
            for (int m = 0; m<nb_summit; m++) {
                if(bellmanArray[k][m] == null)
                    bellmanArray[k][m] = (L3_A7_Summit)bellmanArray[k-1][m].clone();
            }
            /*
             * Check si le chemin les chemins les plus cours sont trouvés
             */
            iteration = k;
            if(checkSameArraySummit(bellmanArray[k], bellmanArray[k-1]))
                break;
        }

        /*
         * Affichage Bellman et test si circuit absorbant
         */
        printBellman(bellmanArray, iteration, rootSummit);
        if(checkSameArraySummit(bellmanArray[iteration], bellmanArray[iteration-1])) {
            System.out.println("Il n'y a pas de circuit absorbant");
            resolveBellman(bellmanArray, iteration, rootSummit);
        }
        else {
            System.out.println("Il y a un circuit absorbant");
        }
    }

    /*
     * Vérifie si deux tableaux de Summit sont identique
     */
    private boolean checkSameArraySummit(L3_A7_Summit[] array1, L3_A7_Summit[] array2) {
        for(int i = 0; i<nb_summit; i++) {
            if (!array1[i].equals(array2[i]))
                return false;
        }
        return true;
    }

    /*
     * Affichage des chemins les plus courts
     */
    
    private void resolveBellman(L3_A7_Summit[][] bellman, int iteration, int rootSummit) {
    	List<Integer> pathSummit = new ArrayList<>();
    	int rootFind;
    	int nextSummit;
    	FileWriter fw = null;
		PrintWriter out = null;
    	
		try {
			fw = new FileWriter("L3-A7-trace"+graphNumber+"_"+rootSummit+".txt", true);
			out = new PrintWriter(fw);
			
			System.out.println("\n\nLes chemins les plus courts pour chaque sommet\n");
			out.println("\n");
			//out.println();
	    	for(int i = 0; i < nb_summit; i++) {
	    		rootFind = 0;
	    		nextSummit = i;
	    		pathSummit.clear();
	    		
	    		System.out.println("Sommet "+i+ ":");
	    		out.println("Sommet "+i+ ":");
	    		
	    		//Si le sommet est innaccessible
	    		if(bellman[iteration][i].getWeight() == null) {
					rootFind = -2;
					System.out.println("Ce sommet est inaccessible.");
					out.println("Ce sommet est inaccessible.");
				}
	    		
	    		//Ajout du sommet à atteindre dans le chemin
	    		pathSummit.add(bellman[iteration][i].getSummit());
	    		
	    		//Recherche du chemin à partir de bellman
	    		while (rootFind == 0) {
	    			if(bellman[iteration][nextSummit].getSummit() == rootSummit)
						rootFind = -1;
	    			else
	    				pathSummit.add(nextSummit = bellman[iteration][nextSummit].getOriginSummit().get(0));	
				}
	    		
	    		//Affichage du chemin s'il existe
	    		if(rootFind == -1) {
	    			Collections.reverse(pathSummit);
	    			System.out.println("Le chemin le plus court est :");
	    			out.println("Le chemin le plus court est :");
	        		System.out.print("-->");
	        		out.print("-->");
	    			System.out.println(pathSummit);
	    			out.println(pathSummit);
	    			System.out.println("Avec un poid de : "+bellman[iteration][i].getWeight()+"\n");
	    			out.println("Avec un poid de : "+bellman[iteration][i].getWeight());
	    			out.println();
	    		}
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			out.close();
		}
    	
    }
    /*
     * Affichage de Bellman
     */
    private void printBellman(L3_A7_Summit[][] bellman, int iteration, int rootSummit) {
    	
    	FileWriter fw = null;
		PrintWriter out = null;
    	
    	try {
    		
    		fw = new FileWriter("L3-A7-trace"+graphNumber+"_"+rootSummit+".txt", false);
			out = new PrintWriter(fw);
    		
    		//Affichage des sommets
            System.out.print("\t ");
            out.print("\t");
            for(int i = 0; i < nb_summit; i++) {
                System.out.print(i+"\t");
                out.print(i+"\t");
            }
            System.out.print("\n\t ");
            out.println("");
            out.print("\t ");
            for (int j = 0; j < nb_summit; j++) {
                System.out.print("=======");
                out.print("=======");
            }
            System.out.println();
            out.println();
            //Affichage des K étapes
            for(int k=0;k<iteration+1;k++) {
                if(k==0) {
                	System.out.print("Init    |");
                	out.print("Init    |");
                }
                else if(k==nb_summit) {
                	System.out.print("Check   |");
                	out.print("Check   |");
                }
                else {
                	System.out.print(k+"       |");
                	out.print(k+"       |");
                }

                for(int l=0; l<nb_summit; l++) {
                    if(bellman[k][l].getWeight() == null) {
                    	System.out.print("i\t");
                    	out.print("i\t");
                    }
                    else {
                    	System.out.print(bellman[k][l].getWeight()+bellman[k][l].printOriginSummit()+"\t");
                    	out.print(bellman[k][l].getWeight()+bellman[k][l].printOriginSummit()+"\t");
                    }
                        
                }
                System.out.println();
                out.println();
            }
    	}
    	catch (IOException e) {
			e.printStackTrace();
		}
    	finally {
    		out.close();
    	}
    	
        
    }
}
