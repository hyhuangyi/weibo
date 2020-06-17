package com.huonu.weibo.webapp.controller;

import com.huonu.weibo.webapp.util.RedisUtil;
import com.huonu.weibo.webapp.webMagic.base.ProxyDownloader;
import com.huonu.weibo.webapp.webMagic.magic.MobileFans;
import com.huonu.weibo.webapp.webMagic.pipLine.MobileFansPipLIne;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import java.util.Set;

@RestController
public class WeiboFansController {

    /**
     * 获取redis粉丝列表
     * @return
     */
    @GetMapping("/fans/list")
    public Set<Object> getUsers(){
        Set set= RedisUtil.sGet("user");
        System.out.println(set.size());
        return set;
    }

    /**
     * 爬取点赞和转发粉丝
     * @param type  1点赞 2转发
     * @param id 微博id
     * @return 布尔类型
     */
    @GetMapping("fans/handle")
    public boolean handle(@RequestParam(defaultValue = "1") String type,
                          @RequestParam(defaultValue = "4516021828714033") String id){
        String reposts="https://m.weibo.cn/api/statuses/repostTimeline?page=1&id="+id;
        String attitudes="https://m.weibo.cn/api/attitudes/show?page=1&id="+id;
        //String comments="https://m.weibo.cn/comments/hotflow?id=4516021828714033&mid=4516021828714033&max_id_type=1";
        String url="";
        if("1".equals(type)){//点赞
            url=attitudes;
        }else {//转发
            url=reposts;
        }
        Spider.create(new MobileFans()).addUrl(url).
                addPipeline(new MobileFansPipLIne())
                .setDownloader(ProxyDownloader.newIpDownloader())
                .thread(1).runAsync();
        return true;
    }
}
