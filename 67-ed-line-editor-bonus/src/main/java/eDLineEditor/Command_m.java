package eDLineEditor;

import java.util.HashMap;

public class Command_m extends Command {
    private String rawCmd;

    public Command_m(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        int target = getArgument(rawCmd, editor);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexs.getOrDefault(Position.FROM, toIndex);
        editor.move(fromIndex, toIndex, target);
    }

}
