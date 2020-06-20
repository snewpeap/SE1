package eDLineEditor;

import java.util.HashMap;

public class Command_j extends Command {
    private final String rawCmd;

    public Command_j(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexes = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexes.getOrDefault(Position.TO, editor.getIndex() + 1);
        if (toIndex == editor.getTotal()) {
            throw new Wrong("?");
        }
        int fromIndex = indexes.getOrDefault(Position.FROM, editor.getIndex());
        editor.combine(fromIndex, toIndex);
    }
}
