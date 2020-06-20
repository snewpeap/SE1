package eDLineEditor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EDLineEditor {
    public static BufferedReader br;

    private static String fileName;

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        EDLineEditor.fileName = fileName;
    }

    public static void main(String[] args) {
        fileName = "";
        Editor editor = new Editor();
        Commander commander = new Commander();
        br = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in, 100)));
        String nextCmd;
        try {
            while ((nextCmd = br.readLine()) != null) {
                try {
                    Command command = commander.getCommand(nextCmd);
                    command.setEditor(editor);
                    commander.setCommand(command);
                    commander.run();
                } catch (Wrong wrong) {
                    System.out.println(wrong.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
