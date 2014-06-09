import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

class Node {
	boolean wasVisited;
	public Node next;
	int distance;

	// constructor
	public Node(boolean w, int x, Node n) {
		this.wasVisited = false;
		this.distance = -1;
		this.next = null;
	}
	// toString, etc.
	// do later
}

class Neighbor {
	public int vertexNum;
	public Neighbor next;

	// constructor
	public Neighbor(int v, Neighbor nbr) {
		this.vertexNum = v;
		next = nbr;
	}
	// toString, etc.
	// do later
}

class Vertex {
	String name;
	Neighbor adjList; // points to first node in linked list
	String school;
	public Vertex next;
	public int dfsnum;
	public boolean visited;
	public int back;

	// constructor
	public Vertex(String name, Neighbor neighbors, String school) {
		this.name = name;
		this.adjList = neighbors;
		this.school = school;
	}
	// toString, etc.
	// do later
}

class Queue {

	private Vertex rear;
	private int size;

	public Queue() {
		rear = null;
		size = 0;
	}

	public void enqueue(Vertex v) {
		Vertex newItem = new Vertex(v.name, v.adjList, v.school);

		if (rear == null) {
			newItem.next = newItem;
		} else {
			newItem.next = rear.next;
			rear.next = newItem;
		}
		size++;
		rear = newItem;
	}

	public Vertex dequeue() throws NoSuchElementException {
		if (rear == null) {
			throw new NoSuchElementException("queue is empty");
		}
		Vertex data = rear.next;
		if (rear == rear.next) {
			rear = null;
		} else {
			rear.next = rear.next.next;
		}
		size--;
		return data;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		size = 0;
		rear = null;
	}

	// returns index number of vertex
	public Vertex peek() throws NoSuchElementException {
		if (rear == null) {
			throw new NoSuchElementException("queue is empty");
		}
		return rear.next;
	}
}

public class Friends {
	
	static BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
	public static void main(String[] args) throws IOException{
		Friends friends = new Friends();
		 System.out.println("Enter file name: ");
		 String file = keyboard.readLine(); 
		 BufferedReader br = new BufferedReader(new FileReader(file));
		 friends.Graph(file);

		 while(true){
			 int choice = getMenuChoice();
			 
			 if(choice == 1){
				 System.out.println("Enter school name: ");
				 String school = keyboard.readLine();
				// Vertex[] finalsub = friends.subGraph(school);
				 Vertex[] graphed = friends.subGraph(school);
				 if (graphed == null){
					 continue;
				 }
				 System.out.println("");
				 friends.printGraph(graphed);
				 
			 }
			 else if(choice == 2){
				 System.out.println("Enter first friends name: ");
				 String firstName = keyboard.readLine();
				 System.out.println("Enter second friends name: ");
				 String secondName = keyboard.readLine();
				 friends.shortestPath(firstName, secondName);
				 //implement the shortest path based on how the method works
			 }
			 else if(choice == 3){
				 System.out.println("Enter name of school: ");
				 String schoo = keyboard.readLine();
				 friends.islands(schoo);
				 
			 }
			 else if(choice == 4){
				friends.connectors();
			 }
			 else  if(choice == 5){
				 return;
			 }
		 }
	   }

	   	public static int getMenuChoice() throws IOException{
		System.out.println();
		System.out.println("Menu:");
		System.out.println("1. Find Subgraph");
		System.out.println("2. Shortest Path");
		System.out.println("3. Cliques");
		System.out.println("4. Connectors");
		System.out.println("5. Quit");
		
		System.out.print("Choice (1-5)? ");
		String choice = keyboard.readLine();
		int choices = Integer.parseInt(choice);
		//int choice = IO.readInt();
		while(choices < 1 || choices > 5){
			System.out.println(choice);
			System.out.print("Please choose valid option."); //somehow it keeps going into this while loop
			String x = keyboard.readLine();
			choices = Integer.parseInt(x);
		}
		return choices;
		
	}
	 


	static Vertex[] adjLists; // LISTS PLURAL: array of vertex objects: the collection
						// of names and adjlinked lists of all the vertices

