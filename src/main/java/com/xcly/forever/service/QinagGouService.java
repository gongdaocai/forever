package com.xcly.forever.service;

import com.xcly.forever.common.config.MsgProducer;
import com.xcly.forever.dao.QiangGouDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QinagGouService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MsgProducer msgProducer;
    @Autowired
    private QiangGouDao qiangGouDao;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //  @Transactional(rollbackFor = Exception.class)
    public String start(String mid, Integer count) {
//        Map<String, Object> skuById = qiangGouDao.getSkuById(mid);
//        if (Integer.valueOf((skuById.get("stock").toString())) >= count) {
//            Integer integer = qiangGouDao.updateStockBySkuId(mid, count);
//            if (integer == 1) {
//                return "抢购成功";
//            } else {
//                return "已经抢购完了,下次再来";
//            }
//
//        } else {
//            return "已经抢购完了,下次再来";
//        }
        if (Integer.valueOf(redisTemplate.opsForValue().get(mid).toString()) >= count) {

            Long increment = redisTemplate.opsForValue().increment(mid, -count);
            if (increment >= 0) {
                LOGGER.info("<<====抢购" + count + "件 成功 ，剩余" + increment + " 件");
//                msgProducer.sendMsg(count.toString());
                qiangGouDao.updateStockBySkuId("190844032484638720", count);
                return "抢购成功";
            } else {
                redisTemplate.opsForValue().increment(mid, count);
                return "已经抢购完了,下次再来";
            }
        } else {
            return "已经抢购完了,下次再来";
        }
    }

    public String addQiangGou() {
        redisTemplate.opsForValue().set("190844032484638720", 5000);
        return "添加抢购成功";
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Integer count) {
        qiangGouDao.updateStockBySkuId("190844032484638720", count);
    }
}
