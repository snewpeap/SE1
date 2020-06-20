package eDLineEditor;

public class Command_q_Q extends Command {
    private final boolean isForceQuit;

    public Command_q_Q(boolean isForceQuit) {
        this.isForceQuit = isForceQuit;
    }

    @Override
    public void execute() throws Exception {
        editor.quit(isForceQuit);
    }

}
