package eDLineEditor;

public class Command_ed implements Command {
    private Editor editor;
    private String rawCmd;

    public Command_ed(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
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
