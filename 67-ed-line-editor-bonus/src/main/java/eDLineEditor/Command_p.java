package eDLineEditor;

import java.util.HashMap;

public class Command_p extends Command {
    private final String rawCmd;

    public Command_p(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexes = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexes.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexes.getOrDefault(Position.FROM, toIndex);
        editor.print(fromIndex, toIndex);
    }

}
