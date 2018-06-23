package eDLineEditor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command_write implements Command {
    private Editor editor;
    private String rawCmd;
    private boolean isAppend;

    public Command_write(String rawCmd, boolean isAppend) {
        this.rawCmd = rawCmd;
        this.isAppend = isAppend;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        String fileName = getArgument(rawCmd);
        HashMap<String,Integer> indexs = getFromAndToIndex(rawCmd,editor);
        int toIndex = indexs.getOrDefault(TO,editor.getTotal()-1);
        int fromIndex = indexs.getOrDefault(FROM, indexs.containsKey(TO)?toIndex:0);
        editor.writeFile(fromIndex,toIndex,fileName,isAppend);
    }

    public String getArgument(String rawCmd) throws Exception {
        Pattern p = Pattern.compile(".*(?:(?: )|(?:\n ))([^/?]+)");
        String fileName = "";String defaultFileName = EDLineEditor.getFileName();
        if (rawCmd.matches(p.toString())){
            Matcher m = p.matcher(rawCmd);
            if (m.find()) {
                fileName = m.group(1);
            }
            if (defaultFileName.equals("")){
                EDLineEditor.setFileName(fileName);
            }
        }else {
            if (defaultFileName.equals("")){
                throw new Exception("?");
            }
            fileName = defaultFileName;
        }
        return fileName;
    }
}
