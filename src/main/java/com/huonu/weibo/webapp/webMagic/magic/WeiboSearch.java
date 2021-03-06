package com.huonu.weibo.webapp.webMagic.magic;

import com.alibaba.fastjson.JSON;
import com.huonu.weibo.webapp.webMagic.base.*;
import com.huonu.weibo.webapp.util.DateUtils;
import com.huonu.weibo.webapp.util.RegexUtils;
import com.huonu.weibo.webapp.webMagic.pojo.WbTopic;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.List;

@Slf4j
@Component
public class WeiboSearch implements PageProcessor {
    //private Site site = Site.me().setCharset("UTF-8").setRetryTimes(6).setCycleRetryTimes(10).setSleepTime(1000).setUserAgent(Agents.getRandom()).addCookie("cookie","ALF=1593086936; _T_WM=94541653860; SCF=AiV16yxZCyVi-LYiFkIbGilv8FWdeox2wxQ8AudRx27kZESmvGXzPdQBxBmZKr6LS3gm0t6xnmWdMKiRPNqKFl8.; SUB=_2A25zyXgjDeRhGeNK61YW9yfOyzyIHXVRMhhrrDV6PUJbktANLRShkW1NSWgnEI6Yu7MMREhv31SUata4z5oK0Bg6; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W51Rx-mMQz6R6cHcQEO5dkI5JpX5K-hUgL.Fo-XehBNS0.Eeh52dJLoIp7LxKML1KBLBKnLxKqL1hnLBoMfSh5XS0M4eo57; SUHB=0U1Jk6nD0H9z3V; SSOLoginState=1590495347; MLOGIN=1; M_WEIBOCN_PARAMS=luicode%3D20000174; __guid=78840338.122118085663280260.1590495394219.12; monitor_count=1");

    @Value("${s.weibo.com.cookie}")
    private String cookie;

    @Override
    public Site getSite() {
        return Site.me().setCharset("UTF-8").setRetryTimes(6).setCycleRetryTimes(10).setSleepTime(1000).setUserAgent(Agents.getRandom()).addCookie("cookie",cookie);
    }
    public void process(Page page) {
        List<String> res= Lists.newArrayList();
        List<String> list = page.getHtml().xpath("//*[@id=\"pl_feedlist_index\"]/div[1]/div").all();
        for(String s:list){
            try {
                WbTopic wbTopic=new WbTopic();
                Document document= Jsoup.parse(s);
                String uid=""; //uid
                String content="";//内容
                String source="";//发布工具
                String time="";//发布时间
                String name="";//微博名
                String pics="";//图片
                String id="";//微博id
                String bid="";//详情id
                //用户id
                Elements e_id=document.select("div[class=info]").select("div");
                if(e_id.size()>=3){
                    uid=  document.select("div[class=info]").select("div").get(2).select("a").get(0).attr("href");
                    uid=uid.substring(uid.lastIndexOf("/")+1,uid.indexOf("?"));
                    name=document.select("div[class=info]").select("div").get(2).select("a").get(0).text();
                    id=document.select("div[class=card-wrap]").attr("mid");
                }else {
                    continue;
                }
                Elements e_txt=document.select("div[class=content]").select("p[class=txt]");
                if(e_txt.size()==1){
                    content=e_txt.get(0).text();
                } else {
                    content=e_txt.get(1).text();
                }
                //发布工具、时间
                Elements e_source=document.select("p[class=from]").select("a");
                time=e_source.get(0).text();
                String detailUrl=e_source.get(0).attr("href");
                bid=detailUrl.substring(detailUrl.lastIndexOf("/")+1,detailUrl.indexOf("?"));
                if(e_source.size()==1){
                    log.info("只有发布时间");
                } else {
                    source=e_source.get(e_source.size()-1).text();
                }
                //转发
                String forward=document.select("div[class=card-act]").select("ul").select("li").get(1).select("a").text().substring(2).trim();
                //评论
                String comment=document.select("div[class=card-act]").select("ul").select("li").get(2).select("a").text().substring(2).trim();
                //点赞
                String upvote=document.select("div[class=card-act]").select("ul").select("li").get(3).select("a").select("em").text().trim();
                String pic1=document.select("div[class=pic]").select("img").attr("src");
                String pic2=document.select("div[class=media media-piclist]").select("img").attr("src");
                if(!"".equals(pic2)){//只取一张
                    pics=pic2;
                }else {
                    pics=pic1;
                }
                wbTopic=wbTopic.setUid(uid).setName(name).setForward(forward.equals("")?"0":forward).setComment(comment.equals("")?"0":comment)
                        .setUpvote(upvote.equals("")?"0":upvote).setContent(content).setTopic(RegexUtils.getTags(content))
                        .setSource(source).setPics(pics).setTime(DateUtils.parseWeiboDate(time)).setId(id).setBid(bid);
                res.add(JSON.toJSONString(wbTopic));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        page.putField("list", res);
        //下一页
        List<String> url=page.getHtml().xpath("//*[@id=\"pl_feedlist_index\"]/div[2]/div/a[@class='next']/@href").all();
        page.addTargetRequests(url);
    }
}
