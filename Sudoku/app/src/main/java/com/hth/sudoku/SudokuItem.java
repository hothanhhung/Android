package com.hth.sudoku;

/**
 * Created by Lenovo on 6/7/2016.
 */
public class SudokuItem {
    private String originalMap;
    private int difficulty;
    private String startAt;
    private String endAt;
    private String resolvedMap;
    private String totalTime;
    private int changes;
    private String name;
    private String comment;
    private String orderViaEndAt;

    public SudokuItem(){

    }

    public SudokuItem(String originalMap, int difficulty){
        this.originalMap = originalMap;
        this.difficulty = difficulty;
    }

    public String getOriginalMap() {
        return originalMap;
    }

    public void setOriginalMap(String originalMap) {
        this.originalMap = originalMap;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getResolvedMap() {
        return resolvedMap;
    }

    public void setResolvedMap(String resolvedMap) {
        this.resolvedMap = resolvedMap;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOrderViaEndAt() {
        return orderViaEndAt;
    }

    public void setOrderViaEndAt(String orderViaEndAt) {
        this.orderViaEndAt = orderViaEndAt;
    }

    public Item[] getResolvedItem() {
        Item[] puz = new Item[81];
        if(resolvedMap.length() == 81 && originalMap.length() == 81) {
            for (int i = 0; i < puz.length; i++) {
                puz[i] = new Item(resolvedMap.charAt(i) - '0', originalMap.charAt(i) == '0');
            }
        }else{
            for (int i = 0; i < puz.length; i++) {
                puz[i] = new Item(0);
            }
        }
        return puz;
    }
}
