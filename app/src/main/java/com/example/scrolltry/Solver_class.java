package com.example.scrolltry;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class Solver_class {
    private SpfaPlanner sp;
    private myVisualizer vis;
    private Context context;
    public Solver_class( Context context){
        this.context = context;
        this.sp = new SpfaPlanner(context);
        this.vis = new myVisualizer(context);
//        sp.close_db();
    }

    public myPath solve(String[] start_set, String[] end_set){
//        sp.open_db();
//        String ans = sp.find_opt_path(start_set[0], end_set[0]);
        myPath ans = sp.find_opt_path(start_set[0], end_set[0]);
//        sp.close_db();
        return ans;
    }
}
