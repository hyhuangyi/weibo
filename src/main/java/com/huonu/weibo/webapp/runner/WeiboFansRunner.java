package com.huonu.weibo.webapp.runner;

import com.huonu.weibo.webapp.webMagic.base.ProxyDownloader;
import com.huonu.weibo.webapp.webMagic.magic.MobileFans;
import com.huonu.weibo.webapp.webMagic.pipLine.MobileFansPipLIne;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

@RestController
@Slf4j
//@Order(1)
public class WeiboFansRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        Spider.create(new MobileFans()).addUrl("https://m.weibo.cn/api/attitudes/show?id=4516021828714033&page=1").
                addPipeline(new MobileFansPipLIne())
                .setDownloader(ProxyDownloader.newIpDownloader())
                .thread(1).runAsync();
    }
}
