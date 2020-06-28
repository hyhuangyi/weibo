package com.huonu.weibo.webapp.service.impl;

import com.huonu.weibo.webapp.service.ISysService;
import com.huonu.weibo.webapp.webMagic.base.ProxyDownloader;
import com.huonu.weibo.webapp.webMagic.magic.CSDN;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

@Service
public class SysServiceImpl implements ISysService {
    @Override
    @Async("myTaskAsyncPool")
    public void handleCsdn(boolean isCycle) {
        while (true) {
            if (isCycle) {
                try {//一轮休息45秒
                    Thread.sleep(45 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Spider.create(new CSDN()).addUrl("https://blog.csdn.net/qq_35529931/article/list/1")
                    .addPipeline(new ConsolePipeline())
                    .thread(1).runAsync();

            Spider.create(new CSDN()).addUrl("https://blog.csdn.net/qq_35529931/article/list/2")
                    .addPipeline(new ConsolePipeline())
                    .thread(1).runAsync();
            if (!isCycle) {
                break;
            }
        }
    }
}
