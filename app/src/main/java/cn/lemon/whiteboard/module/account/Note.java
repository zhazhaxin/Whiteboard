package cn.lemon.whiteboard.module.account;

import java.io.Serializable;
import java.util.List;

import cn.lemon.whiteboard.widget.shape.ShapeResource;


/**
 * Created by user on 2016/10/25.
 */

public class Note implements Serializable{

    public String mFileName;
    public String mTitle;
    public long mCreateTime;
    public List<ShapeResource> mPaths;
}
