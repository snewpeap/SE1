package eDLineEditor;

public class Command_u extends Command {
    @Override
    public void execute() throws Wrong {
        editor.undo();
    }
}
