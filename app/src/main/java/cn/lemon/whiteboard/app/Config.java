package cn.lemon.whiteboard.app;

import android.graphics.Color;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class Config {

    //MainActivity --> NoteActivity
    public static final int NOTE_REQUEST_CODE = 465;
    public static final int NOTE_RESULT_CODE = 666;

    public static final String NOTE_DATA = "note_data";


    //color selector
    public static final Integer[] COLORS = {
            new Integer(Color.BLACK),
            new Integer(0xFFFFC125),new Integer(0xFFFF82AB),new Integer(0xFFFFD39B),new Integer(0xFFFFBBFF),
            new Integer(0xFFFF8247),new Integer(0xFFFF34B3),new Integer(0xFFFF0000),new Integer(0xFFFF00FF),
            new Integer(0xFFFF6A6A),new Integer(0xFFFF4500),new Integer(0xFFC1CDCD),new Integer(0xFFC6E2FF)
    };
}
