package eDLineEditor;

import java.util.HashMap;

public class Command_printOrder implements Command {
    private Editor editor;
    private String rawCmd;

    public Command_printOrder(String rawCmd) {
        this.rawCmd = rawCmd;
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
        //越俎代庖
        System.out.println(row+1);
    }

}
