package eDLineEditor;

import java.util.HashMap;

public class Command_d extends Command {
    private String rawCmd;

    public Command_d(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexs.getOrDefault(Position.FROM, toIndex);
        editor.delete(fromIndex, toIndex);
    }
}
