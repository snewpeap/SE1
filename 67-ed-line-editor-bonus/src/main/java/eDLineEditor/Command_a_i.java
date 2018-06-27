package eDLineEditor;

public class Command_a_i extends Command {
    private String rawCmd;
    private boolean isAddAfter;

    public Command_a_i(String rawCmd, boolean isAddAfter) {
        this.rawCmd = rawCmd;
        this.isAddAfter = isAddAfter;
    }

    @Override
    public void execute() throws Exception {
        getNoArgument(rawCmd);
        editor.add(getFromAndToIndex(rawCmd, editor).getOrDefault(Position.TO, editor.getIndex()), isAddAfter);
    }

}
