import java.awt.RenderingHints.Key;
import java.io.*;
import java.util.*;

public class dfs {

	static Integer numberOfNodes;
	static ArrayList<ArrayList<Integer>> adjacencyList;
	static ArrayList<ArrayList<Integer>> transposedAdjacencyList;
	static ArrayList<ArrayList<Character>> sccList;
	static ArrayList<Character> topologicalItems;
	static ArrayList<Character> dfsItems;
	static ArrayList<Character> sccItems;

	static ArrayList<Character> vertices;
	static ArrayList<Character> color;
	static ArrayList<Character> predecessor;
	static ArrayList<Integer> discoveryTime;
	static ArrayList<Integer> finishingTime;
	static int top = -1;
	static int position = 0;
	static int time = 0;

	public static void main(String[] args) throws  IOException {

		adjacencyList = new ArrayList<ArrayList<Integer>>();
		transposedAdjacencyList = new ArrayList<ArrayList<Integer>>();
		sccList = new ArrayList<ArrayList<Character>>();
		topologicalItems = new ArrayList<Character>();
		dfsItems = new ArrayList<Character>();
		vertices = new ArrayList<Character>();
		color = new ArrayList<Character>();
		predecessor = new ArrayList<Character>();
		discoveryTime = new ArrayList<Integer>();
		finishingTime = new ArrayList<Integer>();

		
    	//read the file and print adj matrix
		String filename = (args.length>0)?args[0]:"input.txt";
		ArrayList<ArrayList<Integer>> adjListFromInputFile = getAdjacencyListFromInputFile(filename);
		if(adjListFromInputFile.size()<1)return;
		numberOfNodes = adjListFromInputFile.get(0).get(0); 
		System.out.print("\nNumberOfNodes: " + numberOfNodes + "\n");
		adjacencyList.addAll(adjListFromInputFile.subList(1, adjListFromInputFile.size()));
		System.out.print("\nInput adjacency Matrix:\n");
    	for (int i=0; i<adjacencyList.size();i++) 
    		System.out.println(adjacencyList.get(i));
    	
    	
    	Character vertex = 'a'; 		
    	for (int i=0; i<numberOfNodes;i++) {
    		vertices.add(vertex); vertex++;
    		discoveryTime.add(0);
    		finishingTime.add(0);  
    		color.add('w');
    		predecessor.add('z');
    	}    	
    	
    	for (int node=0; node<numberOfNodes;node++) 
    		if(color.get(node) == 'w')
    			dfs_visit(node);

    	System.out.println("\n\n----------DFS Traversal------------\n");	
		System.out.println("Vertex"+" | Discovery | "+"Finishing");
		for(int i=0; i<dfsItems.size(); i++)
			for(int j=0; j<vertices.size(); j++)			
				if(dfsItems.get(i)==vertices.get(j))
					System.out.println("  "+dfsItems.get(i)+"\t     "+ discoveryTime.get(j)+"  \t"+finishingTime.get(j));

		topologicalSort();

		transposedAdjacencyList = transpose(adjacencyList);
    	
		 System.out.println("\n\n----------Transposed Matrix----------");
		 for (int i=0; i<numberOfNodes; i++)
			 System.out.println(transposedAdjacencyList.get(i));
		 
		 vertex = 'a'; 		
		 for (int i=0; i<numberOfNodes;i++) 
		 { 
	   		color.add('w');
	   		predecessor.add('z');
		 }  	
	    	
		sccItems = new ArrayList<Character>();
	    for (int node=0; node<topologicalItems.size();node++) 
	  		if(color.get(node) == 'w')
    			scc(node);
	    
	    for(int j=0; j<sccItems.size(); j++)
	    	System.out.println(sccItems.get(j));

	    
	}
	
	public static void dfs_visit(int node) 
	{
    	dfsItems.add(vertices.get(node));
        color.set(node, 'g');
        time=time+1;
        discoveryTime.set(node, time);
        for (int j = node; j < adjacencyList.get(node).size(); j++)
        {
            if (adjacencyList.get(node).get(j) == 1 && color.get(j) == 'w') //if there is a edge and the node is not visited
            {
            	predecessor.set(j, vertices.get(node));
    			dfs_visit(j);
            }
        }
        
        time=time+1;
        finishingTime.set(node, time);
        color.set(node,'b');		
        discoveryTime.set(node, discoveryTime.get(node));
        finishingTime.set(node, finishingTime.get(node));
    }
	
