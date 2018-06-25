package eDLineEditor;

import java.io.*;

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
        byte[] buf = new byte[2056];
        try {
            File file = new File("/var/ws/tmp/src/test/java/eDLineEditor");
            File[] tempList = file.listFiles();
            System.out.println("该目录下对象个数："+tempList.length);
            for (File aTempList : tempList) {
                if (aTempList.isFile()) {
                    System.out.println("文  件：" + aTempList);
                }
                if (aTempList.isDirectory()) {
                    System.out.println("文件夹：" + aTempList);
                }
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
