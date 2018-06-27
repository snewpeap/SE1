package eDLineEditor;

import java.util.ArrayList;

public class Row {
    private String text;
    private ArrayList<String> signs;

    public Row(String text) {
        this.text = text;
        signs = new ArrayList<>(26);
    }

    public Row(String text, ArrayList<String> signs) {
        this.text = text;
        this.signs = signs;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getSigns() {
        return signs;
    }

    public void addOneSign(String sign) {
        signs.add(sign);
    }

    public void removeSign(String sign) {
        signs.remove(sign);
    }
}
