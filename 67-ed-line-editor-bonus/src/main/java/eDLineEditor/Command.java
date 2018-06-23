package eDLineEditor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Command {
    String FROM = "fromIndex";String TO = "toIndex";

    void setEditor(Editor editor);
    void execute() throws Exception;

    default void getNoArgument(String rawCmd) throws Exception {
        if (rawCmd.contains("\n")&&rawCmd.split("\n").length != 1){
                throw new Exception("?");
        }
    }

    default int getArgument(String rawCmd, Editor editor) throws Exception {
        if (rawCmd.contains("\n")){
            if (rawCmd.lastIndexOf('\n') == rawCmd.length()-1){
                rawCmd = ".";
            }else {
                rawCmd = rawCmd.split("\n")[1];
            }
        }else {
            Matcher m = Pattern.compile("^[mt](.*)").matcher(rawCmd);
            if (m.find()) {
                rawCmd = m.group(1);
            }
        }
        Matcher m = Pattern.compile("((/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?([+\\-][0-9]+)?)").matcher(rawCmd);
        if (m.find()){
            return get(m.group(1),editor);
        }
        return get(".",editor);
    }

    /*
    获取操作对象行的Map
     */
    default HashMap<String, Integer> getFromAndToIndex(String rawCmd, Editor editor) throws Exception {
        HashMap<String,Integer> indexs = new HashMap<>(2);
        //未经过前面地址处理的指令不会带有\n
        if (!rawCmd.contains("\n")){
            return indexs;
        }
        int index = editor.getIndex();int total = editor.getTotal();
        Pattern p1 = Pattern.compile("^,?((/[^/]+/|\\?[^?]+\\?|[.$,;]|[0-9]+|'[a-z])?([+\\-][0-9]+)?)\n.*");
        Pattern p2 = Pattern.compile("((/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?([+\\-][0-9]+)?),((/[^/]+/|\\?[^?]+\\?|[.$]|[0-9]+|'[a-z])?([+\\-][0-9]+)?)\n.*");
        Pattern p3 = Pattern.compile(",\n");
        if (rawCmd.matches(p3.toString())){
            indexs.put(FROM,0); indexs.put(TO,total-1);
        }else if (rawCmd.matches(p1.toString())){
            Matcher m = p1.matcher(rawCmd);
            String s = ".";
            if (m.find()) {
                s = m.group(1);
            }
            int addr = get(s, editor);
            switch (addr){
                case -1:
                    indexs.put(FROM,0);indexs.put(TO,total-1);
                    break;
                case -2:
                    indexs.put(FROM,index);indexs.put(TO,total-1);
                    break;
                default:
                    indexs.put(TO,addr);
            }
        }else if (rawCmd.matches(p2.toString())){
            Matcher m = p2.matcher(rawCmd);
            int fromIndex = 0;int toIndex = 0;
            if (m.find()) {
                fromIndex = get(m.group(1), editor);
                toIndex = get(m.group(4), editor);
            }
            if (fromIndex > toIndex){
                throw new Exception("?");
            }
            indexs.put(FROM,fromIndex);indexs.put(TO,toIndex);
        }
        return indexs;
    }

    /*
    获取单个地址
     */
    default int get(String singleAddr, Editor editor) throws Exception {
        int index = editor.getIndex();int total = editor.getTotal();
        if (singleAddr.matches("/[^/]+/|\\?[^?]+\\?")){
            String text = editor.getText();
            String str = singleAddr.matches("/[^/]+/")?singleAddr.split("/")[1]:singleAddr.split("\\?")[1];
            if (text.contains(str)) {
                String[] arr = text.split(System.lineSeparator());
                if (singleAddr.matches("/.+/")) {
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
        }else if (Pattern.compile("/.*[+\\-]+.*/-|\\?.*[+\\-]+.*\\?-|-/.*[+\\-]+.*/|-\\?.*[\\-]+.*\\?").matcher(singleAddr).find()){
            String[] arr = new String[2];
            if (Pattern.compile("/.*[+\\-]+.*/-|\\?.*[+\\-]+.*\\?-").matcher(singleAddr).find()){
                Matcher m = Pattern.compile("/.*[+\\-]+.*/-|\\?.*[+\\-]+.*\\?-").matcher(singleAddr);m.find();
                arr[0] = singleAddr.substring(0,m.end()-1);arr[1] = singleAddr.substring(m.end());
            }else{
                Matcher m = Pattern.compile("-/.*[+\\-]+.*/|-\\?.*[\\-]+.*\\?").matcher(singleAddr);m.find();
                arr[0] = singleAddr.substring(0,m.start());arr[1] = singleAddr.substring(m.start()+1);
            }
            int target = get(arr[0], editor) - Integer.parseInt(arr[1]);
            if (target >= 0){
                return target;
            }
        }else if (Pattern.compile("/.*[+\\-]+.*/\\+|\\?.*[+\\-]+.*\\?\\+|\\+/.*[+\\-]+.*/|\\+\\?.*[\\-]+.*\\?").matcher(singleAddr).find()){
            String[] arr = new String[2];
            if (Pattern.compile("/.*[+\\-]+.*/\\+|\\?.*[+\\-]+.*\\?\\+").matcher(singleAddr).find()){
                Matcher m = Pattern.compile("/.*[+\\-]+.*/\\+|\\?.*[+\\-]+.*\\?\\+").matcher(singleAddr);m.find();
                arr[0] = singleAddr.substring(0,m.end()-1);arr[1] = singleAddr.substring(m.end());
            }else{
                Matcher m = Pattern.compile("\\+/.*[+\\-]+.*/|\\+\\?.*[\\-]+.*\\?").matcher(singleAddr);m.find();
                arr[0] = singleAddr.substring(0,m.start());arr[1] = singleAddr.substring(m.start()+1);
            }
            int target = get(arr[0], editor) + Integer.parseInt(arr[1]);
            if (target < total){
                return target;
            }
        }else if (singleAddr.contains("-")){
            String[] arr = singleAddr.split("-");
            int target = get(arr[0], editor) - Integer.parseInt(arr[1]);
            if (target >= 0){
                return target;
            }
        }else if (singleAddr.contains("+")){
            String[] arr = singleAddr.split("\\+");
            int target = get(arr[0], editor) + Integer.parseInt(arr[1]);
            if (target < total){
                return target;
            }
        }else if (singleAddr.matches("'[a-z]")){
            return editor.getRowBySign(singleAddr);
        }else if (singleAddr.equals("$")){
            return total-1;
        }else if (singleAddr.equals(".")||singleAddr.equals("")){
            return index;
        }else if (singleAddr.equals(",")){
            return -1;
        }else if (singleAddr.equals(";")){
            return -2;
        }else if (singleAddr.matches("[0-9]+")){
            int row = Integer.parseInt(singleAddr)-1;
            if (row < total) {
                return row;
            }
        }
        throw new Exception("?");
    }
}
