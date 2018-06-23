package eDLineEditor;

import java.util.HashMap;

public class Command_delete implements Command{
    private Editor editor;
    private String rawCmd;

    public Command_delete(String rawCmd) {
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
        editor.delete(fromIndex,toIndex);
    }

}
