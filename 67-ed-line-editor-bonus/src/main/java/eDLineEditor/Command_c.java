package eDLineEditor;

import java.util.HashMap;

public class Command_c extends Command {
    private final String rawCmd;

    public Command_c(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Exception {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexes = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexes.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexes.getOrDefault(Position.FROM, toIndex);
        editor.change(fromIndex, toIndex);
    }
}
