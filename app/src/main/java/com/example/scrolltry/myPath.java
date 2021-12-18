package com.example.scrolltry;

import java.util.ArrayList;

public class myPath {
    /*
    *
    *  存储答案：可以get文字说明，也可以作图，作图的话再回到数据库找
    *
    * */

    private ArrayList<Integer> rx = new ArrayList<Integer>();
    private ArrayList<Integer> ry = new ArrayList<Integer>();

    private String ans = null;

    public void addRx(int a){
        this.rx.add(a);
    }
    public void addRy(int a){
        this.ry.add(a);
    }

    public ArrayList<Integer> getRx(){
        return this.rx;
    }
    public ArrayList<Integer> getRy(){
        return this.ry;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
