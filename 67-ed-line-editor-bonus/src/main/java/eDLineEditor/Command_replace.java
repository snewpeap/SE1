package eDLineEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command_replace implements Command {
    private String rawCmd;
    private Editor editor;

    public Command_replace(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        ArrayList<String> args = getArgument();
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd,editor);
        int toIndex = indexs.getOrDefault(TO,editor.getIndex());
        int fromIndex = indexs.getOrDefault(FROM, toIndex);
        if (args == null){
            editor.replace(fromIndex, toIndex, true, "", "", "");
            return;
        }
        String beReplaced = args.get(0); String replaceBy = args.get(1); String replaceMode = args.get(2);
        editor.replace(fromIndex, toIndex, false, beReplaced, replaceBy, replaceMode);
    }

    public ArrayList<String> getArgument() throws Exception {
        Matcher m; ArrayList<String> arguments = null;
        if (rawCmd.contains("\n")){
            m = Pattern.compile(".+\n(.+)").matcher(rawCmd);
        }else {
            m = Pattern.compile("(.+)").matcher(rawCmd);
        }
        if (!m.matches()){
            return arguments;
        }
        String arg = m.group(1);
        rawCmd = rawCmd.substring(0,m.start(1));
        Matcher m2 = Pattern.compile("/(.*)/(.*)/((g|[0-9]+)?)").matcher(arg);
        if (!m2.matches()){
            throw new Exception("?");
        }
        arguments = new ArrayList<>(3);
        for (int i=1;i<=3;i++){
            arguments.add(m2.group(i));
        }
        return arguments;
    }
}
