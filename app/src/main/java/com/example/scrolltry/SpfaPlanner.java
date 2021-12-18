package com.example.scrolltry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SpfaPlanner extends AppCompatActivity {
    /*
     *  要实现在抽象大图中的搜索，返回总体路径； 可以单独拿出来调试
     *   构造时直接从数据库建图，使用链式前向星存储
     *   每次询问，输入两个点的id，找出最短路径 返回路径长度和路径坐标集
     * */
    private static int M = 400005, N = 50005;
    private static boolean[] book = new boolean[N]; //标记是否在队列中
    private static int[] head = new int[M];
    private static int[] e = new int[M], nex = new int[M], w = new int[M], u = new int[M];
    private final int INF = 99999999;
    private int[] dis = new int[N];
    private static int idx = 0;
    private static SQLiteDatabase db;
    static DbManager dbm;
    int[] pre = new int[N];
    Context context;


    public SpfaPlanner(Context context) {
        this.context = context;
    }

    @SuppressLint("Range")
    public void init() {
        //建图与初始化
        //初始化head
        Arrays.fill(head, -1);

        //连接数据库
        dbm = new DbManager(context);
        dbm.openDataBase();
        db = dbm.getDb();
//        dbm.closeDataBase();
//        Log.e("TAG", "SpfaPlanner: 读取数据完成");
        //挨个读数据库的边表，做成邻接表
        String sql_yuju = "select * from edge_table";
        Cursor cursor = db.rawQuery(sql_yuju, new String[]{});
        cursor.moveToFirst();
        int a, b, c;

        while (!cursor.isAfterLast()) {
            a = cursor.getInt(cursor.getColumnIndex("u"));
            b = cursor.getInt(cursor.getColumnIndex("v"));
            c = cursor.getInt(cursor.getColumnIndex("dis"));
            add(a, b, c);
            Log.e("DB", "add:" + a + " to " + b);
            cursor.moveToNext();
        }
        Toast.makeText(context, "加边完成，加了" + idx + "条边", Toast.LENGTH_SHORT).show();
    }

    private void add(int s, int t, int distance) {
        u[idx] = s;
        e[idx] = t;
        w[idx] = distance;
        nex[idx] = head[s];
        head[s] = idx++;
    }

    @SuppressLint("Range")
    public int trans_point_id_from_name(String name) {

        String chaxun = "select id from point_table where name=?";
        Cursor cs = db.rawQuery(chaxun, new String[]{name});
        cs.moveToFirst();
        Toast.makeText(context, "找到" + name, Toast.LENGTH_SHORT).show();
        return cs.getInt(cs.getColumnIndex("id"));
    }

    @SuppressLint("Range")
    public String trans_point_name_from_id(int id) {

        String chaxun = "select name from point_table where id=?";
        Cursor cs = db.rawQuery(chaxun, new String[]{id + ""});
        cs.moveToFirst();
//        Toast.makeText(context, "找到"+id, Toast.LENGTH_SHORT).show();
        return cs.getString(cs.getColumnIndex("name"));
    }

    public myPath find_opt_path(String S, String T) {
        //找到最优路径
        //从数据库拿到名称对应id
        int sid = trans_point_id_from_name(S);
        int tid = trans_point_id_from_name(T);
        Log.e("FOP", "find_opt_path: 拿到id :" + sid + " to " + tid);
        myPath res = new myPath();
        Arrays.fill(book, false);
        Arrays.fill(pre, -1);
        Arrays.fill(dis, INF);
        Log.e("FOP", "初始化完成");
        Queue<Integer> q = new LinkedList<Integer>();
        q.offer(sid);
        book[sid] = true;
        dis[sid] = 0;
        while (!q.isEmpty()) {
            Log.e("FOP", "找");
            int now = q.poll();
            book[now] = false;
            for (int i = head[now]; i != -1; i = nex[i]) {
                int vel = e[i];
//                Log.e("FOP", "去："+vel);
//                Log.e("FOP", "边号："+i);
                if (dis[vel] >= dis[now] + w[i]) {
                    dis[vel] = dis[now] + w[i];
                    pre[vel] = i; // 保存最优路径
                    if (vel == tid)
                        Toast.makeText(context, "找到最优路径啦，最短路径=" + dis[tid], Toast.LENGTH_SHORT).show();
                    if (!book[vel]) {
                        book[vel] = true;
                        q.offer(vel);
                    }
                }
            }
        }

        if (pre[tid] == -1) {
            Toast.makeText(context, "无法到达", Toast.LENGTH_SHORT).show();
            res.setAns("无法到达");
            return res;
        }
        int cnt = 0;
        Stack<Integer> stack = new Stack<Integer>();

        for (int i = tid; i != sid; i = u[pre[i]]) {
            stack.push(i);
            Log.e("PTH", "find_opt_path: " + i);

            int now_edge = pre[i];
            put_edge_point(now_edge, res);

            if (i == sid) break;
        }
        stack.push(sid);
//        Log.e("OPT", "find_opt_path: "+ans);
        String ans = "最优路径:";

        while (!stack.isEmpty()) {
            ans += trans_point_name_from_id(stack.peek()) + " -> ";
            stack.pop();
        }
        ans += " 结束！";
        res.setAns(ans);
        return res;
    }

    private void put_edge_point(int edge_id, myPath res) {
        //存点集
        String sql = "select rx,ry from edge_table where id=?";

        Cursor cs = dbm.getDb().rawQuery(sql, new String[]{String.valueOf(edge_id)});
        cs.moveToFirst();
        @SuppressLint("Range") String rx_str = cs.getString(cs.getColumnIndex("rx"));
        @SuppressLint("Range") String ry_str = cs.getString(cs.getColumnIndex("ry"));
        String[] rx_lst = rx_str.split(",");
        String[] ry_lst = ry_str.split(",");
        for (String x : rx_lst) {
            res.addRx(Integer.parseInt(x));
        }
        for (String y : ry_lst) {
            res.addRy(Integer.parseInt(y));
        }
        Log.e("PTH", "put_edge_point: edge " + edge_id + " is done.");
    }

    private void plan() {
        //进行计算
    }

    public void close_db() {
        dbm.closeDataBase();
    }

    public void open_db() {
        dbm.openDataBase();
    }
}
