package eDLineEditor;

public class Command_undo implements Command {
    private Editor editor;

    public Command_undo() {
    }

    @Override
    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void execute() throws Exception {
        editor.undo();
    }

}
