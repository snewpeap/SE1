package eDLineEditor;

public class Command_f extends Command {
    private String rawCmd;

    public Command_f(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void execute() throws Wrong {
        String fileName = "";
        if (rawCmd.contains(" ")) {
            fileName = rawCmd.split(" ")[1];
        }
        if (fileName.equals("")) {
            if (EDLineEditor.getFileName().equals("")) {
                throw new Wrong("?");
            }
            System.out.println(EDLineEditor.getFileName());
        } else {
            EDLineEditor.setFileName(fileName);
        }
    }
}
