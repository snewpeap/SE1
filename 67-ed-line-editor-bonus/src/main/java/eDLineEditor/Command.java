package eDLineEditor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
    protected Editor editor;

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public abstract void execute() throws Exception;

    public void syncSignsList() {
        editor.syncSignsList();
    }

    protected void getNoArgument(String rawCmd) throws Wrong {
        if (rawCmd.contains("\n") && rawCmd.split("\n").length != 1) {
            throw new Wrong("?");
        }
    }

    protected int getArgument(String rawCmd, Editor editor) throws Wrong {
        if (rawCmd.contains("\n")) {
            if (rawCmd.lastIndexOf('\n') == rawCmd.length() - 1) {
                rawCmd = ".";
            } else {
                rawCmd = rawCmd.split("\n")[1];
            }
        } else {
            Matcher m = Pattern.compile("^[mt](.*)").matcher(rawCmd);
            if (m.find()) {
                rawCmd = m.group(1);
            }
        }
        Matcher m = Pattern.compile("((/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?([+\\-][0-9]+)?)").matcher(rawCmd);
        if (m.find()) {
            return getSingleAddress(m.group(1), editor);
        }
        return getSingleAddress(".", editor);
    }

    /*
    获取操作对象行的Map
     */
    protected HashMap<Position, Integer> getFromAndToIndex(String rawCmd, Editor editor) throws Wrong {
        HashMap<Position, Integer> indexes = new HashMap<>(2);
        //未经过前面地址处理的指令不会带有\n
        if (!rawCmd.contains("\n")) {
            return indexes;
        }
        int index = editor.getIndex();
        int total = editor.getTotal();
        Pattern p1 = Pattern.compile("^,?((/[^/]+/|\\?[^?]+\\?|[.$,;]|[0-9]+|'[a-z])?([+\\-][0-9]+)?)\n.*");
        Pattern p2 = Pattern.compile("((/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?([+\\-][0-9]+)?),((/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?([+\\-][0-9]+)?)\n.*");
        Pattern p3 = Pattern.compile(",\n");
        if (rawCmd.matches(p3.toString())) {
            indexes.put(Position.FROM, 0);
            indexes.put(Position.TO, total - 1);
        } else if (rawCmd.matches(p1.toString())) {
            Matcher m = p1.matcher(rawCmd);
            String s = ".";
            if (m.find()) {
                s = m.group(1);
            }
            int addr = getSingleAddress(s, editor);
            switch (addr) {
                case -1:
                    indexes.put(Position.FROM, 0);
                    indexes.put(Position.TO, total - 1);
                    break;
                case -2:
                    indexes.put(Position.FROM, index);
                    indexes.put(Position.TO, total - 1);
                    break;
                default:
                    indexes.put(Position.TO, addr);
            }
        } else if (rawCmd.matches(p2.toString())) {
            Matcher m = p2.matcher(rawCmd);
            int fromIndex = 0;
            int toIndex = 0;
            if (m.find()) {
                fromIndex = getSingleAddress(m.group(1), editor);
                toIndex = getSingleAddress(m.group(4), editor);
            }
            if (fromIndex > toIndex) {
                throw new Wrong("?");
            }
            indexes.put(Position.FROM, fromIndex);
            indexes.put(Position.TO, toIndex);
        }
        return indexes;
    }

    /*
    获取单个地址
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected int getSingleAddress(String singleAddress, Editor editor) throws Wrong {
        int index = editor.getIndex();
        int total = editor.getTotal();
        if (singleAddress.matches("/[^/]+/|\\?[^?]+\\?")) {
            String text = editor.getText();
            String str = singleAddress.matches("/[^/]+/") ? singleAddress.split("/")[1] : singleAddress.split("\\?")[1];
            if (text.contains(str)) {
                String[] arr = text.split(System.lineSeparator());
                if (singleAddress.matches("/.+/")) {
                    for (int i = index + 1; i < total; i++) {
                        if (arr[i].contains(str)) {
                            return i;
                        }
                    }
                    for (int i = 0; i < index + 1; i++) {
                        if (arr[i].contains(str)) {
                            return i;
                        }
                    }
                } else {
                    for (int i = index - 1; i >= 0; i--) {
                        if (arr[i].contains(str)) {
                            return i;
                        }
                    }
                    for (int i = total - 1; i >= index; i--) {
                        if (arr[i].contains(str)) {
                            return i;
                        }
                    }
                }
            }
        } else if (Pattern.compile("/.*[+\\-]+.*/-|\\?.*[+\\-]+.*\\?-|-/.*[+\\-]+.*/|-\\?.*[\\-]+.*\\?").matcher(singleAddress).find()) {
            String[] arr = new String[2];
            if (Pattern.compile("/.*[+\\-]+.*/-|\\?.*[+\\-]+.*\\?-").matcher(singleAddress).find()) {
                Matcher m = Pattern.compile("/.*[+\\-]+.*/-|\\?.*[+\\-]+.*\\?-").matcher(singleAddress);
                m.find();
                arr[0] = singleAddress.substring(0, m.end() - 1);
                arr[1] = singleAddress.substring(m.end());
            } else {
                Matcher m = Pattern.compile("-/.*[+\\-]+.*/|-\\?.*[\\-]+.*\\?").matcher(singleAddress);
                m.find();
                arr[0] = singleAddress.substring(0, m.start());
                arr[1] = singleAddress.substring(m.start() + 1);
            }
            int target = getSingleAddress(arr[0], editor) - Integer.parseInt(arr[1]);
            if (target >= 0) {
                return target;
            }
        } else if (Pattern.compile("/.*[+\\-]+.*/\\+|\\?.*[+\\-]+.*\\?\\+|\\+/.*[+\\-]+.*/|\\+\\?.*[\\-]+.*\\?").matcher(singleAddress).find()) {
            String[] arr = new String[2];
            if (Pattern.compile("/.*[+\\-]+.*/\\+|\\?.*[+\\-]+.*\\?\\+").matcher(singleAddress).find()) {
                Matcher m = Pattern.compile("/.*[+\\-]+.*/\\+|\\?.*[+\\-]+.*\\?\\+").matcher(singleAddress);
                m.find();
                arr[0] = singleAddress.substring(0, m.end() - 1);
                arr[1] = singleAddress.substring(m.end());
            } else {
                Matcher m = Pattern.compile("\\+/.*[+\\-]+.*/|\\+\\?.*[\\-]+.*\\?").matcher(singleAddress);
                m.find();
                arr[0] = singleAddress.substring(0, m.start());
                arr[1] = singleAddress.substring(m.start() + 1);
            }
            int target = getSingleAddress(arr[0], editor) + Integer.parseInt(arr[1]);
            if (target < total) {
                return target;
            }
        } else if (singleAddress.contains("-")) {
            String[] arr = singleAddress.split("-");
            int target = getSingleAddress(arr[0], editor) - Integer.parseInt(arr[1]);
            if (target >= 0) {
                return target;
            }
        } else if (singleAddress.contains("+")) {
            String[] arr = singleAddress.split("\\+");
            int target = getSingleAddress(arr[0], editor) + Integer.parseInt(arr[1]);
            if (target < total) {
                return target;
            }
        } else if (singleAddress.matches("'[a-z]")) {
            return editor.getRowBySign(singleAddress);
        } else if (singleAddress.equals("$")) {
            return total - 1;
        } else if (singleAddress.equals(".") || singleAddress.equals("")) {
            return index;
        } else if (singleAddress.equals(",")) {
            return -1;
        } else if (singleAddress.equals(";")) {
            return -2;
        } else if (singleAddress.matches("[0-9]+")) {
            int row = Integer.parseInt(singleAddress) - 1;
            if (row < total) {
                return row;
            }
        }
        throw new Wrong("?");
    }

    public enum Position {
        FROM, TO
    }
}