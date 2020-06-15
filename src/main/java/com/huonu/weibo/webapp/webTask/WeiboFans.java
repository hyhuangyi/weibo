package com.huonu.weibo.webapp.webTask;

import com.huonu.weibo.webapp.comm.Agents;
import com.huonu.weibo.webapp.comm.Downloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.List;

public class WeiboFans implements PageProcessor {
    private Site site = Site.me().setCharset("UTF-8").setRetryTimes(3).setSleepTime(100).setUserAgent(Agents.getRandom()).addCookie("cookie","ALF=1593086936; _T_WM=94541653860; SCF=AiV16yxZCyVi-LYiFkIbGilv8FWdeox2wxQ8AudRx27kZESmvGXzPdQBxBmZKr6LS3gm0t6xnmWdMKiRPNqKFl8.; SUB=_2A25zyXgjDeRhGeNK61YW9yfOyzyIHXVRMhhrrDV6PUJbktANLRShkW1NSWgnEI6Yu7MMREhv31SUata4z5oK0Bg6; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9W51Rx-mMQz6R6cHcQEO5dkI5JpX5K-hUgL.Fo-XehBNS0.Eeh52dJLoIp7LxKML1KBLBKnLxKqL1hnLBoMfSh5XS0M4eo57; SUHB=0U1Jk6nD0H9z3V; SSOLoginState=1590495347; MLOGIN=1; M_WEIBOCN_PARAMS=luicode%3D20000174; __guid=78840338.122118085663280260.1590495394219.12; monitor_count=1");

    @Override
    public Site getSite() {
        return site;
    }
    @Override
    public void process(Page page) {
        String url=page.getUrl().toString();
        String ok= page.getJson().jsonPath("ok").get();
        if("1".equals(ok)){
         List<String> data=page.getJson().jsonPath("data.data").all();
         String target=url.substring(0,url.lastIndexOf("=")+1)+(Long.parseLong(url.substring(url.lastIndexOf("=")+1))+1);
         System.out.println(target);
         page.addTargetRequest(target);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            page.putField("users",data);
        }
    }

    public static void main(String[] args) {
        Spider.create(new WeiboFans()).addUrl("https://m.weibo.cn/api/attitudes/show?id=4516021828714033&page=1").
                addPipeline(new JsonFilePipeline("/home/webmagic"))
                .setDownloader(Downloader.newIpDownloader())
                .thread(1).runAsync();
    }

}
