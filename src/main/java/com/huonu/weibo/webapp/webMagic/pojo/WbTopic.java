package com.huonu.weibo.webapp.webMagic.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WbTopic{
    private String uid; //微博uid
    private String name; //微博名
    private String content;//内容
    private String forward;//转发
    private String upvote;//点赞
    private String comment;//评论
    private String topic;//话题
    private String source;//发布工具
    private String time;//时间
    private String pics;//图片
    private String id; //微博id
    private String bid; //详情id
}
