package eDLineEditor;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EDLineEditorBonusTest {

	InputStream in = null;
	PrintStream out = null;
	
	InputStream inputStream = null;
	ByteArrayOutputStream outputStream = null;
	
    String lineBreak = null;
	
    @Before
	public void setUp() {
    	in = System.in;
		out = System.out;
		outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        lineBreak = System.getProperty("line.separator");
	}
	
	@After
	public void tearDown() {
		System.setIn(in);
		System.setOut(out);
	}


	/**
	 * command k with default address
	 * command p and = with the mark
	 */
	@Test
	public void test201k() {
		String testFile = "test201";
		String content = "//201k" + lineBreak + 
				"todo:go shopping" + lineBreak + 
				"todo:do exercises" + lineBreak + 
				"todo:order delivery" + lineBreak + 
				"todo:take online course" + lineBreak + 
				"todo:listen to a report" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test201" + lineBreak,
				"ka" + lineBreak,
				"'ap" + lineBreak,
				"'a-1=" + lineBreak,
				"3ka" + lineBreak,
				"'a+1=" + lineBreak,
				"'ap" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:listen to a report" + lineBreak,
				"5" + lineBreak,
				"4" + lineBreak,
				"todo:do exercises" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command p and = with the mark after command k marking same symbol for multiple times . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k with pointed address
	 * test the mark after command d deleting the line marked
	 */
	@Test
	public void test202k() {
		String testFile = "test202";
		String content = "//202k" + lineBreak + 
				"todo:go shopping" + lineBreak + 
				"todo:do exercises" + lineBreak + 
				"todo:order delivery" + lineBreak + 
				"todo:take online course" + lineBreak + 
				"todo:listen to a report" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test202" + lineBreak,
				"4kb" + lineBreak,
				"'bp" + lineBreak,
				"3,5d" + lineBreak,
				"'bp" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:order delivery" + lineBreak,
				"?" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark after commad d deleting the line marked by k . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}

	/**
	 * command k with pointed address
	 * test the mark after command d deleting the line before the mark
	 */
	@Test
	public void test203k() {
		String testFile = "test203";
		String content = "//203k" + lineBreak + 
				"todo:go shopping" + lineBreak + 
				"todo:do exercises" + lineBreak + 
				"todo:order delivery" + lineBreak + 
				"todo:take online course" + lineBreak + 
				"todo:listen to a report" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test203" + lineBreak,
				"$-1kc" + lineBreak,
				"'cp" + lineBreak,
				".-2,4d" + lineBreak,
				"'cp" + lineBreak,
				"'c=" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:take online course" + lineBreak,
				"todo:take online course" + lineBreak,
				"3" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark with p and = after command d deleting the line before the mark . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k
	 * test the mark after command a adding contents before the mark
	 */
	@Test
	public void test204k() {
		String testFile = "test204";
		String content = "//204k" + lineBreak + 
				"todo:take online course" + lineBreak + 
				"todo:do exercises" + lineBreak + 
				"todo:order delivery" + lineBreak + 
				"todo:go online shopping" + lineBreak + 
				"todo:listen to a report" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test204" + lineBreak,
				"?online?kd" + lineBreak,
				"'dp" + lineBreak,
				"'d-1a" + lineBreak,
				"completed:greeting" + lineBreak,
				"completed:take a snap" + lineBreak,
				"." + lineBreak,
				"'dp" + lineBreak,
				"'d=" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:go online shopping" + lineBreak,
				"todo:go online shopping" + lineBreak,
				"7" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark with p and = after command a adding contents before the mark . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k
	 * test the mark after command i adding contents before the mark
	 */
	@Test
	public void test205k() {
		String testFile = "test205";
		String content = "meat:beef" + lineBreak + 
				"vegetable:cabbage" + lineBreak + 
				"food:noodles" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test205" + lineBreak,
				"?vegetable?kp" + lineBreak,
				"'p=" + lineBreak,
				"'pp" + lineBreak,
				"'pi" + lineBreak,
				"vegetable:carrot" + lineBreak,
				"food:rice" + lineBreak,
				"meat:goat" + lineBreak,
				"." + lineBreak,
				"'p=" + lineBreak,
				"'pp" + lineBreak,
				"'p-2p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"2" + lineBreak,
				"vegetable:cabbage" + lineBreak,
				"5" + lineBreak,
				"vegetable:cabbage" + lineBreak,
				"food:rice" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark after command i adding contents before the mark . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k
	 * test the mark after command m moving the mark
	 */
	@Test
	public void test206k() {
		String testFile = "test206";
		String content = "//206k" + lineBreak + 
				"todo:take online course" + lineBreak + 
				"todo:do exercises" + lineBreak + 
				"todo:order delivery" + lineBreak + 
				"todo:go online shopping" + lineBreak + 
				"todo:listen to a report" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test206" + lineBreak,
				"/online/ke" + lineBreak,
				"'ep" + lineBreak,
				"'em/order/" + lineBreak,
				"'e-1p" + lineBreak,
				"'e=" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:take online course" + lineBreak,
				"todo:order delivery" + lineBreak,
				"4" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark with p and = after command m moving the mark . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}

	/**
	 * command k,m
	 * test the mark after command m moving the line before the mark
	 */
	@Test
	public void test207k() {
		String testFile = "test207";
		String content = "website:moocoder,github" + lineBreak + 
				"appliance:tv,air conditioner " + lineBreak + 
				"digital product:mobile,camera" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test207" + lineBreak,
				"2kt" + lineBreak,
				"'tp" + lineBreak,
				"1m/er/" + lineBreak,
				"'t=" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"appliance:tv,air conditioner " + lineBreak,
				"1" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark with = after command m moving lines before the mark . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k,m
	 * test the mark after command m moving lines to the place before the mark
	 */
	@Test
	public void test208k() {
		String testFile = "test208";
		String content = "website:moocoder,github" + lineBreak + 
				"appliance:tv,air conditioner " + lineBreak + 
				"digital product:mobile,camera" + lineBreak + 
				"software:mooc,steam" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test208" + lineBreak,
				"2kt" + lineBreak,
				"'tp" + lineBreak,
				"'t+1,$m/:mooco/" + lineBreak,
				"'t=" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"appliance:tv,air conditioner " + lineBreak,
				"4" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark after command m moving lines to the place before the mark . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k
	 * test the mark after command j combining the mark with other contents
	 */
	@Test
	public void test209k() {
		String testFile = "test209";
		String content = "//209k" + lineBreak + 
				"todo:visit a doctor" + lineBreak + 
				"todo:do exercises" + lineBreak + 
				"todo:order delivery" + lineBreak + 
				"todo:listen to a report" + lineBreak + 
				"todo:go online shopping" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test209" + lineBreak,
				"?a?kf" + lineBreak,
				"'fp" + lineBreak,
				"'f-1,'fj" + lineBreak,
				"'f=" + lineBreak,
				".p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:listen to a report" + lineBreak,
				"?" + lineBreak,
				"todo:order deliverytodo:listen to a report" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark with p and = after command j combining the mark with other contents . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}

	/**
	 * command k
	 * test the mark after command s replace content in the line marked
	 */
	@Test
	public void test210k() {
		String testFile = "test210";
		String content = "//210k" + lineBreak + 
				"todo:thing A" + lineBreak + 
				"todo:thing B" + lineBreak + 
				"todo:thing C" + lineBreak + 
				"completed:thing D" + lineBreak + 
				"completed:thing E" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test210" + lineBreak,
				"?todo?kg" + lineBreak,
				"'gp" + lineBreak,
				"'gs/todo/completed/1" + lineBreak,
				"'g=" + lineBreak,
				".p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:thing C" + lineBreak,
				"?" + lineBreak,
				"completed:thing C" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test the mark with = after command s replace content in the line marked . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command k
	 * test multiple marks and save them with command W
	 */
	@Test
	public void test211k() {
		String testFile = "test211";
		String content = "//211k" + lineBreak + 
				"todo:thing D" + lineBreak + 
				"completed:thing A" + lineBreak + 
				"todo:thing E" + lineBreak + 
				"completed:thing B" + lineBreak + 
				"todo:thing C" + lineBreak;
		createFile(testFile, content);

		String file2 = "test211_2";
		File file = new File(file2);
        if(file.exists()) {
        	file.delete();
        }
		
		String[] inputs = {
				"ed test211" + lineBreak,
				"/completed/kh" + lineBreak,
				"'hp" + lineBreak,
				"/completed/ki" + lineBreak,
				"'ip" + lineBreak,
				"/completed/kj" + lineBreak,
				"'h,'ip" + lineBreak,
				"'h,'jp" + lineBreak,
				"'i,'jp" + lineBreak,
				"'hW test211_2" + lineBreak,
				"'iW test211_2" + lineBreak,
				"'jW test211_2" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"completed:thing A" + lineBreak,
				"completed:thing B" + lineBreak,
				"completed:thing A" + lineBreak + "todo:thing E" + lineBreak + "completed:thing B" + lineBreak,
				"completed:thing A" + lineBreak,
				"?" + lineBreak,
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command p with multiple marks . Your output does not match!", arrayToString(outputs), outputStream.toString());
		
		String fileContent2 = "completed:thing A" + lineBreak + 
				"completed:thing B" + lineBreak + 
				"completed:thing A" + lineBreak;
		
		assertFile(file2, fileContent2, "Test command W with the mark . The file you save: test211_2's content does not match.");
	}
	
	/**
	 * command u after command i with search address
	 */
	@Test
	public void test212u() {
		String testFile = "test212";
		String content = "//212u" + lineBreak + 
				"todo:thing A" + lineBreak + 
				"todo:thing H" + lineBreak + 
				"completed:thing D" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test212" + lineBreak,
				"?todo?+1i" + lineBreak,
				"todo:thing Z" + lineBreak,
				"." + lineBreak,
				"2,$p" + lineBreak,
				"u" + lineBreak,
				"2,$p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:thing A" + lineBreak + "todo:thing H" + lineBreak + "todo:thing Z" + lineBreak + "completed:thing D" + lineBreak,
				"todo:thing A" + lineBreak + "todo:thing H" + lineBreak + "completed:thing D" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command i with search address . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u after command i with search address and command w
	 */
	@Test
	public void test213u() {
		String testFile = "test213";
		String content = "//213u" + lineBreak + 
				"todo:thing A" + lineBreak + 
				"todo:thing H" + lineBreak + 
				"completed:thing D" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test213" + lineBreak,
				"?todo?+1i" + lineBreak,
				"todo:thing Z" + lineBreak,
				"." + lineBreak,
				"2,$p" + lineBreak,
				"w" + lineBreak,
				"u" + lineBreak,
				"2,$p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"todo:thing A" + lineBreak + "todo:thing H" + lineBreak + "todo:thing Z" + lineBreak + "completed:thing D" + lineBreak,
				"todo:thing A" + lineBreak + "todo:thing H" + lineBreak + "completed:thing D" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command i with search address and command w . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u after command c with ranged search address
	 */
	@Test
	public void test214u() {
		String testFile = "test214";
		String content = "//214u" + lineBreak + 
				"todo:thing C" + lineBreak + 
				"todo:thing E" + lineBreak + 
				"completed:thing D" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test214" + lineBreak,
				"/todo/,?todo?c" + lineBreak,
				"." + lineBreak,
				",p" + lineBreak,
				"u" + lineBreak,
				",p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"//214u" + lineBreak + "completed:thing D" + lineBreak,
				"//214u" + lineBreak + "todo:thing C" + lineBreak + "todo:thing E" + lineBreak + "completed:thing D" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command c with /str/,?str? address . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u after command m
	 */
	@Test
	public void test215u() {
		String testFile = "test215";
		String content = "//215u" + lineBreak + 
				"start" + lineBreak + 
				"middle1qaz" + lineBreak + 
				"end2wsx" + lineBreak + 
				"middle3edc" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test215" + lineBreak,
				"/end/m$" + lineBreak,
				"2,$p" + lineBreak,
				"u" + lineBreak,
				"2,$p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"start" + lineBreak + "middle1qaz" + lineBreak + "middle3edc" + lineBreak + "end2wsx" + lineBreak,
				"start" + lineBreak + "middle1qaz" + lineBreak + "end2wsx" + lineBreak + "middle3edc" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command m . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}

	/**
	 * command u after command j
	 */
	@Test
	public void test216u() {
		String testFile = "test216";
		String content = "//216u" + lineBreak + 
				"NJU " + lineBreak + 
				"520 " + lineBreak + 
				"Congratulations." + lineBreak + 
				"end" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test216" + lineBreak,
				"/520/-1,/520/+1j" + lineBreak,
				";p" + lineBreak,
				"u" + lineBreak,
				"2,$p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"NJU 520 Congratulations." + lineBreak + "end" + lineBreak,
				"NJU " + lineBreak + "520 " + lineBreak + "Congratulations." + lineBreak + "end" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command j . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u after command s with param /str1/str2/
	 */
	@Test
	public void test217u() {
		String testFile = "test217";
		String content = "//217u" + lineBreak + 
				"test comman ed command" + lineBreak + 
				"test todo comman command" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test217" + lineBreak,
				",s/ comman//" + lineBreak,
				"2,$p" + lineBreak,
				"u" + lineBreak,
				"2,$p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"test ed command" + lineBreak + "test todo command" + lineBreak,
				"test comman ed command" + lineBreak + "test todo comman command" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command s with param /str1/str2/ . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u after command s with param /str1/str2/n
	 */
	@Test
	public void test218u() {
		String testFile = "test218";
		String content = "//218u comman" + lineBreak + 
				"test command ed comman" + lineBreak + 
				"test todo comman command" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test218" + lineBreak,
				",s/ comman//2" + lineBreak,
				"1,$p" + lineBreak,
				"u" + lineBreak,
				"1,$p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"//218u comman" + lineBreak + "test command ed" + lineBreak + "test todo command" + lineBreak,
				"//218u comman" + lineBreak + "test command ed comman" + lineBreak + "test todo comman command" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command s with param /str1/str2/n . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u after command d deleting contents include the line marked by command k
	 * the mark should be recovered
	 */
	@Test
	public void test219u() {
		String testFile = "test219";
		String content = "meat:pork" + lineBreak + 
				"vegetable:cabbage" + lineBreak + 
				"meat:fish" + lineBreak + 
				"vegetable:carrot" + lineBreak + 
				"meat:prawn" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test219" + lineBreak,
				"1p" + lineBreak,
				"/vegetable:/kk" + lineBreak,
				"'kp" + lineBreak,
				"1,3d" + lineBreak,
				"'kp" + lineBreak,
				"u" + lineBreak,
				"'kp" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"meat:pork" + lineBreak,
				"vegetable:cabbage" + lineBreak,
				"?" + lineBreak,
				"vegetable:cabbage" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u after command d deleting contents include the line marked by command k . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * command u for multiple times
	 */
	@Test
	public void test220u() {
		String testFile = "test220";
		String content = "meat:pork" + lineBreak + 
				"vegetable:cabbage" + lineBreak + 
				"meat:fish" + lineBreak + 
				"vegetable:carrot" + lineBreak + 
				"meat:prawn" + lineBreak;
		createFile(testFile, content);
		
		String file2 = "test220_2";
		String fileContent = "test220_2" + lineBreak;
		createFile(file2, fileContent);
		
		String[] inputs = {
				"ed test220" + lineBreak,
				"3i" + lineBreak,
				"vegetable:broccoli" + lineBreak,
				"." + lineBreak,
				"/meat/m/vegetable/" + lineBreak,
				".,$d" + lineBreak,
				"w test220_2" + lineBreak,
				"u" + lineBreak,
				",p" + lineBreak,
				"u" + lineBreak,
				",p" + lineBreak,
				"u" + lineBreak,
				",p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"meat:pork" + lineBreak + "vegetable:cabbage" + lineBreak + 
				"vegetable:broccoli" + lineBreak + "vegetable:carrot" + lineBreak + 
				"meat:fish" + lineBreak + "meat:prawn" + lineBreak,
				
				"meat:pork" + lineBreak + "vegetable:cabbage" + lineBreak + 
				"vegetable:broccoli" + lineBreak + "meat:fish" + lineBreak + 
				"vegetable:carrot" + lineBreak + "meat:prawn" + lineBreak,
				
				"meat:pork" + lineBreak + "vegetable:cabbage" + lineBreak + 
				"meat:fish" + lineBreak + "vegetable:carrot" + lineBreak + 
				"meat:prawn" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command u for multiple times (with command p showing contents) after command i,m,d and w with another file . Your output does not match!", arrayToString(outputs), outputStream.toString());
		
		String fileContent2 = "meat:pork" + lineBreak + 
				"vegetable:cabbage" + lineBreak + 
				"vegetable:broccoli" + lineBreak + 
				"vegetable:carrot" + lineBreak;
		
		assertFile(file2, fileContent2, "Test command w with another file before command u . The file you save: test220_2's content does not match.");
	}
	
	/**
	 * error using of command k
	 */
	@Test
	public void test221k() {
		String testFile = "test221";
		String content = "beef:meat" + lineBreak + 
				"cabbage:vegetable" + lineBreak + 
				"rice:food" + lineBreak + 
				"cabbage:vegetable" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test221" + lineBreak,
				"/vegetable/kQ" + lineBreak,
				"?e:?k3" + lineBreak,
				"'Qp" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"?" + lineBreak,
				"?" + lineBreak,
				"?" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Error using of param of command k (such as marking with letter of upper case,marking with number,error using of ' and so on) . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	/**
	 * test current address after command u
	 */
	@Test
	public void test222u() {
		String testFile = "test222";
		String content = "test_u" + lineBreak + 
				"001" + lineBreak + 
				"dd" + lineBreak + 
				"ff" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test222" + lineBreak,
				"2p" + lineBreak,
				"3d" + lineBreak,
				"2a" + lineBreak,
				"aa" + lineBreak,
				"ww" + lineBreak,
				"." + lineBreak,
				".=" + lineBreak,
				",p" + lineBreak,
				".=" + lineBreak,
				"u" + lineBreak,
				".=" + lineBreak,
				",p" + lineBreak,
				".=" + lineBreak,
				"u" + lineBreak,
				".=" + lineBreak,
				",p" + lineBreak,
				".=" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"001" + lineBreak,
				"4" + lineBreak,
				"test_u" + lineBreak + "001" + lineBreak + "aa" + lineBreak + "ww" + lineBreak + "ff" + lineBreak,
				"5" + lineBreak,
				"3" + lineBreak,
				"test_u" + lineBreak + "001" + lineBreak + "ff" + lineBreak,
				"3" + lineBreak,
				"2" + lineBreak,
				"test_u" + lineBreak + "001" + lineBreak + "dd" + lineBreak + "ff" + lineBreak,
				"4" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test current address after command u . Your output does not match!", arrayToString(outputs), outputStream.toString());
	}
	
	//????????????
	/**
	 * command i,j,s,p,u,Q
	 */
	@Test
	public void test223together() {
		String testFile = "test223";
		String content = "wear:" + lineBreak + 
				"Smartband&nbsp" + lineBreak;
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test223" + lineBreak,
				"/wear:/i" + lineBreak,
				"website:" + lineBreak,
				"gitlab&nbsp" + lineBreak,
				"jianshu&nbsp" + lineBreak,
				"google&nbsp" + lineBreak,
				"." + lineBreak,
				"?:?,.j" + lineBreak,
				".+1,$j" + lineBreak,
				",s/&nbsp/./" + lineBreak,
				".p" + lineBreak,
				"u" + lineBreak,
				".p" + lineBreak,
				"s/&nbsp//" + lineBreak,
				".p" + lineBreak,
				"1,$-1s/&nbsp/./g" + lineBreak,
				".p" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"wear:Smartband." + lineBreak,
				"wear:Smartband&nbsp" + lineBreak,
				"wear:Smartband" + lineBreak,
				"website:gitlab.jianshu.google." + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command i,j,s,p,u,Q . Your output does not match!", arrayToString(outputs), outputStream.toString());
		
		String file = "test223";
		String fileContent = "wear:" + lineBreak + 
				"Smartband&nbsp" + lineBreak;
		
		assertFile(file, fileContent, "Test command i,j,s,p,u,Q . You should not modify the file test223's content.");
	}

	/**
	 * command a,k,p,d,s,w,W,Q
	 */
	@Test
	public void test224together() {
		String testFile = "test224";
		String content = "";
		createFile(testFile, content);
		
		String[] inputs = {
				"ed test224" + lineBreak,
				"a" + lineBreak,
				"favourite:games" + lineBreak,
				"dislike:debating" + lineBreak,
				"dislike:coffee" + lineBreak,
				"favourite:noodles" + lineBreak,
				"." + lineBreak,
				"?favourite?kz" + lineBreak,
				"'zp" + lineBreak,
				"?favourite?ky" + lineBreak,
				"'yp" + lineBreak,
				"?favourite?kx" + lineBreak,
				"'x,'zp" + lineBreak,
				"1d" + lineBreak,
				"'xp" + lineBreak,
				"'zp" + lineBreak,
				"/dislike/s/dislike/favourite/1" + lineBreak,
				"'yw test224_favour" + lineBreak,
				".W test224_favour" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"favourite:games" + lineBreak,
				"favourite:noodles" + lineBreak,
				"favourite:games" + lineBreak,
				"?" + lineBreak,
				"?" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command a,k,p,d,s,w,W,Q . Your output does not match!", arrayToString(outputs), outputStream.toString());
		
		String file = "test224_favour";
		String fileContent = "favourite:noodles" + lineBreak + 
				"favourite:coffee" + lineBreak;
		
		assertFile(file, fileContent, "Test command a,k,p,d,s,w,W,Q . The file you save: test224_favour's content does not match.");
	}
	
	/**
	 * command i,d,j,s,p,u,w,Q
	 */
	@Test
	public void test225together() {
		String[] inputs = {
				"ed" + lineBreak,
				"i" + lineBreak,
				"fruit:apple" + lineBreak,
				"fruit:banana" + lineBreak,
				"snack:sweet" + lineBreak,
				"fruit:pineapple" + lineBreak,
				"." + lineBreak,
				"?snack?d" + lineBreak,
				",j" + lineBreak,
				"?fruit:?s/fruit:/ and /2" + lineBreak,
				".p" + lineBreak,
				"$s" + lineBreak,
				".p" + lineBreak,
				"1,$s" + lineBreak,
				"u" + lineBreak,
				".p" + lineBreak,
				"u" + lineBreak,
				".p" + lineBreak,
				"u" + lineBreak,
				"w test225_2" + lineBreak,
				"Q" + lineBreak
		};
		
		String[] outputs = {
				"fruit:apple and bananafruit:pineapple" + lineBreak,
				"fruit:apple and banana and pineapple" + lineBreak,
				"?" + lineBreak,
				"fruit:apple and bananafruit:pineapple" + lineBreak,
				"fruit:applefruit:bananafruit:pineapple" + lineBreak
		};
		
		inputStream = new ByteArrayInputStream(arrayToString(inputs).getBytes());
		System.setIn(inputStream);
		
		EDLineEditor.main(null);
		
		assertEquals("Test command i,d,j,s,p,u,w,Q . Your output does not match!", arrayToString(outputs), outputStream.toString());
		
		String file = "test225_2";
		String fileContent = "fruit:apple" + lineBreak + 
				"fruit:banana" + lineBreak + 
				"fruit:pineapple" + lineBreak;
		
		assertFile(file, fileContent, "Test command i,d,j,s,p,u,w,Q . The file you save: test225_2's content does not match.");
	}

	private String arrayToString(String[] array) {
		StringBuffer buffer = new StringBuffer();
		for(String str:array) {
			buffer.append(str);
		}
		return buffer.toString();
	}
	
	private void assertFile(String file, String expect, String failMessage){
		StringBuffer buffer = new StringBuffer();
		if(new File(file).exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				while((line=br.readLine())!=null) {
					buffer.append(line+lineBreak);
				}
				br.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		assertEquals(failMessage, expect, buffer.toString());
	}
	
	private void createFile(String file, String content) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}