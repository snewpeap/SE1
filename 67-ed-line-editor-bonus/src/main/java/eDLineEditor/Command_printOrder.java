package eDLineEditor;

public class Command_printOrder extends Command {
    private final String rawCmd;

    public Command_printOrder(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        getNoArgument(rawCmd);
        System.out.println(getFromAndToIndex(rawCmd, editor).getOrDefault(Position.TO, editor.getIndex()) + 1);
    }
}
