package eDLineEditor;

import java.util.HashMap;

public class Command_z implements Command {
    private Editor editor;
    private String rawCmd;

    public Command_z(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        int offset = getArgument(rawCmd);
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int row = indexs.getOrDefault(TO,editor.getIndex()+1);
        editor.offSetPrint(row,offset);
    }

    public int getArgument(String rawCmd) throws Exception {
        if (rawCmd.split("\n").length == 1){
            return -1;
        }
        String arg = rawCmd.split("\n")[1];
        try{
            return Integer.parseInt(arg);
        }catch (NumberFormatException e){
            throw new Exception("?");
        }
    }
}
