package eDLineEditor;

public class Command_file implements Command {
    private String rawCmd;

    public Command_file(String rawCmd) {
        this.rawCmd = rawCmd;
    }

    @Override
    public void setEditor(Editor editor) {
    }

    @Override
    public void execute() throws Exception {
        String fileName = "";
        if (rawCmd.contains(" ")){
            fileName = rawCmd.split(" ")[1];
        }
        if (fileName.equals("")) {
            if (EDLineEditor.getFileName().equals("")) {
                throw new Exception("?");
            }
            System.out.println(EDLineEditor.getFileName());
        }else {
            EDLineEditor.setFileName(fileName);
        }
    }
}
