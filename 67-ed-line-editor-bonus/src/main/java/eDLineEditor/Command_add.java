package eDLineEditor;

import java.util.HashMap;

public class Command_add implements Command {
    private String rawCmd;
    private Editor editor;
    private boolean isAddAfter;

    public Command_add(String rawCmd, boolean isAddAfter) {
        this.rawCmd = rawCmd;
        this.isAddAfter = isAddAfter;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        getNoArgument(rawCmd);
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int row = indexs.getOrDefault(TO,editor.getIndex());
        editor.add(row,isAddAfter);
    }

}
