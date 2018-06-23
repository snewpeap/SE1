package eDLineEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor {
    private TextLinkedList textAsList;

    private Deque<TextLinkedList> textAsListStack;
    private Deque<TextLinkedList> constantStack;

    private ArrayList<String> signsList;
    public void syncSignsList(){
        for (Row row:textAsList){
            for (String sign:row.getSigns()){
                if (!signsList.contains(sign)){
                    signsList.add(sign);
                }
            }
        }
    }

    private void pushAndState(TextLinkedList tll){
        TextLinkedList tllCopy = (TextLinkedList)tll.clone();
        textAsListStack.push(tllCopy);
        constantStack.push(tllCopy);
        isQuitable = false;
    }

    private boolean isQuitable = false;
    private String beReplaced =null;
    private String replaceBy = null;
    private String replaceMode = null;

    public Editor() {
        textAsListStack = new ArrayDeque<>();
        constantStack = new ArrayDeque<>();
        signsList = new ArrayList<>(26);
    }

    /**
     * 实现ed指令
     * @param fileName 文件名，为""则不带文件进入
     */
    public void ed(String fileName){
        textAsList = new TextLinkedList();
        EDLineEditor.setFileName(fileName);
        if (!fileName.equals("")){
            try (Scanner sc = new Scanner(new FileInputStream(fileName))) {
                while (sc.hasNextLine()) {
                    textAsList.add(new Row(sc.nextLine()));
                }
                textAsList.setIndexRow(textAsList.getLast());
                pushAndState(textAsList);
                isQuitable = true;
            } catch (IOException ignored) {}
        }
    }

    /**
     * 实现a指令与i指令，a指令isAddAfter=true，i指令isAddAfter=false
     * @param row 加入文本基准行-1
     * @param isAddAfter 是否添加到基准行后
     * @throws IOException 将被忽略
     */
    public void add(int row,boolean isAddAfter) throws IOException {
        textAsList.clearIndexRow();
        LinkedList<Row> addList = new LinkedList<>();
        String s;
        while ((s = EDLineEditor.br.readLine()) != null){
            if (s.equals(".")){
                textAsList.addRows(isAddAfter?row+1:row==-1?0:row,addList);
                if (addList.size()!=0) {
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
     * @param toIndex 结束行数-1
     * @throws IOException 将被忽略
     */
    public void change(int fromIndex,int toIndex) throws Exception {
        if (fromIndex < 0) throw new Exception("?");
        textAsList.clearIndexRow();
        LinkedList<Row> changeList = new LinkedList<>();
        String s;
        while ((s = EDLineEditor.br.readLine()) != null) {
            if (s.equals(".")) {
                textAsList.addRows(fromIndex,changeList);
                textAsList.deleteRows(fromIndex+changeList.size(),toIndex+changeList.size());
                if (changeList.size()!=0){
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
     * @param toIndex 结束行数-1
     */
    public void delete(int fromIndex,int toIndex) throws Exception {
        if (isQuitable) pushAndState(textAsList);
        if (fromIndex < 0) throw new Exception("?");
        textAsList.clearIndexRow();
        Row indexRow = textAsList.get(toIndex == textAsList.size()-1?fromIndex-1:toIndex+1);
        textAsList.deleteRows(fromIndex,toIndex);
        textAsList.setIndexRow(indexRow);
        pushAndState(textAsList);
    }

    /**
     * 实现p指令，打印文本
     * @param fromIndex 开始行数-1
     * @param toIndex 结束行数-1
     */
    public void print(int fromIndex,int toIndex) throws Exception {
        if (fromIndex < 0) throw new Exception("?");
        for (int row=fromIndex;row<=toIndex;row++){
            System.out.println(textAsList.getRowContent(row));
        }
        textAsList.setIndexRow(textAsList.get(toIndex));
    }

    /**
     * 实现=指令，按位移打印文本
     * @param start 开始行数-1
     * @param offset 位移
     */
    public void offSetPrint(int start,int offset) throws Exception {
        int total = textAsList.getTotal();
        int fromIndex;int toIndex;
        if (offset < 0||start+offset>total){
            fromIndex = start;toIndex = total-1;
        }else {
            fromIndex = start;toIndex = start+offset;
        }
        print(fromIndex,toIndex);
    }

    /**
     * 实现w和W指令，写文件
     * @param fromIndex 开始行数-1
     * @param toIndex 结束行数-1
     * @param fileName 文件名
     * @param isAppend 是否为追加模式
     * @throws Exception 不确定
     */
    public void writeFile(int fromIndex,int toIndex,String fileName,boolean isAppend) throws Exception{
        if (fromIndex < 0) throw new Exception("?");
        File file = new File(fileName);file.createNewFile();
        StringBuilder textBuilder = new StringBuilder();
        for (int row = fromIndex;row<=toIndex;row++){
            textBuilder.append(textAsList.getRowContent(row).concat(System.lineSeparator()));
        }
        try {
            FileOutputStream fos = new FileOutputStream(file,isAppend);
            fos.write(textBuilder.toString().getBytes("utf-8"));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        textAsListStack.clear();
    }

    /**
     * 实现q和Q指令，退出ed
     * @param isForceQuit 是否强制退出
     * @throws Exception 抛出错误以请求再确认
     */
    public void quit(boolean isForceQuit) throws Exception {
        if (!isForceQuit&&!textAsListStack.isEmpty()){
            if (!isQuitable) {
                isQuitable = true;
                throw new Exception("?");
            }
        }else {
            EDLineEditor.br.close();
        }
    }

    /**
     * 实现m指令，移动文本到指定位置
     * @param fromIndex 开始行数-1
     * @param toIndex 结束行数-1
     * @param toTargetRow 目标行数-1
     * @throws Exception 行数有问题
     */
    public void move(int fromIndex,int toIndex,int toTargetRow) throws Exception {
        if (fromIndex < 0) throw new Exception("?");
        textAsList.clearIndexRow();
        LinkedList<Row> moveList = new LinkedList<>();
        if (toTargetRow>=fromIndex&&toTargetRow<toIndex){
            throw new Exception("?");
        }
        for (int row=fromIndex;row<=toIndex;row++){
            moveList.add(new Row(textAsList.getRowContent(row),textAsList.get(row).getSigns()));
        }
        textAsList.setIndexRow(moveList.getLast());
        textAsList.addRows(toTargetRow+1,moveList);
        if (toTargetRow<fromIndex){
            fromIndex+=moveList.size();
            toIndex+=moveList.size();
        }
        textAsList.deleteRows(fromIndex,toIndex);
        pushAndState(textAsList);
    }

    /**
     * 实现c指令，复制文本到指定位置
     * @param fromIndex 开始行数-1
     * @param toIndex 结束行数-1
     * @param toTargetRow 目标行数-1
     */
    public void copy(int fromIndex,int toIndex,int toTargetRow) throws Exception {
        if (fromIndex < 0) throw new Exception("?");
        textAsList.clearIndexRow();
        LinkedList<Row> copyList = new LinkedList<>();
        for (int row=fromIndex;row<=toIndex;row++){
            copyList.add(new Row(textAsList.get(row).getText()));
        }
        textAsList.setIndexRow(copyList.getLast());
        textAsList.addRows(toTargetRow+1,copyList);
        pushAndState(textAsList);
    }

    /**
     * 实现k指令，打标记
     * @param row 行数-1
     * @param sign 标记
     */
    public void setSign(int row,String sign) throws Exception {
        Row r = textAsList.get(row);
        if (signsList.contains(sign)){
            textAsList.get(textAsList.getRowBySign(sign)).removeSign(sign);
        }else {
            signsList.add(sign);
        }
        r.addOneSign(sign);
        textAsList.set(row,r);
    }

    /**
     * 实现u命令，撤销操作
     */
    public void undo() throws Exception {
        if (constantStack.isEmpty()) throw new Exception("?");
        constantStack.pop();
        textAsList = constantStack.peekFirst();
    }

    /**
     * 实现j指令，合并文本
     * @param fromIndex 开始行数-1
     * @param toIndex 结束行数-1
     */
    public void combine(int fromIndex,int toIndex) throws Exception {
        if (fromIndex < 0) throw new Exception("?");
        if (fromIndex == toIndex){
            return;
        }
        textAsList.clearIndexRow();
        String combineString = "";
        for (int i=fromIndex;i<=toIndex;i++){
            combineString = combineString.concat(textAsList.getRowContent(i));
        }
        Row combineRow = new Row(combineString);
        textAsList.add(fromIndex,combineRow);
        textAsList.deleteRows(fromIndex+1,toIndex+1);
        textAsList.setIndexRow(combineRow);
        pushAndState(textAsList);
    }

    public void replace(int fromIndex, int toIndex, boolean isTryAgain,
                        String beReplaced, String replaceBy, String replaceMode) throws Exception {
        if (fromIndex < 0) throw new Exception("?");
        if (isTryAgain){
            if (this.beReplaced==null||this.replaceBy==null||this.replaceMode ==null){
                throw new Exception("?");
            }
            realReplace(fromIndex, toIndex, this.beReplaced, this.replaceBy, this.replaceMode);
            return;
        }
        this.beReplaced = beReplaced;this.replaceBy = replaceBy;this.replaceMode = replaceMode;
        if (realReplace(fromIndex, toIndex, beReplaced, replaceBy, replaceMode)){
            pushAndState(textAsList);
        }
    }

    private boolean realReplace(int fromIndex, int toIndex,
                                String beReplaced, String replaceBy, String replaceMode) throws Exception {
        int time = 1;int count = 0;
        if (replaceMode.equals("g")){
            time = -1;
        } else if (!replaceMode.equals("")){
            try {
                time = Integer.parseInt(replaceMode);
            }catch (Exception e){
                throw new Exception("?");
            }
        }
        for (int i=fromIndex;i<=toIndex;i++){
            String text = textAsList.getRowContent(i);
            Matcher m = Pattern.compile(beReplaced).matcher(text);
            StringBuffer sb = new StringBuffer();
            if (time<0){
                if (!m.find()){
                    count++;continue;
                }
                m.reset();
                while (m.find()){
                    m.appendReplacement(sb,replaceBy);
                }
            }else {
                int j = time;
                while (m.find()){
                    j--;
                }
                if (j > 0){
                    count++;continue;
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
            if (!afterReplace.equals(text)){
                Row row = new Row(afterReplace);
                textAsList.set(i,row);
                textAsList.setIndexRow(row);
            }
        }
        if (count==toIndex-fromIndex+1){
            throw new Exception("?");
        }
        return true;
    }

    public int getRowBySign(String s) throws Exception {
        return textAsList.getRowBySign(s);
    }

    public String getText(){
        return textAsList.getText();
    }

    public int getIndex(){
        return textAsList.getIndexRow();
    }

    public int getTotal(){
        return textAsList.getTotal();
    }

    /**
     *************************
     *
     *      缓存的内部类
     *
     *************************
     */
    private class TextLinkedList extends LinkedList<Row>{
        private Row indexRow;
        public int getIndexRow(){
            return this.indexOf(indexRow);
        }
        public void setIndexRow(Row indexRow){
            this.indexRow = indexRow;
        }
        public void clearIndexRow(){
            this.indexRow = null;
        }

        public String getText(){
            ArrayList<String> textlist = new ArrayList<>(size());
            for (Row r:this){
                textlist.add(r.getText());
            }
            return String.join(System.lineSeparator(),textlist);
        }

        public int getTotal(){
            return size();
        }

        public int getRowBySign(String sign) throws Exception {
            for (Row r:this){
                if (r.getSigns().contains(sign)){
                    return indexOf(r);
                }
            }
            throw new Exception("?");
        }

        public String getRowContent(int index){
            return get(index).getText();
        }

        public boolean addRows(int start, Collection<Row> c){
            return size() == 0?addAll(c):addAll(start,c);
        }

        public void deleteRows(int fromIndex,int toIndex){
            subList(fromIndex,toIndex+1).clear();
        }
    }
}
