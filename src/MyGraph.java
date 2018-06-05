import java.util.*;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Color; 

/**
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 * Add your name here: Nick Amundsen, Devon McDonald
 */

public class MyGraph extends JPanel implements ActionListener{
	
	private static ArrayList<Vertex> vertices; //The arraylist of the vertex objects
	private static ArrayList<Edge> edges; //The arraylist of the edge objects
	private static JTextField inputField;
	private static String input = "";
	private static ArrayList<String> route = new ArrayList<String>();
	private static HashMap<String, String> previousNodes = new HashMap<String,String>();
	
	//Constructor method for MyGraph. Utilizes the methods from the JPanel which this class extends. (the superclass)
	public MyGraph() {
		super();
	}
	
	//Sets the vertex object arraylist
	public void setVertices(ArrayList<Vertex> vertices) {
		MyGraph.vertices = vertices;
	}
	
	//Sets the edge object arraylist
	public  void setEdges(ArrayList<Edge> edges) {
		MyGraph.edges = edges;
	}
	
	//Override of the JPanel draw class for our needs, called automatically.
	//Must call repaint to paint again.
	@Override
	public void paint(Graphics g) { 
		int width = getWidth(); //Gets the width          
        int height = getHeight(); //Gets the height
		super.paint(g); //Calls for the superclass to paint, so our panel is drawn on correctly
		displayVertices(vertices, width, height, g); //Calls for the method to draw the vertices
        displayEdges(edges, vertices, width, height, g); //Calls for the method to draw the edges
        g.setColor(Color.BLACK); //Sets color back to black after edges method
        g.drawString("Black to green line is direction of flight path, complete green line means flight travels both ways", 10, 80);
        g.drawString(input, 10, 120);
	}
	
