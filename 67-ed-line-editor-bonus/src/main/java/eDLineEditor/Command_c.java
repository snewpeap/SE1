package eDLineEditor;

import java.util.HashMap;

public class Command_c extends Command {
    private String rawCmd;

    public Command_c(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Exception {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexs.getOrDefault(Position.FROM, toIndex);
        editor.change(fromIndex, toIndex);
    }
}
