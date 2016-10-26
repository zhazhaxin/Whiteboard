package cn.lemon.whiteboard.module.note;

import java.io.Serializable;
import java.util.List;

import cn.lemon.whiteboard.widget.type.WritablePath;


/**
 * Created by user on 2016/10/25.
 */

public class Note implements Serializable{
    public String title;
    public long createTime;
    public List<WritablePath> paths;
}
