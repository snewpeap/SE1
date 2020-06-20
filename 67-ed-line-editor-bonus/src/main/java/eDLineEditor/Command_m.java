package eDLineEditor;

import java.util.HashMap;

public class Command_m extends Command {
    private final String rawCmd;

    public Command_m(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        int target = getArgument(rawCmd, editor);
        HashMap<Position, Integer> indexes = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexes.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexes.getOrDefault(Position.FROM, toIndex);
        editor.move(fromIndex, toIndex, target);
    }

}
