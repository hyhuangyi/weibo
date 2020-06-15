package com.huonu.weibo.webapp.runner;

import com.huonu.weibo.webapp.webTask.CSDN;
import com.huonu.weibo.webapp.comm.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

@RestController
@RequestMapping("/property")
@Slf4j
public class MyRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        int num=0;
        while (true){
            log.error("第"+ ++num+"次执行");
            Thread.sleep(60*1000);
            Spider.create(new CSDN()).addUrl("https://blog.csdn.net/qq_37209293/article/list/1")
                    .addPipeline(new CSDNPipLine())
                    .setDownloader(Downloader.newIpDownloader())
                    .thread(1).runAsync();

            Spider.create(new CSDN()).addUrl("https://blog.csdn.net/qq_37209293/article/list/2")
                    .addPipeline(new CSDNPipLine())
                    .setDownloader(Downloader.newIpDownloader())
                    .thread(1).runAsync();
        }
    }
}