	//Loads the vertices from the file into objects in an arraylist.
	public static ArrayList<Vertex> loadVertices(String vertexfile) {
       	ArrayList <Vertex> vertices = new ArrayList<Vertex>(); //The arraylist to be returned        
      	try { //Try to open the text file, throw exception if unable to do so
    		FileReader fr = new FileReader(vertexfile); //Opens a file reader to convert the text file into an array
    		BufferedReader br = new BufferedReader(fr); //This reader is used to read the lines of the file
    		String str; //Will be input for name of vertex
    		while((str = br.readLine())!=null){ //Continue while there are lines to read
    			Vertex v = new Vertex(str); //Vertex object to be added to arraylist
    			vertices.add(v); //Adds vertex to arraylist
    		}
    		fr.close(); //Close reader
    	} catch (FileNotFoundException e) {
    		System.out.println("Error: Unable to open file " + vertexfile); //Prints out the error message if the file cannot be found
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
        return vertices;   
   }
   
   	//Loads the edges form the file into objects in an arraylist.
   	public static ArrayList<Edge> loadEdges(String edgefile) {      	   
        ArrayList <Edge> edges = new ArrayList<Edge>(); //Arraylist to be returned
        try { //Try to open the text file, throw exception if unable to do so
			FileReader fr = new FileReader(edgefile); //Opens a file reader to convert the text file into an array
			BufferedReader br = new BufferedReader(fr); //This reader is used to read the lines of the file
			LinkedList<String> inputs = new LinkedList<String>(); //This list will make up the components of the edge object
			String str; //First component
			while((str =br.readLine())!=null){ //Continue while there are lines to read
				inputs.add(str); //Adds the first component, because of the check readline in the loop
				for (int i = 0; i<4; i++) { //Iterates a couple lines to add the rest of the components to the list
					inputs.add(br.readLine());
				}
				Edge input = new Edge(inputs.pop(),inputs.pop(),inputs.pop(),inputs.pop(),inputs.pop()); //Pops all the components in proper order into a new edge object
				edges.add(input); //Adds that edge object into the list
		    }
			fr.close(); //Close reader
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to open file " + edgefile); //Prints out the error message if the file cannot be found
		} catch (IOException e) {
			e.printStackTrace();
		}         
        return edges;   
   	}
      
    //Called by Paint method, draws the vertices onto the JPanel.
   	//Uses a sin and cos function to map the x and y values based on the angle
   	//at which the vertex is positioned. 360 degrees / Number of vertices gives
   	//the angle difference ten we convert it to radians.
   	public static void displayVertices(ArrayList<Vertex> vertices, int width, int height, Graphics g) { //Draws the vertices onto the JPanel
    	double angledif = 0.0; //This will be the base angle to run our cos and sin functions
  		for (int i = 0; i < vertices.size(); i++) { //Iterates through every vertex in the list
  			vertices.get(i).setXcoord(((.5*width)+Math.cos(Math.toRadians(angledif))*200)); //Sets the x pos for that vertex using a function of cos(angle)
  			vertices.get(i).setYcoord(((.5*height)+Math.sin(Math.toRadians(angledif))*200)); //Sets the y pos for that vertex using a function of sin(angle)
  			g.drawOval((int)vertices.get(i).getXcoord(), (int)vertices.get(i).getYcoord(), 40, 40); //Draws the oval in the correct position calling to the x and y values of the vertex
  			g.drawString(vertices.get(i).getName(), (int)vertices.get(i).getXcoord()+10, (int)vertices.get(i).getYcoord()-5); //Prints out the name of the vertex inside the circle
  			angledif = angledif + 360.0/vertices.size(); //Increases the angle by the correct increments
  			
  		}
  		return;
  	}
         
    //Called by the Paint method, draws the edges going between vertices onto the JPanel 
   	//as well as green lines for direction.
   	public static void displayEdges(ArrayList<Edge> edges, ArrayList<Vertex> vertices, int width, int height, Graphics g) { //Draws the edges onto the JPanel between the vertices  
  		for (int i = 0; i < vertices.size(); i++) { //Iterates through all the vertices to find adj vertices
  			ArrayList<Vertex> adj = findAdjacentVertexObj(vertices.get(i), vertices, edges); //Creates a arraylist of adj vertices
  			for (int k = 0; k < adj.size(); k++) { //Iterates through all the vertices to find the vertex obj for each
  				g.setColor(Color.BLACK); //Sets color for complete line to black
  				g.drawLine((int)vertices.get(i).getXcoord()+20, (int)vertices.get(i).getYcoord()+20, (int)adj.get(k).getXcoord()+20, (int)adj.get(k).getYcoord()+20); //Draws a line between each vertex
  				g.setColor(Color.GREEN); //Sets color for direction line to green 
  				double greenlineX = (((vertices.get(i).getXcoord()+21)+(adj.get(k).getXcoord()+21))/2.0); //Midpoint formula for the green line x coordinate
  				double greenlineY = (((vertices.get(i).getYcoord()+21)+(adj.get(k).getYcoord()+21))/2.0); //Midpoint formula for the green line y coordinate
  				g.drawLine((int)adj.get(k).getXcoord()+21, (int)adj.get(k).getYcoord()+21, (int)greenlineX, (int)greenlineY); //Draws the green line from the end point to the midpoint	
  			}
  		}
       
  	}
   	
   	//Similar to findAdjacentVertices but instead of a string it returns the Vertex objects for such vertices
   	//Used purely for the graphics part of this program.
	private static ArrayList<Vertex> findAdjacentVertexObj(Vertex vertex, ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
		ArrayList<Vertex> adjVertices = new ArrayList<Vertex>(); //Arraylist to be returned
   		for(int i = 0; i < edges.size(); i++) { //Iterates through every edge to find relevant ones
			Edge e = edges.get(i); //The edge to be looked at
			if (e.getStart().equals(vertex.getName())) { //If the edge starts with the input vertex
		   		for (int k = 0; k < vertices.size(); k ++) { //Then check through every vertex name to find vertex object
		   			if (e.getEnd().equals(vertices.get(k).getName())) {
		   				adjVertices.add(vertices.get(k)); //Add vertex object to arraylist
		   			} else {
		   				continue;
		   			}
		   		}
			} else {
				continue;
			}
		}
        return adjVertices; 
	}

	// Creates a JFrame and outs a JPanel on it.
	// Creates a texct field and button on the JPanel,
	// and the creation of the JPanel calls the paint method automatically 
	// thus the graph is drawn as well.
   	public void displayGraph() {
    	MyGraph graphPanel = new MyGraph(); //Call of the constructor class
        JFrame graphApp = new JFrame(); //Creates applet
        graphApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //What to do on exit
        graphApp.add(graphPanel); //Adds panel to applet           
        graphApp.setSize(900, 900); //Defines window size
        graphApp.setVisible(true); //Makes sure its visible
        JLabel label = new JLabel("Please input two vertex names in all CAPS and an option (1 = shortest distance, 2 = fastest time, 3 = cheapest flight, 4 = all options) seperated by spaces. ", JLabel.TRAILING); //Text to go before the calculate button
        graphPanel.add(label); //Adds the label to the panel
        inputField = new JTextField(20); //Sets the inputField text field to a dimension
        label.setLabelFor(inputField); //Adds the label just made before the textField
        graphPanel.add(inputField); //Adds the field to the panel
        JButton button = new JButton("Calculate"); //Creates a label for the button
        graphPanel.add(new JLabel()); //Adds the label to the button
        graphPanel.add(button); //Adds the button to the panel
        button.addActionListener(graphPanel); //Adds an action listener to respond to the button press
        graphApp.setContentPane(graphPanel); //Sets the content pane
        graphPanel.setOpaque(true); //Sets the content pane to opaque, solid
        
   	}

         
   	// Identifies adjacent vertices for a given vertex
   	// Precondition
    //  input vertex, v is one of the vertices in the graph
   	// Postcondition
    //  returns a collection of vertices adjacent to v in the graph             	 
   	public static ArrayList<String> findAdjacentVertices(String vertex, ArrayList<Vertex> vertices, ArrayList<Edge> edges) { 
   		ArrayList<String> adjVertices = new ArrayList<String>(); //Arraylist to be returned
   		for(int i = 0; i < edges.size(); i++) { //Iterates through every edge to find relevant ones
			Edge e = edges.get(i); //The edge to be looked at
			if (e.getStart().equals(vertex)) { //If the edge starts with the input vertex
		   		adjVertices.add(e.getEnd());
			} else {
				continue;
			}
		}
        return adjVertices;   
   	}
         	 
    // Checks whether vertex end_point is adjacent to vertex start_point (i.e. start_point -> end_point) in a directed graph.
    //Precondition
    // 	Vertex a is the start_point
    // 	Vertex b is the end_point 
    //Postcondition
    // 	Returns an array which will contain distance, time_needed, and ticket_price of edge if there is a directed edge from start_point to end_point 
    // 	Returns -1 otherwise.
    // 	(Also, returns -1 if one of the two vertices does not exist.)         
   	public static int[] checkIsAdjacent(String a, String b, ArrayList<Edge> edges) {
   		int [] value = {-1}; //To be returned if the edge doesnt exist
		for(int i = 0; i < edges.size(); i++) { //Iterates through the edges to find the desired one
			Edge e = edges.get(i); //Sets the object to gather info from
			if (e.getStart().equals(a)&&e.getEnd().equals(b)) { //The start and end vertices compared
				value = new int[] {e.getDistance(),e.getTime(),e.getCost()}; //Sets the int[] for values
			} else {
				continue; //Or continues
			}
		}
        return value;   
   	}
   
   	// Identifies the shortest route from start_point to end_point.
   	// Precondition
    //  start_point is the starting vertex
    //  end_point is the destination vertex
   	//  route is a list in which the route will be stored, in order, beginning with the start_point and ending with the end_point
    //  the list will be empty if no such route exists.  
    //Postcondition
   	//  returns the length of the shortest route from start_point to end_point
    //  -1 if no such path exists.
    //  if multiple such route exists, return only 1 route satisfying the criteria   	
   	public static int findShortestRoute(String start_point, String end_point, List<String> route, ArrayList<Edge> edges, ArrayList<Vertex> vertices) {   
    	 HashMap<String, Integer> vertexDist = new HashMap<String, Integer>(); //Stores the distance info for each vertex
    	 HashSet<String> queueSet = new HashSet<String>(); //Stores one of each vertex
    	 ArrayList<String> completed = new ArrayList<String>(); //Stores the vertices that have been completed
     	 queueSet.add(start_point); //Adds the first point to the queue
    	    for (Vertex i : vertices) { //For every vertex in vertices, find adjacent nodes
    	    	for (String node : findAdjacentVertices(i.getName(), vertices, edges)) { //This way any node that has no path towards it does not get used during the calculation
    	    			vertexDist.put(node, Integer.MAX_VALUE); //Adds the node with an integer for max value
    	    			queueSet.add(node); //Adds the node to the queue set
    	    	}
    	    }
    	    vertexDist.put(start_point, 0); //Adds the start node with an initial value of 0
    	    ArrayList<String> queue = new ArrayList<String>(queueSet); //Turns the set into an arrayList for easier use
    	    whileloop: //Label for the while loop
    	    while (!queue.isEmpty()) { //While the loop is not empty
    	    	int minDist = vertexDist.get(queue.get(0)); //Grabs the distance for the first value in the queue, even if it is not the smallest
    	    	String minNode = queue.get(0); //Grabs the first node from the queue
    	    	for (String i : queue) { //Iterates through the queue and compares the first value to each other one trying to find the smallest
    	    		if (vertexDist.get(i) < minDist){
    	    			minDist = vertexDist.get(i);
    	    			minNode = i;
    	    		}else{
    	    			continue;
    	    		}
    	    	}
    	    	if (minDist == Integer.MAX_VALUE) { //Breaks the loop if the smallest value is still infinity
    	    		break whileloop;
    	    	}
    	    	String u = minNode; //Sets the node u as the smallest value
    	    	queue.remove(u); //Removes it from the set
    	    	completed.add(u); //Adds it to the completed set
    	    	for (String node : findAdjacentVertices(u, vertices, edges)) { //for every node in adjacent nodes
    	    		int[] distance = checkIsAdjacent(u, node, edges); //Gets the distance, cost, time of the node
    	    		String v = node; //Sets the second node to v
    	    		int weight = distance[0]; //Gets the first value from the array, distance
    	    		int tempWeight = minDist + weight; //Sets a temporary weight
    	    		if (tempWeight < vertexDist.get(v)) { //The relax method
    	    			vertexDist.replace(v, vertexDist.get(v), tempWeight); 
    	    			previousNodes.put(v, u);
    	    		} else {
    	    			continue;
    	    		}
    	    	}
    	    }

    	   if (vertexDist.get(end_point) == Integer.MAX_VALUE) { //Returns -1 if the end point value is still infinity, meaning there is no path
    		   return -1;
    	   } else { //Or it builds the routes list
    		   String node = end_point; //Sets the first node as the end point
        	   while (!node.equals(start_point)) { //Uses the values it has in the previousNode hashmap to find each previous node
        	    	route.add(0,node);
        	    	node = previousNodes.get(node);
        	   }
        	   route.add(0,start_point); //Adds the start point at the start of the list
    		   return vertexDist.get(end_point); //Returns the value
    	   }
    	}
      
      
    // Identifies the shortest route from start_point to end_point.
   	// Precondition
    //  start_point is the starting vertex
    //  end_point is the destination vertex
   	//  route is a list in which the route will be stored, in order, beginning with the start_point and ending with the end_point
    //  the list will be empty if no such route exists.  
    //Postcondition
   	//  returns the length of the shortest route from start_point to end_point
    //  -1 if no such path exists.
    //  if multiple such route exists, return only 1 route satisfying the criteria   	
   	public static int findCheapestRoute(String start_point, String end_point, List<String> route, ArrayList<Edge> edges, ArrayList<Vertex> vertices) {   
   		HashMap<String, Integer> vertexDist = new HashMap<String, Integer>(); //Stores the distance info for each vertex
   	 HashSet<String> queueSet = new HashSet<String>(); //Stores one of each vertex
   	 ArrayList<String> completed = new ArrayList<String>(); //Stores the vertices that have been completed
    	 queueSet.add(start_point); //Adds the first point to the queue
   	    for (Vertex i : vertices) { //For every vertex in vertices, find adjacent nodes
   	    	for (String node : findAdjacentVertices(i.getName(), vertices, edges)) { //This way any node that has no path towards it does not get used during the calculation
   	    			vertexDist.put(node, Integer.MAX_VALUE); //Adds the node with an integer for max value
   	    			queueSet.add(node); //Adds the node to the queue set
   	    	}
   	    }
   	    vertexDist.put(start_point, 0); //Adds the start node with an initial value of 0
   	    ArrayList<String> queue = new ArrayList<String>(queueSet); //Turns the set into an arrayList for easier use
   	    whileloop: //Label for the while loop
   	    while (!queue.isEmpty()) { //While the loop is not empty
   	    	int minDist = vertexDist.get(queue.get(0)); //Grabs the distance for the first value in the queue, even if it is not the smallest
   	    	String minNode = queue.get(0); //Grabs the first node from the queue
   	    	for (String i : queue) { //Iterates through the queue and compares the first value to each other one trying to find the smallest
   	    		if (vertexDist.get(i) < minDist){
   	    			minDist = vertexDist.get(i);
   	    			minNode = i;
   	    		}else{
   	    			continue;
   	    		}
   	    	}
   	    	if (minDist == Integer.MAX_VALUE) { //Breaks the loop if the smallest value is still infinity
   	    		break whileloop;
   	    	}
   	    	String u = minNode; //Sets the node u as the smallest value
   	    	queue.remove(u); //Removes it from the set
   	    	completed.add(u); //Adds it to the completed set
   	    	for (String node : findAdjacentVertices(u, vertices, edges)) { //for every node in adjacent nodes
   	    		int[] distance = checkIsAdjacent(u, node, edges); //Gets the distance, cost, time of the node
   	    		String v = node; //Sets the second node to v
   	    		int weight = distance[2]; //Gets the third value from the array, cost
   	    		int tempWeight = minDist + weight; //Sets a temporary weight
   	    		if (tempWeight < vertexDist.get(v)) { //The relax method
   	    			vertexDist.replace(v, vertexDist.get(v), tempWeight); 
   	    			previousNodes.put(v, u);
   	    		} else {
   	    			continue;
   	    		}
   	    	}
   	    }

   	   if (vertexDist.get(end_point) == Integer.MAX_VALUE) { //Returns -1 if the end point value is still infinity, meaning there is no path
   		   return -1;
   	   } else { //Or it builds the routes list
   		   String node = end_point; //Sets the first node as the end point
       	   while (!node.equals(start_point)) { //Uses the values it has in the previousNode hashmap to find each previous node
       	    	route.add(0,node);
       	    	node = previousNodes.get(node);
       	   }
       	   route.add(0,start_point); //Adds the start point at the start of the list
   		   return vertexDist.get(end_point); //Returns the value
   	   }
   	}
      
    // Identifies the fastest route from start_point to end_point.
   	// Precondition
    //  start_point is the starting vertex
    //  end_point is the destination vertex
   	//  route is a list in which the route will be stored, in order, beginning with the start_point and ending with the end_point
    //  the list will be empty if no such route exists.  
    //Postcondition
   	//  returns the total_time of the fastest route from start_point to end_point
    //  -1 if no such path exists.
    //  if multiple such route exists, return only 1 route satisfying the criteria   	 
   	public static int findFastestRoute(String start_point, String end_point, List<String> route, ArrayList<Edge> edges, ArrayList<Vertex> vertices) {   
   		HashMap<String, Integer> vertexDist = new HashMap<String, Integer>(); //Stores the distance info for each vertex
   	 HashSet<String> queueSet = new HashSet<String>(); //Stores one of each vertex
   	 ArrayList<String> completed = new ArrayList<String>(); //Stores the vertices that have been completed
    	 queueSet.add(start_point); //Adds the first point to the queue
   	    for (Vertex i : vertices) { //For every vertex in vertices, find adjacent nodes
   	    	for (String node : findAdjacentVertices(i.getName(), vertices, edges)) { //This way any node that has no path towards it does not get used during the calculation
   	    			vertexDist.put(node, Integer.MAX_VALUE); //Adds the node with an integer for max value
   	    			queueSet.add(node); //Adds the node to the queue set
   	    	}
   	    }
   	    vertexDist.put(start_point, 0); //Adds the start node with an initial value of 0
   	    ArrayList<String> queue = new ArrayList<String>(queueSet); //Turns the set into an arrayList for easier use
   	    whileloop: //Label for the while loop
   	    while (!queue.isEmpty()) { //While the loop is not empty
   	    	int minDist = vertexDist.get(queue.get(0)); //Grabs the distance for the first value in the queue, even if it is not the smallest
   	    	String minNode = queue.get(0); //Grabs the first node from the queue
   	    	for (String i : queue) { //Iterates through the queue and compares the first value to each other one trying to find the smallest
   	    		if (vertexDist.get(i) < minDist){
   	    			minDist = vertexDist.get(i);
   	    			minNode = i;
   	    		}else{
   	    			continue;
   	    		}
   	    	}
   	    	if (minDist == Integer.MAX_VALUE) { //Breaks the loop if the smallest value is still infinity
   	    		break whileloop;
   	    	}
   	    	String u = minNode; //Sets the node u as the smallest value
   	    	queue.remove(u); //Removes it from the set
   	    	completed.add(u); //Adds it to the completed set
   	    	for (String node : findAdjacentVertices(u, vertices, edges)) { //for every node in adjacent nodes
   	    		int[] distance = checkIsAdjacent(u, node, edges); //Gets the distance, cost, time of the node
   	    		String v = node; //Sets the second node to v
   	    		int weight = distance[1]; //Gets the second value from the array, time
   	    		int tempWeight = minDist + weight; //Sets a temporary weight
   	    		if (tempWeight < vertexDist.get(v)) { //The relax method
   	    			vertexDist.replace(v, vertexDist.get(v), tempWeight); 
   	    			previousNodes.put(v, u);
   	    		} else {
   	    			continue;
   	    		}
   	    	}
   	    }

   	   if (vertexDist.get(end_point) == Integer.MAX_VALUE) { //Returns -1 if the end point value is still infinity, meaning there is no path
   		   return -1;
   	   } else { //Or it builds the routes list
   		   String node = end_point; //Sets the first node as the end point
       	   while (!node.equals(start_point)) { //Uses the values it has in the previousNode hashmap to find each previous node
       	    	route.add(0,node);
       	    	node = previousNodes.get(node);
       	   }
       	   route.add(0,start_point); //Adds the start point at the start of the list
   		   return vertexDist.get(end_point); //Returns the value
   	   }
   	}
   	
   	//Called when the button is clicked, taking in the String from the textfield
   	//and then using that to decide what the user wants to do.
   	//Eventually calling one of the findRoute methods.
   	
	@Override
	public void actionPerformed(ActionEvent e) {
		input = inputField.getText();
		String start = input.substring(0, 3);
		String end = input.substring(4, 7);
		String choice = input.substring(8, 9);
		if (Integer.parseInt(choice) == 1) {
			int distance = findShortestRoute(start, end, route, edges, vertices);
			if (distance != -1) {
				input = "Distance = " + distance + ", the travel path is " + route;
				route.removeAll(route);
				previousNodes.clear();
				repaint();
			} else {
				input = "Sorry, no path is available";
				route.removeAll(route);
				previousNodes.clear();
				repaint();
			}
		} else if (Integer.parseInt(choice) == 2) {
			int time = findFastestRoute(start, end, route, edges, vertices);
			if (time != -1) {
				input = "Time = " + time + ", the travel path is " + route;
				route.removeAll(route);
				previousNodes.clear();
				repaint();
			} else {
				input = "Sorry, no path is available";
				route.removeAll(route);
				previousNodes.clear();
				repaint();
			}
		} else if (Integer.parseInt(choice) == 3) {
			int cost = findCheapestRoute(start, end, route, edges, vertices);
			if (cost != -1) {
				input = "Cost = " + cost + ", the travel path is " + route;
				route.removeAll(route);
				previousNodes.clear();
				repaint();
			} else {
				input = "Sorry, no path is available";
				route.removeAll(route);
				previousNodes.clear();
				repaint();
			}
		} else if (Integer.parseInt(choice) == 4) {
			int cost = findCheapestRoute(start, end, route, edges, vertices);
			input = "Cost = " + cost + ", the path is " + route;
			route.removeAll(route);
			previousNodes.clear();
			int time = findFastestRoute(start, end, route, edges, vertices);
			input = input + ",  Time = " + time + ", the path is " + route;
			route.removeAll(route);
			previousNodes.clear();
			int distance = findShortestRoute(start, end, route, edges, vertices);
			input = input + ",  Distance = " + distance + ", the path is " + route;
			route.removeAll(route);
			previousNodes.clear();
			repaint();
		} else {
			input = "Sorry, incorrect input";
		}
		
	}
	
	
}