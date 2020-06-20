package eDLineEditor;

import java.util.HashMap;

public class Command_t extends Command {
    private final String rawCmd;

    public Command_t(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        int target = getArgument(rawCmd, editor);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexs.getOrDefault(Position.FROM, toIndex);
        editor.copy(fromIndex, toIndex, target);
    }
}
