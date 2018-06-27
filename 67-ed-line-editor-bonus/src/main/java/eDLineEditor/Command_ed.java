package eDLineEditor;

public class Command_ed extends Command {
    private String rawCmd;

    public Command_ed(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Exception {
        String fileName = "";
        if (rawCmd.contains(" ")) {
            fileName = rawCmd.split(" ")[1].trim();
        }
        editor.ed(fileName);
    }

}
