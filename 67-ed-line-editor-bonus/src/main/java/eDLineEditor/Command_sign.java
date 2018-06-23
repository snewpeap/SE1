package eDLineEditor;

import java.util.HashMap;

public class Command_sign implements Command {
    private Editor editor;
    private String rawCmd;

    public Command_sign(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        String sign = rawCmd.contains("\n")?rawCmd.split("\n")[1]:rawCmd;
        if (!sign.matches("[a-z]")) {
            throw new Exception("?");
        }
        sign = "\'" + sign;
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd,editor);
        int row = indexs.getOrDefault(TO,editor.getIndex());
        editor.setSign(row,sign);
    }

}
