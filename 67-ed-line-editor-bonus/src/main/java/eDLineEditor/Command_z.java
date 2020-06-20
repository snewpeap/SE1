package eDLineEditor;

public class Command_z extends Command {
    private final String rawCmd;

    public Command_z(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        int offset = getArgument(rawCmd);
        editor.offSetPrint(getFromAndToIndex(rawCmd, editor).getOrDefault(Position.TO, editor.getIndex() + 1), offset);
    }

    public int getArgument(String rawCmd) throws Wrong {
        if (rawCmd.split("\n").length == 1) {
            return -1;
        }
        String arg = rawCmd.split("\n")[1];
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new Wrong("?");
        }
    }
}
