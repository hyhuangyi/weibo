package com.huonu.weibo.webapp.controller;

import com.huonu.weibo.webapp.util.RedisUtil;
import com.huonu.weibo.webapp.webMagic.base.ProxyDownloader;
import com.huonu.weibo.webapp.webMagic.magic.MobileFans;
import com.huonu.weibo.webapp.webMagic.magic.Weibo;
import com.huonu.weibo.webapp.webMagic.pipLine.MobileFansPipLine;
import com.huonu.weibo.webapp.webMagic.pipLine.WeiboSearchPipLine;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;
import java.util.Set;

@RestController
@Api(tags = "微博粉丝")
public class WeiboFansController {
    @Autowired
    private MobileFans mobileFans;
    @Autowired
    private Weibo weibo;
    @Autowired
    private MobileFansPipLine mobileFansPipLine;
    @Autowired
    private WeiboSearchPipLine weiboSearchPipLine;
    /**
     * 获取redis粉丝列表
     * @return
     */
    @GetMapping("/fans/list")
    @ApiOperation("获取redis粉丝列表")
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
    @ApiOperation("爬取点赞和转发粉丝")
    public boolean handle(@RequestParam(defaultValue = "1") @ApiParam("1点赞 2转发") String type,
                          @RequestParam(defaultValue = "4516021828714033") @ApiParam("微博id") String id){
        String reposts="https://m.weibo.cn/api/statuses/repostTimeline?id="+id+"&page=1";
        String attitudes="https://m.weibo.cn/api/attitudes/show?id="+id+"&page=1";
        //String comments="https://m.weibo.cn/comments/hotflow?id=4516021828714033&mid=4516021828714033&max_id_type=1";
        String url="";
        if("1".equals(type)){//点赞
            url=attitudes;
        }else {//转发
            url=reposts;
        }
        Spider.create(mobileFans).addUrl(url).
                addPipeline(mobileFansPipLine)
                .setDownloader(ProxyDownloader.newIpDownloader())
                .thread(1).runAsync();
        return true;
    }

    @GetMapping("/topics/handle")
    @ApiOperation("微博热搜")
    public boolean topics(@ApiParam("热搜关键词")@RequestParam(defaultValue = "教育部要求严格国际学生申请资格") String key){
        String baseUrl="https://s.weibo.com/weibo?q=%23"+key+"%23";
        Spider.create(weibo).addUrl(baseUrl).
                addPipeline(weiboSearchPipLine)
                .setDownloader(ProxyDownloader.newIpDownloader())
                .thread(1).runAsync();
        return true;
    }
}
