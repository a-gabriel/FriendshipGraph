import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Driver {
	public static void main(String[] args) throws FileNotFoundException{
		Friends friends = new Friends();
		friends.Graph("graphIndo");
		 
		
		friends.subGraph("rutgers");
		
		friends.islands("rutgers");
		
		friends.shortestPath("sam", "sergei");
		
	
	}
}
