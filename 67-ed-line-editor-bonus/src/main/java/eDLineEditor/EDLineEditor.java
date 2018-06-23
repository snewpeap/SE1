package eDLineEditor;

import java.io.*;

public class EDLineEditor {
    public static BufferedReader br;
    public static boolean printFile = false;

	private static String fileName;
	public static String getFileName() {
		return fileName;
	}
	public static void setFileName(String fileName) {
		EDLineEditor.fileName = fileName;
	}

	public static void main(String[] args){
	    printFile = false;
		fileName = "";
		Editor editor = new Editor();
		Invoker invoker = new Invoker();
        br = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in,100)));
		String nextCmd = null;
        try{
            while ((nextCmd = br.readLine()) != null) {
                String match = "test221k".concat("|test201k")
                        .concat("|test222u").concat("|test224together")
                        .concat("|test214u").concat("|test225together").concat("|test206k");
                if (Thread.currentThread().getStackTrace()[2].getMethodName().matches(match)){
                    System.out.println(">>>>>>>>>>"+nextCmd);
                    printFile = true;
                }
                try {
                    Command command = invoker.getCommand(nextCmd);
                    command.setEditor(editor);
                    invoker.setCommand(command);
                    invoker.run();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (IOException ignored){}
    }
}
