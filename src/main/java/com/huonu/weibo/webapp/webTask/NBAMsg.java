package com.huonu.weibo.webapp.webTask;

import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class NBAMsg implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public Site getSite() {
        return site;
    }

    public void process(Page page) {
        // 文章页，匹配 https://voice.hupu.com/nba/七位数字.html
        if (page.getUrl().regex("https://voice.hupu.com/nba/[0-9]{7}\\.html").match()) {
            page.putField("Title", page.getHtml().xpath("/html/body/div[4]/div[1]/div[1]/h1/text()").toString());
            page.putField("Content",
                    page.getHtml().xpath("/html/body/div[4]/div[1]/div[2]/div/div[2]/p/text()").all().toString());
        }
        // 列表页
        else {
            // 文章url
            page.addTargetRequests(
                    page.getHtml().xpath("/html/body/div[3]/div[1]/div[2]/ul/li/div[1]/h4/a").all());
            // 翻页url
            page.addTargetRequests(
                    page.getHtml().xpath("/html/body/div[3]/div[1]/div[3]/a[@class='page-btn-prev']/@href").all());
        }
    }

    public static void main(String[] args) {
        Spider.create(new NBAMsg()).addUrl("https://voice.hupu.com/nba/1").addPipeline(new JsonFilePipeline("/home/webmagic"))
                .thread(3).run();
    }
}