	private void printGraph(Vertex[] graph){
		
		int n = graph.length;
		System.out.println(n);
		for (int p = 0; p < n; p++) {
			System.out.println(graph[p].name + "|y|" + graph[p].school);
		}

		for (int i = 0; i < graph.length; i++) {

			Neighbor person = graph[i].adjList;
			while (person != null) {
				if (person.vertexNum < indexForName(graph[i].name)) {
					person = person.next;
				} else {
					System.out.println(graph[i].name + "|"
							+ adjLists[person.vertexNum].name);
					person = person.next;
				}
			}
		}
	}
	public void Graph(String file) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(file));

		try {

			int i = Integer.parseInt(br.readLine());
			adjLists = new Vertex[i];

			for (int v = 0; v < i; v++) {
				String line = br.readLine();

				String[] fields = line.split("\\|");

				if (fields.length == 2) {

					adjLists[v] = new Vertex(fields[0], null, null);

				}
				if (fields.length == 3) {

					adjLists[v] = new Vertex(fields[0], null, fields[2]);
				}

			}

			// reads edges
			String line3;
			while ((line3 = br.readLine()) != null) {

				String[] fields3 = line3.split("\\|");

				int v1 = indexForName(fields3[0]);
				int v2 = indexForName(fields3[1]);

				adjLists[v1].adjList = new Neighbor(v2, adjLists[v1].adjList);
				adjLists[v2].adjList = new Neighbor(v1, adjLists[v2].adjList);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*System.out.println("BUILD TEST CHECK: ");
		System.out.println("");
		for (int h = 0; h < adjLists.length; h++) {
			System.out.println(adjLists[h].name.toUpperCase());
			if (adjLists[h].school != null) {
				System.out.println("School: ");
				System.out.println(adjLists[h].school);
			}
			System.out.println("Friends: ");
			Neighbor person = adjLists[h].adjList;
			while (person != null) {
				System.out.println(adjLists[person.vertexNum].name);
				person = person.next;
			}
			System.out.println("");
		}*/
	}

	// method to find index number main adjLists
	public int indexForName(String name) {
		for (int v = 0; v < adjLists.length; v++) {
			if (adjLists[v].name.equalsIgnoreCase(name)) {
				return v;
			} else {
				continue;
			}
		}

		return -1;
	}

	// search/subgraph method based on schools
	// finds how many people go to that school
	public Vertex[] subGraph(String school) {
		school = school.toLowerCase();
		int n = 0;
		for (int i = 0; i < adjLists.length; i++) {
			if (adjLists[i].school != null) {
				if (adjLists[i].school.equalsIgnoreCase(school)) {
					n++;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		if (n==0){
			System.out.println("");
			System.out.println("No one in graph goes to " + school);
			return null;
		}
		Vertex[] subgraph = new Vertex[n];
		// adds the vertices to the subgraph
		int v = 0;
		for (int i = 0; i < adjLists.length; i++) {
			if (adjLists[i].school != null) {
				if (adjLists[i].school.equalsIgnoreCase(school)) {
					subgraph[v] = new Vertex(adjLists[i].name, null, school);
					v++;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		// creates the linked list for each vertex in subgraph
		for (int i = 0; i < subgraph.length; i++) {

			int vert = indexForName(subgraph[i].name);
			Neighbor person = adjLists[vert].adjList;
			Neighbor current = new Neighbor(-1, null);

			while (person != null) {

				if (adjLists[person.vertexNum].school != null) {
					if (adjLists[person.vertexNum].school
							.equalsIgnoreCase(school)) {
						// checks if first slot in linked list
						if (subgraph[i].adjList == null) {
							Neighbor nbr = new Neighbor(person.vertexNum, null);
							current = nbr;
							subgraph[i].adjList = current;
							person = person.next;
						} else {
							current.next = new Neighbor(person.vertexNum, null);
							current = current.next;
							person = person.next;
						}
					} else {
						person = person.next;
					}
				} else {
					person = person.next;
				}

			}
		}/*
		System.out.println("");
		System.out.println("TESTING FOR SUBGRAPH: ");
		System.out.println("");
		System.out.println(n);
		for (int p = 0; p < n; p++) {
			System.out.println(subgraph[p].name + "|y|" + school);
		}

		for (int i = 0; i < subgraph.length; i++) {

			Neighbor person = subgraph[i].adjList;
			while (person != null) {
				if (person.vertexNum < indexForName(subgraph[i].name)) {
					person = person.next;
				} else {
					System.out.println(subgraph[i].name + "|"
							+ adjLists[person.vertexNum].name);
					person = person.next;
				}
			}
		}*/

		return subgraph;
	}

	public void islands(String school) {
		school = school.toLowerCase();
		/*
		int n = 0;
		for (int i = 0; i < adjLists.length; i++) {
			if (adjLists[i].school != null) {
				if (adjLists[i].school.equalsIgnoreCase(school)) {
					n++;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		Vertex[] subgraph = new Vertex[n];
		// adds the vertices to the subgraph
		int v = 0;
		for (int i = 0; i < adjLists.length; i++) {
			if (adjLists[i].school != null) {
				if (adjLists[i].school.equalsIgnoreCase(school)) {
					subgraph[v] = new Vertex(adjLists[i].name, null, school);
					v++;
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		// creates the linked list for each vertex in subgraph
		for (int i = 0; i < subgraph.length; i++) {

			int vert = indexForName(subgraph[i].name);
			Neighbor person = adjLists[vert].adjList;
			Neighbor current = new Neighbor(-1, null);

			while (person != null) {

				if (adjLists[person.vertexNum].school != null) {
					if (adjLists[person.vertexNum].school
							.equalsIgnoreCase(school)) {
						// checks if first slot in linked list
						if (subgraph[i].adjList == null) {
							Neighbor nbr = new Neighbor(person.vertexNum, null);
							current = nbr;
							subgraph[i].adjList = current;
							person = person.next;
						} else {
							current.next = new Neighbor(person.vertexNum, null);
							current = current.next;
							person = person.next;
						}
					} else {
						person = person.next;
					}
				} else {
					person = person.next;
				}

			}
		}*/

		Vertex[] entireSchool = subGraph(school);
		if (entireSchool == null){
			return;
		}
		Vertex[] clique = new Vertex[entireSchool.length];
		ArrayList<Vertex[]> allCliques = new ArrayList<Vertex[]>();
		System.out.println("");

		int v1 = 0;
		boolean again = true;
		while (again == true) {
			for (int i = 0; i < entireSchool.length; i++) {

				if (v1 == 0) {
					if (entireSchool[i] != null) {

						clique[v1] = entireSchool[i];

						v1 = 1;
						Neighbor person = entireSchool[i].adjList;
						while (person != null) {
							clique[v1] = adjLists[person.vertexNum];

							v1++;
							person = person.next;
						}
						continue; // not sure if necessary?
					} else {
						continue;
					}
				} else {

					for (int k = 0; k < clique.length; k++) {
						if (clique[k] == null) {
							continue;
						} else {
							if (clique[k] != null && entireSchool[i] != null) {
								if (clique[k].name.equals(entireSchool[i].name)) {
									Neighbor person = entireSchool[i].adjList;

									boolean there = false;
									while (person != null) {

										for (int d = 0; d < clique.length; d++) {
											if (clique[d] != null) {
												if (adjLists[person.vertexNum].name
														.equalsIgnoreCase(clique[d].name)) {

													there = true;
													continue;
												} else {
													continue;
												}
											} else {
												continue;
											}
										}
										if (there == false) {

											clique[v1] = adjLists[person.vertexNum];

											v1++;
											person = person.next;
										}
										if (there == true) {

											person = person.next;
										}

									}
								} else {

									continue;
								}
							} else {
								continue;
							}
						}
					}

				}
			}
			int size = 0;
			for (int j = 0; j < clique.length; j++) {
				if (clique[j] != null) {
					size++;
				} else {
					continue;
				}
			}

			boolean[] cliqued = new boolean[entireSchool.length];

			for (int y = 0; y < entireSchool.length; y++) {
				for (int u = 0; u < clique.length; u++) {
					if (clique[u] != null && entireSchool[y] != null) {
						if (clique[u].name
								.equalsIgnoreCase(entireSchool[y].name)) {

							cliqued[y] = true;
						} else {
							continue;
						}
					} else {
						continue;
					}
				}
			}

			int x = 0;
			for (int a = 0; a < clique.length; a++) {
				if (clique[a] != null) {
					x++;
				} else {
					continue;
				}
			}
			allCliques.add(clique);
			clique = new Vertex[entireSchool.length];
			Vertex[] newSchool = new Vertex[entireSchool.length - x];

			int vert = 0;
			for (int w = 0; w < entireSchool.length; w++) {

				if (cliqued[w] != true && entireSchool[w] != null) {

					newSchool[vert] = entireSchool[w];
					vert++;
				} else {
					continue;
				}

			}

			entireSchool = newSchool;
			v1 = 0;

			if (entireSchool.length == 0) {

				again = false;

			} else {

				again = true;
			}

		}
		System.out.println("");
		int x = 1;
		for (int i = 0; i < allCliques.size(); i++) {

			System.out.println("");
			System.out.println("Clique " + x + ":");
			System.out.println("");
			x++;
			int size = 0;
			for (int j = 0; j < allCliques.get(i).length; j++) {
				if (allCliques.get(i)[j] != null) {
					size++;
				} else {
					continue;
				}
			}

			System.out.println(size);
			for (int k = 0; k < allCliques.get(i).length; k++) {
				if (allCliques.get(i)[k] != null) {
					System.out.println(allCliques.get(i)[k].name + "|y|"
							+ allCliques.get(i)[k].school);
				} else {
					continue;
				}
			}
			for (int p = 0; p < allCliques.get(i).length; p++) {
				if (allCliques.get(i)[p] != null) {
					Neighbor person = allCliques.get(i)[p].adjList;
					while (person != null) {
						if (person.vertexNum < indexForName(allCliques.get(i)[p].name)) {
							person = person.next;
						} else {
							if (allCliques.get(i)[p] != null) {
								boolean there = false;
								for (int y = 0; y < allCliques.get(i).length; y++) {
									if (allCliques.get(i)[y] != null) {
										if (adjLists[person.vertexNum].name
												.equals(allCliques.get(i)[y].name)) {
											there = true;
										} else {
											continue;
										}
									}
								}

								if (there == true) {
									System.out
											.println(allCliques.get(i)[p].name
													+ "|"
													+ adjLists[person.vertexNum].name);
								}
							} else {
								continue;
							}
							person = person.next;
						}
					}
				} else {
					continue;
				}
			}
		}
	}

	public void shortestPath(String start, String end) {

		System.out.println("");
		if (start == null || end == null) {
			System.out.println("One or more input was invalid.");
		}
		Vertex friend_A = null;
		

		// Checks if student is in list
		for (int i = 0; i < adjLists.length; i++) {
			if (adjLists[i].name.equalsIgnoreCase(start)) {
				friend_A = adjLists[i];
			} else {
				continue;
			}
		}

		if (friend_A == null) {
			System.out.println("Invalid input: " + start
					+ " was not found in list.");
			return;
		} else {
			boolean bool = false;
			for(int i = 0; i<adjLists.length; i++){
				if(adjLists[i].name.equalsIgnoreCase(end)){
					bool = true;
				}
				continue;
			}
			
			if (bool == false){
				System.out.println(end + " not found in list.");
				return;
			}
			
			Neighbor person = friend_A.adjList;
			// checks to see if they are already friends
			while (person != null) {
				if (adjLists[person.vertexNum].name.equalsIgnoreCase(end)) {
					System.out.println(start + " is already friends with "
							+ end);
					return;
				} else {
					person = person.next;
				}
			}
			person = friend_A.adjList;

		}

		start = start.toLowerCase();
		end = end.toLowerCase();

		int endIndex;
		Vertex vert;
		Neighbor n;
		Queue q = new Queue();

		// to check for visited
		Node[] visited = new Node[adjLists.length];
		Vertex[] path = new Vertex[adjLists.length];

		for (int i = 0; i < visited.length; i++) {
			visited[i] = new Node(false, -1, null);
		}

		endIndex = indexForName(end);
		visited[endIndex].wasVisited = true;
		q.enqueue(adjLists[endIndex]);

		while (!q.isEmpty()) {
			vert = q.dequeue();
			n = vert.adjList;// first edge in the linked list

			while (n != null) {
				if (visited[n.vertexNum].wasVisited == false) {
					visited[n.vertexNum].distance = 1;
					path[n.vertexNum] = vert;
					visited[n.vertexNum].wasVisited = true;

					q.enqueue(adjLists[n.vertexNum]);

				}
				n = n.next;
			}
		}

		// throws exception if there is no path between start and end user
		if (visited[indexForName(start)].distance == -1 || start.equals(end)) {
			System.out.println("No path exists between " + start + " and "
					+ end + "!");
		} else {
			int begin = indexForName(start);
			while (!end.equals(adjLists[begin].name)) {

				System.out.print(adjLists[begin].name + "--");

				begin = indexForName(path[begin].name);
			}
			System.out.println(end);
		}
	}
	
	ArrayList<String> connectors = new ArrayList<String>();
	int dfsn = 0;
	int start = 0;

	private void dfs(Vertex v) {

		System.out.println("");
		System.out.println("Starting Vertex: " + v.name);
		Neighbor nbr = v.adjList;
		
		dfsn++;
		v.dfsnum = dfsn;
		v.back = dfsn;
		v.visited = true;

		for(nbr = v.adjList; nbr!=null; nbr = nbr.next){
			
			Vertex w = adjLists[nbr.vertexNum];
			System.out.println(v.name+ "'s friend "+w.name+" came next");
			if (!w.visited){
				System.out.println("Recursive DFS from "+w.name);
				dfs(w);
				if (indexForName(v.name)!= start && v.dfsnum <= w.back){
					if(!connectors.contains(v.name)){
						System.out.println("Adding "+v.name+" to the connectors list.");
						connectors.add(v.name);
					
					}
				}
				if(v.dfsnum > w.back){
					v.back = Math.min(v.back, w.back);
					
				}
			}
			else{
				
				
			
				System.out.println(w.name+" was visited already.");
				v.back = Math.min(v.back, w.dfsnum);
				
			}
			
		}
		
			
	}

	public void connectors() {

		for(int i = 0; i < adjLists.length; i++) {

			Vertex v = adjLists[i];
			
			
			if(!v.visited) {
			
			
				dfsn = 0;
				start = i;
				dfs(v);
			}else{
				continue;
			}
		}
		System.out.println("");
		System.out.println("Connectors: ");
		System.out.println("");
		for (int i = 0; i<connectors.size(); i++){
			if (i==connectors.size()-1){
				System.out.print(connectors.get(i));
			}else{
				System.out.print(connectors.get(i)+",");}
		}
		System.out.println("");
	}	
	

}
