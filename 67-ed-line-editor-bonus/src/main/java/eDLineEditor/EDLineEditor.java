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
        fileName = "";
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
        catch (IOException ignored){}
    }
}
