package eDLineEditor;

import java.io.*;
import java.util.Scanner;

public class EDLineEditor {
    public static BufferedReader br;

	private static String fileName;
	public static String getFileName() {
		return fileName;
	}
	public static void setFileName(String fileName) {
		EDLineEditor.fileName = fileName;
	}

	public static void main(String[] args){
        try {
            Scanner sc = new Scanner(new FileInputStream("/var/ws/tmp/src/test/java/eDLineEditor/EDLineEditorBonusTest.java"));
            while (sc.hasNextLine()){
                System.out.println(sc.nextLine());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
		/*fileName = "";
		Editor editor = new Editor();
		Invoker invoker = new Invoker();
        br = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in,100)));
		String nextCmd = null;
        try{
            while ((nextCmd = br.readLine()) != null) {
                try {
                    Command command = invoker.getCommand(nextCmd);
                    command.setEditor(editor);
                    invoker.setCommand(command);
                    invoker.run();
                    editor.syncSignsList();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException ignored){}*/
    }
}
