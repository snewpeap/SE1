package eDLineEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {
    private TextLinkedList textAsList;

    private final Deque<TextLinkedList> textAsListStack;
    private final Deque<TextLinkedList> constantStack;

    private final ArrayList<String> signsList;
    private boolean isQuitable = false;
    private String beReplaced = null;
    private String replaceBy = null;
    private String replaceMode = null;
    public Editor() {
        textAsListStack = new ArrayDeque<>();
        constantStack = new ArrayDeque<>();
        signsList = new ArrayList<>(26);
    }

    public void syncSignsList() {
        for (Row row : textAsList) {
            for (String sign : row.getSigns()) {
                if (!signsList.contains(sign)) {
                    signsList.add(sign);
                }
            }
        }
    }

    private void pushAndState(TextLinkedList tll) {
        TextLinkedList tllCopy = (TextLinkedList) tll.clone();
        textAsListStack.push(tllCopy);
        constantStack.push(tllCopy);
        isQuitable = false;
    }

    /**
     * 实现ed指令
     * @param fileName 文件名，为""则不带文件进入
     * @throws IOException IOException
     */
    public void ed(String fileName) throws IOException {
        textAsList = new TextLinkedList();
        EDLineEditor.setFileName(fileName);
        if (!fileName.equals("")) {
            try (Scanner sc = new Scanner(new FileInputStream(fileName))) {
                while (sc.hasNextLine()) {
                    textAsList.add(new Row(sc.nextLine()));
                }
                if (textAsList.size() != 0) {
                    textAsList.setIndexRow(textAsList.getLast());
                }
                pushAndState(textAsList);
                isQuitable = true;
            }
        }
    }

    /**
     * 实现a指令与i指令，a指令isAddAfter=true，i指令isAddAfter=false
     * @param row        加入文本基准行-1
     * @param isAddAfter 是否添加到基准行后
     * @throws IOException IOException
     */
    public void add(int row, boolean isAddAfter) throws IOException {
        textAsList.clearIndexRow();
        LinkedList<Row> addList = new LinkedList<>();
        String s;
        while ((s = EDLineEditor.br.readLine()) != null) {
            if (s.equals(".")) {
                textAsList.addRows(isAddAfter ? row + 1 : row == -1 ? 0 : row, addList);
                if (addList.size() != 0) {
                    textAsList.setIndexRow(addList.getLast());
                }
                pushAndState(textAsList);
                break;
            }
            addList.add(new Row(s));
        }
    }

    /**
     * 实现c指令，改变文本
     * @param fromIndex 开始行数-1
     * @param toIndex   结束行数-1
     * @throws Exception IOException
     */
    public void change(int fromIndex, int toIndex) throws Exception {
        if (fromIndex < 0) throw new Wrong("?");
        textAsList.clearIndexRow();
        LinkedList<Row> changeList = new LinkedList<>();
        String s;
        while ((s = EDLineEditor.br.readLine()) != null) {
            if (s.equals(".")) {
                textAsList.addRows(fromIndex, changeList);
                textAsList.deleteRows(fromIndex + changeList.size(), toIndex + changeList.size());
                if (changeList.size() != 0) {
                    textAsList.setIndexRow(changeList.getLast());
                }
                pushAndState(textAsList);
                break;
            }
            changeList.add(new Row(s));
        }
    }

    /**
     * 实现d指令，删除文本
     * @param fromIndex 开始行数-1
     * @param toIndex   结束行数-1
     * @throws Wrong 可能情况：开始行数<0
     */
    public void delete(int fromIndex, int toIndex) throws Wrong {
        if (isQuitable) pushAndState(textAsList);
        if (fromIndex < 0) throw new Wrong("?");
        textAsList.clearIndexRow();
        Row indexRow = textAsList.get(toIndex == textAsList.size() - 1 ? fromIndex - 1 : toIndex + 1);
        textAsList.deleteRows(fromIndex, toIndex);
        textAsList.setIndexRow(indexRow);
        pushAndState(textAsList);
    }

    /**
     * 实现p指令，打印文本
     * @param fromIndex 开始行数-1
     * @param toIndex   结束行数-1
     * @throws Wrong 可能情况：开始行数<0
     */
    public void print(int fromIndex, int toIndex) throws Wrong {
        if (fromIndex < 0) throw new Wrong("?");
        for (int row = fromIndex; row <= toIndex; row++) {
            System.out.println(textAsList.getRowContent(row));
        }
        textAsList.setIndexRow(textAsList.get(toIndex));
    }

    /**
     * 实现=指令，按位移打印文本
     * @param start  开始行数-1
     * @param offset 位移
     * @throws Wrong 可能情况：开始行数<0
     */
    public void offSetPrint(int start, int offset) throws Wrong {
        int total = textAsList.getTotal();
        int fromIndex;int toIndex;
        if (offset < 0 || start + offset > total) {
            fromIndex = start;
            toIndex = total - 1;
        } else {
            fromIndex = start;
            toIndex = start + offset;
        }
        print(fromIndex, toIndex);
    }

    /**
     * 实现w和W指令，写文件
     * @param fromIndex 开始行数-1
     * @param toIndex   结束行数-1
     * @param fileName  文件名
     * @param isAppend  是否为追加模式
     * @throws Exception 不确定
     */
    public void writeFile(int fromIndex, int toIndex, String fileName, boolean isAppend) throws Exception {
        if (fromIndex < 0) throw new Wrong("?");
        File file = new File(fileName);
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        StringBuilder textBuilder = new StringBuilder();
        for (int row = fromIndex; row <= toIndex; row++) {
            textBuilder.append(textAsList.getRowContent(row).concat(System.lineSeparator()));
        }
        FileOutputStream fos = new FileOutputStream(file, isAppend);
        fos.write(textBuilder.toString().getBytes(StandardCharsets.UTF_8));
        fos.close();
        textAsListStack.clear();
    }

    /**
     * 实现q和Q指令，退出ed
     * @param isForceQuit 是否强制退出
     * @throws Exception Wrong:抛出错误以请求再确认   IOException:IOException
     */
    public void quit(boolean isForceQuit) throws Exception {
        if (!isForceQuit && !textAsListStack.isEmpty()) {
            if (!isQuitable) {
                isQuitable = true;
                throw new Wrong("?");
            }
        } else {
            EDLineEditor.br.close();
        }
    }

    /**
     * 实现m指令，移动文本到指定位置
     * @param fromIndex   开始行数-1
     * @param toIndex     结束行数-1
     * @param toTargetRow 目标行数-1
     * @throws Wrong 可能情况：开始行数<0;目标行数>=开始行数；目标行数<结束行数
     */
    public void move(int fromIndex, int toIndex, int toTargetRow) throws Wrong {
        if (fromIndex < 0) throw new Wrong("?");
        textAsList.clearIndexRow();
        LinkedList<Row> moveList = new LinkedList<>();
        if (toTargetRow >= fromIndex && toTargetRow < toIndex) {
            throw new Wrong("?");
        }
        for (int row = fromIndex; row <= toIndex; row++) {
            moveList.add(new Row(textAsList.getRowContent(row), textAsList.get(row).getSigns()));
        }
        textAsList.setIndexRow(moveList.getLast());
        textAsList.addRows(toTargetRow + 1, moveList);
        if (toTargetRow < fromIndex) {
            fromIndex += moveList.size();
            toIndex += moveList.size();
        }
        textAsList.deleteRows(fromIndex, toIndex);
        pushAndState(textAsList);
    }

    /**
     * 实现c指令，复制文本到指定位置
     * @param fromIndex   开始行数-1
     * @param toIndex     结束行数-1
     * @param toTargetRow 目标行数-1
     * @throws Wrong 可能情况：开始行数<0
     */
    public void copy(int fromIndex, int toIndex, int toTargetRow) throws Wrong {
        if (fromIndex < 0) throw new Wrong("?");
        textAsList.clearIndexRow();
        LinkedList<Row> copyList = new LinkedList<>();
        for (int row = fromIndex; row <= toIndex; row++) {
            copyList.add(new Row(textAsList.get(row).getText()));
        }
        textAsList.setIndexRow(copyList.getLast());
        textAsList.addRows(toTargetRow + 1, copyList);
        pushAndState(textAsList);
    }

    /**
     * 实现k指令，打标记
     *
     * @param row  行数-1
     * @param sign 标记
     * @throws Wrong 可能情况：不存在此标记
     */
    public void setSign(int row, String sign) throws Wrong {
        Row r = textAsList.get(row);
        if (signsList.contains(sign)) {
            textAsList.get(textAsList.getRowBySign(sign)).removeSign(sign);
        } else {
            signsList.add(sign);
        }
        r.addOneSign(sign);
        textAsList.set(row, r);
    }

    /**
     * 实现u命令，撤销操作
     * @throws Wrong 持久栈为空
     */
    public void undo() throws Wrong {
        if (constantStack.isEmpty()) throw new Wrong("?");
        constantStack.pop();
        textAsList = constantStack.peekFirst();
    }

    /**
     * 实现j指令，合并文本
     *
     * @param fromIndex 开始行数-1
     * @param toIndex   结束行数-1
     * @throws Wrong 可能情况：开始行数<0
     */
    public void combine(int fromIndex, int toIndex) throws Wrong {
        if (fromIndex < 0) throw new Wrong("?");
        if (fromIndex == toIndex) {
            //对同一行不作操作
            return;
        }
        textAsList.clearIndexRow();
        String combineString = "";
        for (int i = fromIndex; i <= toIndex; i++) {
            combineString = combineString.concat(textAsList.getRowContent(i));
        }
        Row combineRow = new Row(combineString);
        textAsList.add(fromIndex, combineRow);
        textAsList.deleteRows(fromIndex + 1, toIndex + 1);
        textAsList.setIndexRow(combineRow);
        pushAndState(textAsList);
    }

    /**
     * 实现s指令，替换字符串。该方法为入口方法，预处理行数等参数
     *
     * @param fromIndex   开始行数-1
     * @param toIndex     结束行数-1
     * @param isTryAgain  是否重做上次的s指令内容
     * @param beReplaced  被替换字符串
     * @param replaceBy   替换字符串
     * @param replaceMode 替换模式，-1为全局替换
     * @throws Wrong 可能情况：开始行数<0；重复上次操作而上次操作未记录
     */
    public void replace(int fromIndex, int toIndex, boolean isTryAgain,
                        String beReplaced, String replaceBy, String replaceMode) throws Wrong {
        if (fromIndex < 0) throw new Wrong("?");
        if (isTryAgain) {
            if (this.beReplaced == null || this.replaceBy == null || this.replaceMode == null) {
                throw new Wrong("?");
            }
            if (realReplace(fromIndex, toIndex, this.beReplaced, this.replaceBy, this.replaceMode)) {
                pushAndState(textAsList);
            }
            return;
        }
        this.beReplaced = beReplaced;
        this.replaceBy = replaceBy;
        this.replaceMode = replaceMode;
        if (realReplace(fromIndex, toIndex, beReplaced, replaceBy, replaceMode)) {
            pushAndState(textAsList);
        }
    }

    /**
     * 实现s指令，替换字符串。该方法为实际操作方法
     *
     * @param fromIndex  开始行数-1
     * @param toIndex    结束行数-1
     * @param beReplaced 被替换字符串
     * @param replaceBy  替换字符串
     * @throws Wrong 可能情况：没有任何替换；最后一个参数错误
     */
    private boolean realReplace(int fromIndex, int toIndex,
                                String beReplaced, String replaceBy, String replaceMode) throws Wrong {
        int time = 1;
        int count = 0;
        if (replaceMode.equals("g")) {
            time = -1;
        } else if (!replaceMode.equals("")) {
            try {
                time = Integer.parseInt(replaceMode);
            } catch (NumberFormatException e) {
                throw new Wrong("?");
            }
        }
        for (int i = fromIndex; i <= toIndex; i++) {
            String text = textAsList.getRowContent(i);
            Matcher m = Pattern.compile(beReplaced).matcher(text);
            StringBuffer sb = new StringBuffer();
            if (time < 0) {
                if (!m.find()) {
                    count++;
                    continue;
                }
                m.reset();
                while (m.find()) {
                    m.appendReplacement(sb, replaceBy);
                }
            } else {
                int j = time;
                while (m.find()) {
                    j--;
                }
                if (j > 0) {
                    count++;
                    continue;
                }
                m.reset();
                for (int k = time - 1; m.find(); k--) {
                    if (k == 0) {
                        m.appendReplacement(sb, replaceBy);
                        break;
                    }
                }
            }
            m.appendTail(sb);
            String afterReplace = sb.toString();
            if (!afterReplace.equals(text)) {
                Row row = new Row(afterReplace);
                textAsList.set(i, row);
                textAsList.setIndexRow(row);
            }
        }
        if (count == toIndex - fromIndex + 1) {
            throw new Wrong("?");
        }
        return true;
    }

    /*
    以下四个方法为Editor类向外暴露TextLinkedList的接口
     */
    public int getRowBySign(String s) throws Wrong {
        return textAsList.getRowBySign(s);
    }

    public String getText() {
        return textAsList.getText();
    }

    public int getIndex() {
        return textAsList.getIndexRow();
    }

    public int getTotal() {
        return textAsList.getTotal();
    }

    /**
     * ************************
     *
     *     缓存文本的内部类
     *
     * ************************
     */
    private static class TextLinkedList extends LinkedList<Row> {
        private Row indexRow;

        public TextLinkedList() {
            indexRow = null;
        }

        public int getIndexRow() {
            return this.indexOf(indexRow);
        }

        public void setIndexRow(Row indexRow) {
            this.indexRow = indexRow;
        }

        public void clearIndexRow() {
            this.indexRow = null;
        }

        public String getText() {
            ArrayList<String> textlist = new ArrayList<>(size());
            for (Row r : this) {
                textlist.add(r.getText());
            }
            return String.join(System.lineSeparator(), textlist);
        }

        public int getTotal() {
            return size();
        }

        public int getRowBySign(String sign) throws Wrong {
            for (Row r : this) {
                if (r.getSigns().contains(sign)) {
                    return indexOf(r);
                }
            }
            throw new Wrong("?");
        }

        public String getRowContent(int index) {
            return get(index).getText();
        }

        public void addRows(int start, Collection<Row> c) {
            if (size() == 0) {
                addAll(c);
            } else {
                addAll(start, c);
            }
        }

        public void deleteRows(int fromIndex, int toIndex) {
            subList(fromIndex, toIndex + 1).clear();
        }
    }
}
