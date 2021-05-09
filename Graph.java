package ShortestMiddPath;

/*
* Author: Shelby Kimmel 
* Danielle Newberry
* Creates a adjacency list object to store information about the graph of roads, and contains the main functions used to 
* run the Bellman Ford algorithm 
*/

import java.io.File;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.lang.Math;
import java.io.File;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Graph {

	// Object that contains an adjacency list of a road network and a dictionary from elements of the list to indeces 
	// from 0 to |V|-1, since the roads are labeled in the data by arbitrary indices. Because we are considering a walking application,
	// construct the adjacency list so that if there is an edge {u,v}, then u appears in the list of v's neighbors, 
	// and v appears in the list of u's neighbors. This means that the adjacency matrix and the reverse adjacency matrix are the same.
	// In other words, the adjacency matrix that is already written here is a reverse adjacency matrix.
	HashMap<Integer, ArrayList<Road>> adjList;
	HashMap<Integer,Integer> nodeDict;
	Double max = Double.valueOf(Integer.MAX_VALUE);


	public Graph(String file) throws IOException{
		// We will store the information about the road graph in an adjacency list
		// We will use a HashMap to store the Adjacency List, since each vertex in the graph has a more or less random integer name.
		// Each element of the HashMap will be an ArrayList containing all roads (edges) connected to that vertex
		adjList = new HashMap<>();
		nodeDict = null;

		// Based on https://stackoverflow.com/questions/49599194/reading-csv-file-into-an-arrayliststudent-java
		String line = null;
		BufferedReader br = new BufferedReader(new FileReader(file));
		if ((line=br.readLine())==null){
			return;
		}
		while ((line = br.readLine())!=null) {
			String[] temp = line.split(",");
			//Assume all roads are two-way, and using ArcMiles as distance:
			this.addToAdjList(new Road(Integer.parseInt(temp[60]),Integer.parseInt(temp[61]),temp[9],Double.parseDouble(temp[31])));
			this.addToAdjList(new Road(Integer.parseInt(temp[61]),Integer.parseInt(temp[60]),temp[9],Double.parseDouble(temp[31])));
		}


		//For dynamic programming, we will have an array with indeces 0 to |V|-1, 
		// where |V| is the number of vertices. Thus we need to associate each element of adjList with a number between 0 and |V|-1
		// We will use a Dictionary (HashMap) to do this.
		nodeDict = new HashMap<>();
		int j = 0;
		for (Integer nodeName: adjList.keySet()){
			nodeDict.put(nodeName, j);
			j++;
		}
	}


	// get functions
	public HashMap<Integer, ArrayList<Road>> getAdjList(){
		return adjList;
	}
	public HashMap<Integer,Integer> getDict(){
		return nodeDict;
	}


	public synchronized void addToAdjList(Road road) {
		//Adds the Road (edge) to the appropriate list of the adjacency list. 
		//This method is used by the constructor method
		//Based on https://stackoverflow.com/questions/12134687/how-to-add-element-into-arraylist-in-hashmap 
		Integer node = road.getStart();
    	ArrayList<Road> roadList = this.getAdjList().get(node);

    	// if node is not already in adjacency list, we create a list for it
    	if(roadList == null) {
    	    roadList = new ArrayList<Road>();
    	    roadList.add(road);
   		    this.getAdjList().put(node, roadList);
  	  	} 
  	  	else {
        	// add to appropraite list if item is not already in list
        	if(!roadList.contains(road)) roadList.add(road);
    	}
    	
    }

    public Double[][] ShortestDistance(Integer startNode){
    	// This method should create the array storing the objective function values of subproblems used in Bellman Ford.

    	//You need to write this method!!
		int edges = this.getAdjList().size();
		int vertices = this.getDict().size();
		
		Integer start = this.getDict().get(startNode);
		
		
		Double[][] dpArray=new Double[vertices][vertices];
		//initialize distances to infinity
		for (int i = 0; i < vertices; i++) {
			for (int j = 0; j < vertices; j++) {
				dpArray[i][j]= max;
			}
		}
		//distance to get to yourself is 0
		dpArray[0][start] = Double.valueOf(0);


		for (int i = 1; i < vertices-1; i++) {
		
		//System.out.println(" adjList " + this.getAdjList());
		//System.out.println(" node " + this.getDict());
				
			int v = 0;
			for (Integer key: this.getDict().keySet()) {
				int u = 0;
				//find min among options
				for (Road path: adjList.get(key) ){
					if (dpArray[u][i-1]+ path.getMiles() < dpArray[v][i]){
						dpArray[v][i] = dpArray[u][i-1]+ path.getMiles();
						//System.out.println("happening" );
					}
					u++;
				}
				v++;
			}
			
		}
		//LOOK for negative cycles???

		/*for (int i = 0; i < vertices; i++) {
			System.out.println("");
			for (int j = 0; j < vertices; j++) {
				System.out.print(dpArray[i][j] + " ");
			}
		}*/

		return dpArray;
    }

    public void ShortestPath(Integer endNode, Double[][] dpArray){
		// This method should work backwards through the array you created in ShortestDistance and output the 
		// sequence of streets you should take to get from your starting point to your ending point.

		// You need to write this method!!
		//int currentNode = this.getDict().get(endNode);
		//System.out.println(this.getDict().get(endNode));
		/*if (dpArray[0][currentNode] == 0){
			System.out.println("this.getDict().get(endNode)");
			//at start node
			return;
		}*/
		//get next node moving backwards in list

		
		/*ArrayList<Road> roadOptions = this.getAdjList().get(endNode);
		for (int i = currentNode-1; i < 0; i--) {
			for (Road option: roadOptions){
				if (dpArray[i][currentNode] == option.getMiles() + dpArray[i][currentNode -1]){
					//this road was used
					ShortestPath(option.getStart(), dpArray);
					System.out.println(option);
					break;
				}
			}
		}*/
		//INFINITE CYCLE
		
		//System.out.println(this.getAdjList().get(endNode));
		Integer pastEnd = 0;
		for (int i = this.getDict().size()-1; i > 1; i--) {
				//see if previous entry is only path size smaller

				//System.out.println("this should happen more");
				
				/*if(!pastEnd.equals(endNode)){
					pastEnd = endNode;*/
					int currentNode = this.getDict().get(endNode);
					int u = currentNode;
					for (Road path: this.getAdjList().get(endNode) ){
						//check if this path was used to travel
						//make sure end node ends the road and does not start
						/*System.out.println (path.getMiles());
						System.out.println("are they equal?");
						System.out.println(dpArray[currentNode][i]- dpArray[u][i-1]);*/
						if (dpArray[currentNode][i]- dpArray[u][i-1] == path.getMiles()){
							System.out.println(path.toString());
							//System.out.println(this.adjList.get(this.getDict().get(endNode)));
							endNode = path.getStart();
							//currentNode = path.getStart();
							//currentNode = this.getDict().get(endNode);
							//ShortestPath(path.getStart(), dpArray);
							
							break;
						}	
						u--;
					}
				//}
				
		}

		System.out.println("You need to write some code!");
	}			
				

}
