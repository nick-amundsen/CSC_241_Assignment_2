
public class Routes {
	
	public static void main(String[] args) {
		if( args.length != 2 ) {	//Checks for the correct number of arguments, prints something out if incorrect
			System.err.println("Error: Wrong number of arguments");
			System.exit(1);
		}
		MyGraph graph = new MyGraph(); //Creates new graph object to be displayed
    	graph.setVertices(MyGraph.loadVertices(args[0])); //Creates the vertices arraylist
    	graph.setEdges(MyGraph.loadEdges(args[1])); //Creates the edges arraylist
    	graph.displayGraph(); //Runs the display graph method, effectively starting the entire application
		
	}

}
