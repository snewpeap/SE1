package eDLineEditor;

import java.util.HashMap;

public class Command_move implements Command {
    private Editor editor;
    private String rawCmd;

    public Command_move(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        int target = getArgument(rawCmd,editor);
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd,editor);
        int toIndex = indexs.getOrDefault(TO,editor.getIndex());
        int fromIndex = indexs.getOrDefault(FROM, toIndex);
        editor.move(fromIndex,toIndex,target);
    }

}
