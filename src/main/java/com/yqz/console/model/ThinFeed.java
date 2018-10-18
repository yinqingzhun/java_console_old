package com.yqz.console.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * feed流
 * @author liuchao
 * @date 2017/12/07
 **/
@Data
public class ThinFeed {
    /**
     * 作者id(目前只有原创的文章、视频、直播有作者id)
     */
    private int authorid;

    /**
     * 作者类型
     */
    private int authortype;

    /**
     * 内容id
     */
    @JsonProperty("Objid")
    private int objid;

    /**
     * 内容类型
     */
    private int objtype;

    /**
     * 发布时间
     */
    private String publishtime;

    /**
     * 关注维度
     */
    private List<ConcernTypeVO> concerntype = new ArrayList<ConcernTypeVO>();
    /**
     * 车系id
     */
    private int seriesid;
    private  String name;

     
}
