package eDLineEditor;

import java.util.HashMap;

public class Command_change implements Command {
    private Editor editor;
    private String rawCmd;

    public Command_change(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        getNoArgument(rawCmd);
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd,editor);
        int toIndex = indexs.getOrDefault(TO,editor.getIndex());
        int fromIndex = indexs.getOrDefault(FROM, toIndex);
        editor.change(fromIndex,toIndex);
    }
}
