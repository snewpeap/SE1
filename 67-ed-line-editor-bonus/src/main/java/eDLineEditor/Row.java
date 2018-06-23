package eDLineEditor;

import java.util.ArrayList;

public class Row {
    private String text;
    public String getText() {
        return text;
    }

    private ArrayList<String> signs;
    public void setSigns(ArrayList<String> signs) {
        this.signs = signs;
    }
    public ArrayList<String> getSigns() {
        return signs;
    }
    public void addOneSign(String sign){
        signs.add(sign);
    }

    public Row(String text) {
        this.text = text;
        signs = new ArrayList<>(26);
    }

    public Row(String text, ArrayList<String> signs) {
        this.text = text;
        this.signs = signs;
    }
}
