package eDLineEditor;

public class Command_quit implements Command {
    private Editor editor;
    private boolean isForceQuit;

    public Command_quit(boolean isForceQuit) {
        this.isForceQuit = isForceQuit;
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        editor.quit(isForceQuit);
    }

}
