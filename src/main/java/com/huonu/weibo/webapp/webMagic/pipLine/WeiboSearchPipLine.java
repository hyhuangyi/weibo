package com.huonu.weibo.webapp.webMagic.pipLine;

import com.huonu.weibo.webapp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.util.List;

@Component
@Slf4j
public class WeiboSearchPipLine extends FilePersistentBase implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        //拿到数据 可以存redis或者mysql
        List<Object> list=resultItems.get("list");
        if(list.size()!=0){
            RedisUtil.lSet("topic",list);
        }
    }
}
