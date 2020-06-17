package com.huonu.weibo.webapp.webMagic.pipLine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huonu.weibo.webapp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;
import java.util.*;

/**
 * 微博手机端点赞持久化
 */
@Component
@Slf4j
public class MobileAttitudesPipLIne extends FilePersistentBase implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> list = resultItems.get("users");
        if (list!=null&&list.size() != 0) {
            for (String s : list) {
                JSONObject jo = JSON.parseObject(s).getJSONObject("user");
                String id = jo.get("id").toString();
                String name = jo.get("screen_name").toString();
                Map map=new HashMap();
                map.put("id",id);
                map.put("name",name);
                RedisUtil.lSet("user",map);
            }
        }
    }
}
