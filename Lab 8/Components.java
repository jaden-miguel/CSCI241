// Author: Jaden Miguel
// Date: Spring 2020
// Purpose: Lab 8 CSCI241

package lab8;

import java.util.Scanner; 
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;


public class Components {

    public static int[][] adjMatrix;
    
    public static void main(String[] args) throws FileNotFoundException {

        //read file
        File file = new File(args[0]);
        Scanner fileInput = new Scanner(file);

        //create token input, find size
        StringTokenizer T = new StringTokenizer(fileInput.nextLine());
        int size = Integer.parseInt(T.nextToken());

        adjMatrix = new int[size][size];

        //iterate over text file, fill adjacency matrix
        while (fileInput.hasNext()) {
            int x = fileInput.nextInt();
            int y = fileInput.nextInt();

            adjMatrix[x][y] = 1;
            adjMatrix[y][x] = 1;
        }
    

        //create count and visited check
        int count = 0;
        boolean visited [] = new boolean[size];

        for (int i = 0; i < adjMatrix.length; i++) {
            if (!visited[i]) {
                dfs(i, adjMatrix, visited);
                count++;
            }
        }
        //final print connected components
        System.out.println(count);

    
        fileInput.close();

    }


    // Depth first search recursive method
    // Reads adjacency matrix and marks visited
    // Runtime should be O(v^2), has to visit all nodes
    public static void dfs(int i, int[][] graph, boolean[] visited) {
        if (!visited[i]) {        
            visited[i] = true; // mark node as visited
    
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == 1 && !visited[j]) {   
                    dfs(j, graph, visited); // visit node
                }
            }
        }   
    }








}