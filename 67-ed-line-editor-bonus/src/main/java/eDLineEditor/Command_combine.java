package eDLineEditor;

import java.util.HashMap;

public class Command_combine implements Command {
    private String rawCmd;
    private Editor editor;

    public Command_combine(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        getNoArgument(rawCmd);
        HashMap<String,Integer> indexs= getFromAndToIndex(rawCmd,editor);
        int toIndex = indexs.getOrDefault(TO,editor.getIndex()+1);
        if (toIndex==editor.getTotal()){
            throw new Exception("?");
        }
        int fromIndex = indexs.getOrDefault(FROM,editor.getIndex());
        editor.combine(fromIndex,toIndex);
    }
}
