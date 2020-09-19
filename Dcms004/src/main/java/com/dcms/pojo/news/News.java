package com.dcms.pojo.news;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: NZD
 * Date: 2020/3/23 0023 17:01
 * Description:
 **/
@Data
public class News {
    private String newsId;      //文章编号
    private String headLine;    //文章标题
    private String newsLabel;   //标签
    private String author;      //发布人（作者）
    private String general;     //文章概要
    private String content;     //内容
    private String newsTime;     //上传时间
    private String status;      //状态
}
