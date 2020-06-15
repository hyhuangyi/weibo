package com.huonu.weibo.webapp.webTask;

import com.huonu.weibo.webapp.comm.*;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.List;
@Slf4j
public class CSDN implements PageProcessor {

    private Site site = Site.me().setCharset("UTF-8").setRetryTimes(3).setSleepTime(100).setUserAgent(Agents.getRandom());
    @Override
    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        // 文章页
        if (page.getUrl().regex("https://blog.csdn.net/qq_37209293/article/details/[0-9]{7}").match()) {
            page.putField("Title", page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString());
            page.putField("Content",
                    page.getHtml().xpath("//*[@id=\"content_views\"]/p/text()").all().toString());
        } else {// 列表页
            List<String> list = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[2]/div/p/a/@href").all();
            page.addTargetRequests(list);
        }
    }
    public static void main(String[] args)throws Exception{
        int num=0;
        while (true){
            log.error("第"+ ++num+"次执行");
            Thread.sleep(60*1000);
            Spider.create(new CSDN()).addUrl("https://blog.csdn.net/qq_37209293/article/list/1").
                    addPipeline(new CSDNPipLine())
                    .setDownloader(Downloader.newIpDownloader())
                    .thread(1).runAsync();
        }
    }
}