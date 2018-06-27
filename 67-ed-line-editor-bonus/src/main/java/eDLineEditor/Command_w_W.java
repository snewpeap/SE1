package eDLineEditor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command_w_W extends Command {
    private String rawCmd;
    private boolean isAppend;

    public Command_w_W(String rawCmd, boolean isAppend) {
        this.rawCmd = rawCmd;
        this.isAppend = isAppend;
    }

    @Override
    public void execute() throws Exception {
        String fileName = getArgument(rawCmd);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getTotal() - 1);
        int fromIndex = indexs.getOrDefault(Position.FROM, indexs.containsKey(Position.TO) ? toIndex : 0);
        editor.writeFile(fromIndex, toIndex, fileName, isAppend);
    }

    public String getArgument(String rawCmd) throws Wrong {
        Pattern p = Pattern.compile(".*(?:(?: )|(?:\n ))([^/?]+)");
        String fileName = "";
        String defaultFileName = EDLineEditor.getFileName();
        if (rawCmd.matches(p.toString())) {
            Matcher m = p.matcher(rawCmd);
            if (m.find()) {
                fileName = m.group(1);
            }
            if (defaultFileName.equals("")) {
                EDLineEditor.setFileName(fileName);
            }
        } else {
            if (defaultFileName.equals("")) {
                throw new Wrong("?");
            }
            fileName = defaultFileName;
        }
        return fileName;
    }
}