	public static void topologicalSort()
	{
		int i, j, temp;
	    boolean flag = true;   
	    
		for (i=0; i<numberOfNodes;i++) 
    		topologicalItems.add(vertices.get(i));
    	
		ArrayList<Integer> tempFinishingTimes = new ArrayList<Integer>();
		for (i=0; i<numberOfNodes;i++) 
			tempFinishingTimes.add(finishingTime.get(i));
		
    	ArrayList<Integer> tempDiscoveryTime = new ArrayList<Integer>();
		for (i=0; i<numberOfNodes;i++) 
			tempDiscoveryTime.add(discoveryTime.get(i));
		
		while ( flag ) 
		{
			flag= false;  
			for (j = 0; j<tempFinishingTimes.size()-1; j++)  
			{
				if(tempFinishingTimes.get(j)<tempFinishingTimes.get(j+1))
				{
					temp = tempFinishingTimes.get(j);
           			tempFinishingTimes.set(j, tempFinishingTimes.get(j+1));
           			tempFinishingTimes.set(j+1, temp);
           			
					temp = tempDiscoveryTime.get(j);
					tempDiscoveryTime.set(j, tempDiscoveryTime.get(j+1));
					tempDiscoveryTime.set(j+1, temp);
           			
           			char tempVetrex = topologicalItems.get(j);
	   				topologicalItems.set(j, topologicalItems.get(j+1));
     				topologicalItems.set(j+1, tempVetrex);
	                flag = true;            
				}
			} 
		}
		
		System.out.println("\n\n----------Topological Sort------------\n");
		for(i=0; i<numberOfNodes; i++)
			System.out.println("  " + topologicalItems.get(i)+" ---> " + tempDiscoveryTime.get(i)+"/"+tempFinishingTimes.get(i));
	}
		
	public static ArrayList<ArrayList<Integer>> transpose(ArrayList<ArrayList<Integer>> adjacencyList)
	 {
		 ArrayList<ArrayList<Integer>> tempArray = new ArrayList<ArrayList<Integer>>();
		 for (int i=0; i<numberOfNodes; i++)
		 {
			 ArrayList<Integer> subArray = new ArrayList<Integer>();
			 for (int j=0; j<numberOfNodes; j++)
				 subArray.add(0);
			 tempArray.add(i,subArray);
		 }
		 
		 for (int i=0; i<numberOfNodes; i++)
		 {
			 ArrayList<Integer> subArray = new ArrayList<Integer>();
			 for (int j=0; j<numberOfNodes; j++)
				 subArray.add(adjacencyList.get(j).get(i));
			 tempArray.set(i,subArray);
		 }
		 return tempArray;
	 }
	 
	public static void scc(int node) 
	{
//	    sccList.add // (vertices.get(node));
//		ArrayList<Character> tempArray = new ArrayList<Character>();
//		tempArray.add(topologicalItems.get(node));
		
		sccItems.add(topologicalItems.get(node));
		
	    color.set(node, 'g');
	    for (int j = node; j < transposedAdjacencyList.get(node).size(); j++)
	    {
	    	if (transposedAdjacencyList.get(node).get(j) == 1 && color.get(j) == 'w') //if there is a edge and the node is not visited
	    	{
	    		predecessor.set(j, topologicalItems.get(node));
	    		scc(j);
	    	}
	    	if (transposedAdjacencyList.get(node).get(j) == 1 && predecessor.get(node) == 'w') //if there is a edge and the node is not visited
	    	{
	    		predecessor.set(j, topologicalItems.get(node));
	    		scc(j);
	    	}
	    }	        
	    color.set(node,'b');		
	}
	
	
	private static ArrayList<ArrayList<Integer>> getAdjacencyListFromInputFile(String fileName) throws NumberFormatException, IOException 
	{
		FileInputStream fileInputStream = new FileInputStream(fileName);
		DataInputStream dataInputStream = new DataInputStream(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
		String strLine;
		int i=0;
		ArrayList<ArrayList<Integer>> arrayOfInputFileLines = new ArrayList<ArrayList<Integer>>();
		
		while ((strLine = bufferedReader.readLine()) != null && strLine.length()!=0)
		{              
            	StringTokenizer st = new StringTokenizer(strLine, ",");  
           		ArrayList<Integer> intArr = new ArrayList<Integer>();
            	while (st.hasMoreElements()) 
            	{
                		String element = ((String)st.nextElement());
                		intArr.add(Integer.parseInt(element));
          		}
         		arrayOfInputFileLines.add(i, intArr); 
            	i++;
		}		
		dataInputStream.close();
		return arrayOfInputFileLines;
	}
}
