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
            new Integer(Color.BLACK), new Integer(0xFFFFC125),new Integer(0xFFFF82AB),new Integer(0xFFFFD39B),
            new Integer(0xFFFFBBFF),new Integer(0xFF7FFFD4),new Integer(0xFF7D26CD),new Integer(0xFFC0FF3E),
            new Integer(0xFFFF8247),new Integer(0xFFFF34B3),new Integer(0xFFFF0000),new Integer(0xFFFF00FF),
            new Integer(0xFFFF6A6A),new Integer(0xFFFF4500),new Integer(0xFFC1CDCD),new Integer(0xFFC6E2FF),
            new Integer(0xFF32CD32),new Integer(0xFF228B22),new Integer(0xFF191970),new Integer(0xFF9B30FF),
            new Integer(0xFF8B0A50),new Integer(0xFF8B3A3A),new Integer(0xFF8B7500),new Integer(0xFFC6E2FF),
            new Integer(0xFF218868),new Integer(0xFF2F4F4F),new Integer(0xFFCD5B45),new Integer(0xFFEE7600)
    };
}
