package eDLineEditor;

public class Command_k extends Command {
    private final String rawCmd;

    public Command_k(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        String sign = rawCmd.contains("\n") ? rawCmd.split("\n")[1] : rawCmd;
        if (!sign.matches("[a-z]")) {
            throw new Wrong("?");
        }
        editor.setSign(getFromAndToIndex(rawCmd, editor).getOrDefault(Position.TO, editor.getIndex()), "'" + sign);
    }

}
