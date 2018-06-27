package eDLineEditor;

import java.util.HashMap;

public class Command_p extends Command {
    private String rawCmd;

    public Command_p(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        getNoArgument(rawCmd);
        HashMap<Position, Integer> indexs = getFromAndToIndex(rawCmd, editor);
        int toIndex = indexs.getOrDefault(Position.TO, editor.getIndex());
        int fromIndex = indexs.getOrDefault(Position.FROM, toIndex);
        editor.print(fromIndex, toIndex);
    }

}
