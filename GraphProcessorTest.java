import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.*;

/////////////////////////////////////////////////////////////////////////////
//Semester:         CS400 Spring 2018
//PROJECT:          cs400_p4_ Due 4/16/18
//FILES:            GraphProcessorTest.java
// Written by Brandon Jonen of X-team
//USER:             jonen
//
//Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
//Bugs:             no known bugs
//
////////////////////////////80 columns wide //////////////////////////////////
public class GraphProcessorTest {

	private GraphProcessor graphproc1; //graph processor object 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.graphproc1 = new GraphProcessor(); //generate new for each test
	}

	@After
	public void tearDown() throws Exception {
		this.graphproc1 = null; 
	}
	/*
	 * Tests populate graph returns correct number of insertions for small graph
	 */
	@Test
	public final void populateGraphShouldReturnCorrectNumberSmall() {

		String words ="" + graphproc1.populateGraph("words_list2.txt");
		int expectedWords = 0;
		
		try {
	        File file = new File("words_list2.txt"); //read in the file
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        //ArrayList inputData = new ArrayList<>();
	        String line = br.readLine();
	        
	        while (line != null) {
	            if (line.trim() != "") {
	            	expectedWords++; //add number of words
	            }
	            line = br.readLine();
	        }
	        br.close();

		}
			
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String expected = ""+expectedWords;
		if (!words.equals(expected)) {
			System.out.println("Failed: expected: "+expected+ " actual: "+words);
			fail("expected: "+expected+ " actual: "+words);
		}
	}
	
	/*
	 * tests populate graph returns correct number of entries for large graph
	 */
	@Test
	public final void populateGraphShouldReturnCorrectNumbersLarge() {

		String words ="" + graphproc1.populateGraph("largelist.txt");
		int expectedWords = 0;
		
		try {
	        File file = new File("largelist.txt"); //load a large list
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String line = br.readLine();
	        
	        while (line != null) {
	            if (line.trim() != "") {
	            	expectedWords++;
	            }
	            line = br.readLine();
	        }
	        br.close();

		}
			
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String expected = ""+expectedWords;
		if (!words.equals(expected)) {
			System.out.println("Failed long: expected: "+expected+ " actual: "+words);
			fail("expected: "+expected+ " actual: "+words);
		}
	}
	
	/*
	 * Tests that populate graph returns -1 if file is not present.
	 */
	@Test
	public final void populateGraphShouldReturnNeg1IfNoFile() {

		int actual = graphproc1.populateGraph("notpresentfile.fake");
		int expected = -1;
		
		if (expected != actual) {
			System.out.println("Failed: expected: "+expected+ " actual: "+actual);
			fail("expected: "+expected+ " actual: "+actual);
		}
	}
	
	/*
	 * Tests that getshortestpath returns the correct path for a short path
	 */
	@Test
	public final void getShortPathReturnsCorrectPathShort() {

		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		List<String> actualwordlist = graphproc1.getShortestPath("chat","hat");
				
		List<String> expectedwordList = new ArrayList<String>();
		expectedwordList.add("chat");
		expectedwordList.add("hat");
		boolean match = true;
		if (expectedwordList.size() == actualwordlist.size()) {
			for (int i =0; i < expectedwordList.size();i++) {
				if (!expectedwordList.get(i).equals(actualwordlist.get(i))) {
					match = false;
				}
			}
		}
		else {
			match = false;//lists are different size
		}
		if (!match) {
			System.out.println("Failed: expected: "+expectedwordList+ " actual: "+actualwordlist);
			fail("expected: "+expectedwordList+ " actual: "+actualwordlist);
		}
	}
	
	/*
	 * Tests getshortestpath returns correct path for a longer path
	 */
	@Test
	public final void getShortPathReturnsCorrectPathLonger() {

		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		List<String> actualwordlist = graphproc1.getShortestPath("chat","scarce");
				
		List<String> expectedwordList = new ArrayList<String>();
		expectedwordList.add("chat");
		expectedwordList.add("hat");
		expectedwordList.add("hate");
		expectedwordList.add("hates");
		expectedwordList.add("dates");
		expectedwordList.add("dated");
		expectedwordList.add("dared");
		expectedwordList.add("cared");
		expectedwordList.add("scared");
		expectedwordList.add("scare");
		expectedwordList.add("scarce");

		boolean match = true;
		if (expectedwordList.size() == actualwordlist.size()) {
			for (int i =0; i < expectedwordList.size();i++) {
				if (!expectedwordList.get(i).equals(actualwordlist.get(i))) {
					match = false;
				}
			}
		}
		else {
			match = false;//lists are different size
		}
		if (!match) {
			System.out.println("Failed: expected: "+expectedwordList+ " actual: "+actualwordlist);
			fail("expected: "+expectedwordList+ " actual: "+actualwordlist);
		}
	}
	
	/*
	 * Tests getshortest path returns empty if parameters given are equal
	 */
	@Test
	public final void getShortPathShouldReturnEmptyonEqualParams() {
	
		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		List<String> actualwordlist = graphproc1.getShortestPath("chat","chat");
				
		List<String> expectedwordList = new ArrayList<String>();
		
		if (expectedwordList.size() != actualwordlist.size()) {
			System.out.println("Failed: expected: "+expectedwordList+ " actual: "+actualwordlist);
			fail("expected: "+expectedwordList+ " actual: "+actualwordlist);
		}
	}
	
	/*
	 * Tests get shortest distance returns correct for a short path
	 */
	@Test
	public final void getShorDistCorrectSmall() {
		
		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		int actualdist = graphproc1.getShortestDistance("chat", "hat");
		int expecteddist = 1;
				
		if (expecteddist != actualdist) {
			System.out.println("Failed: expected: "+expecteddist+ " actual: "+actualdist);
			fail("expected: "+expecteddist+ " actual: "+actualdist);			
		}
	}
	
	/*
	 * Tests get shortest distance returns acorrect for a small file with a medium path
	 */
	@Test
	public final void getShorDistCorrectlonger() {
		
		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		int actualdist = graphproc1.getShortestDistance("chat", "scarce");
		int expecteddist = 10;
				
		if (expecteddist != actualdist) {
			System.out.println("Failed: expected: "+expecteddist+ " actual: "+actualdist);
			fail("expected: "+expecteddist+ " actual: "+actualdist);			
		}

	}
	
	/*
	 * tests get shortest distance returns correct for a large file (not very long path)
	 */
	@Test
	public final void getShorDistCorrectLargeFile() {
		
		graphproc1.populateGraph("largelist.txt"); //using my short custom list here 
		int actualdist = graphproc1.getShortestDistance("aaaa", "ffff");
		int expecteddist = 4;
				
		if (expecteddist != actualdist) {
			System.out.println("Failed: expected: "+expecteddist+ " actual: "+actualdist);
			fail("expected: "+expecteddist+ " actual: "+actualdist);			
		}

	}
	
	/*
	 * tests getshortestpath returns -1 if the parameters given are equal
	 */
	@Test
	public final void getShorDistShoulrdReturnNeg1IfEqual() {
		
		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		int actualdist = graphproc1.getShortestDistance("chat", "chat");
		int expecteddist = -1;
				
		if (expecteddist != actualdist) {
			System.out.println("Failed: expected: "+expecteddist+ " actual: "+actualdist);
			fail("expected: "+expecteddist+ " actual: "+actualdist);			
		}

	}
	
	/*
	 * tests getshortestdistance returns -1 if there is no path
	 */
	@Test
	public final void getShorDistShouldReturnNeg1IfNoPath() {
		
		graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		int actualdist = graphproc1.getShortestDistance("chat", "blind");
		int expecteddist = -1;
				
		if (expecteddist != actualdist) {
			System.out.println("Failed: expected: "+expecteddist+ " actual: "+actualdist);
			fail("expected: "+expecteddist+ " actual: "+actualdist);			
		}

	}
	
	/*
	 * tests isadjacent to be true if one character is deleted
	 */
	@Test
	public final void isAdjacentShouldReturTtruDdelete() {
		
		//graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		boolean actualadjacent = WordProcessor.isAdjacent("chat","hat");
		boolean expectedadjacent = true;
				
		if (expectedadjacent != actualadjacent) {
			System.out.println("Failed: expected: "+expectedadjacent+ " actual: "+actualadjacent);
			fail("expected: "+expectedadjacent+ " actual: "+actualadjacent);			
		}

	}
	
	/*
	 * tests is adjacent if one character is added
	 */
	@Test
	public final void isAdjacentShouldReturnTrueAdd() {
		
		//graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		boolean actualadjacent = WordProcessor.isAdjacent("chat","chats");
		boolean expectedadjacent = true;
				
		if (expectedadjacent != actualadjacent) {
			System.out.println("Failed: expected: "+expectedadjacent+ " actual: "+actualadjacent);
			fail("expected: "+expectedadjacent+ " actual: "+actualadjacent);			
		}

	}
	
	/*
	 * tests is adjacent if on character is changed
	 */
	@Test
	public final void isAdjacentShouldReturnTrueChange() {
		
		//graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		boolean actualadjacent = WordProcessor.isAdjacent("chat","chap");
		boolean expectedadjacent = true;
				
		if (expectedadjacent != actualadjacent) {
			System.out.println("Failed: expected: "+expectedadjacent+ " actual: "+actualadjacent);
			fail("expected: "+expectedadjacent+ " actual: "+actualadjacent);			
		}

	}
	
	/*
	 * tests adjacent if 2 chars are changed
	 */
	@Test
	public final void isAdjacentShouldReturnFalseChange2() {
		
		//graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		boolean actualadjacent = WordProcessor.isAdjacent("chat","chia");
		boolean expectedadjacent = false;
				
		if (expectedadjacent != actualadjacent) {
			System.out.println("Failed: expected: "+expectedadjacent+ " actual: "+actualadjacent);
			fail("expected: "+expectedadjacent+ " actual: "+actualadjacent);			
		}

	}
	
	/*
	 * tests adjacent if 3 characters are added
	 */
	@Test
	public final void IsAdjacentShouldReturnFalseAdd3() {
		
		//graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		boolean actualadjacent = WordProcessor.isAdjacent("chat","chatter");
		boolean expectedadjacent = false;
				
		if (expectedadjacent != actualadjacent) {
			System.out.println("Failed: expected: "+expectedadjacent+ " actual: "+actualadjacent);
			fail("expected: "+expectedadjacent+ " actual: "+actualadjacent);			
		}

	}
	
	/*
	 * tests getstream matches expected from file
	 */
	@Test
	public final void getstreamShouldMatch(){
		
		//graphproc1.populateGraph("words_list2.txt"); //using my short custom list here 
		Stream<String> actualStream = null;
		try {
			actualStream = WordProcessor.getWordStream("words_list2.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean match = true;
		List<String> actualStreamList = actualStream.collect(Collectors.toList());
	
		if (actualStreamList == null) {
			match = false;
		}
		
		List<String> expectedwordList = new ArrayList<String>();
		expectedwordList.add("chat");
		expectedwordList.add("hat");
		expectedwordList.add("mat");
		expectedwordList.add("hate");
		expectedwordList.add("hates");
		expectedwordList.add("dates");
		expectedwordList.add("dated");
		expectedwordList.add("dared");
		expectedwordList.add("cared");
		expectedwordList.add("scared");
		expectedwordList.add("scaredy");
		expectedwordList.add("scare");
		expectedwordList.add("scarce");
		expectedwordList.add("blind");

		for (int i = 0; i < expectedwordList.size();i++) {
			System.out.println(actualStreamList.get(i));
			System.out.println(expectedwordList.get(i).toUpperCase());
			System.out.println();
			if (!expectedwordList.get(i).toUpperCase().equals(actualStreamList.get(i))) {
				match = false;
			}
		}
				
		if (!match) {
			System.out.println("Failed: expected: "+expectedwordList+ " actual: "+actualStreamList);
			fail("expected: "+expectedwordList+ " actual: "+actualStreamList);			
		}

	}
	
	/*
	 * tests getstreeam throws io exception on a bad filename
	 */
	@Test
	public final void getstreamShouldThrowNoSuchFileOnBadFile() throws IOException{
		Stream<String> actualStream;
		try {
			actualStream = WordProcessor.getWordStream("noneexistingfile.fake");
			//an exception should be thrown and enter catch block
			System.out.println("Failed: expected: "+"exception thrown"+ " actual: "+"no exception thrown");
			fail("expected: "+"exception thrown"+ " actual: "+"no exception thrown");	
		} catch (NoSuchFileException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();//stack trace is printed but this is expected behavior
		}
	}

}
