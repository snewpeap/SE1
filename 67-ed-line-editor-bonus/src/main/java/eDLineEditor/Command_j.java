package eDLineEditor;

import java.util.HashMap;

public class Command_j extends Command {
    private String rawCmd;

    public Command_j(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getIndex() + 1);
        if (toIndex == editor.getTotal()) {
            throw new Wrong("?");
        }
        int fromIndex = indexs.getOrDefault(Position.FROM, editor.getIndex());
        editor.combine(fromIndex, toIndex);
    }
}
