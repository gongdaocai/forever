package com.xcly.forever.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcly.forever.common.config.MsgProducer;
import com.xcly.forever.common.config.RabbitConfig;
import com.xcly.forever.common.model.MessageVo;
import com.xcly.forever.dao.QiangGouDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class QinagGouService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MsgProducer msgProducer;
    @Autowired
    private QiangGouDao qiangGouDao;
    @Autowired
    private ObjectMapper objectMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String start(String mid, Integer count) throws JsonProcessingException {
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
                //   LOGGER.info("<<====抢购" + count + "件 成功 ，剩余" + increment + " 件");
                MessageVo message = new MessageVo();
                String UserId = UUID.randomUUID().toString();
                message.setUserid(UserId);
                message.setAppSubOrderLstDetail("[{\"supplierid\":\"315419241111093248\",\"skuid\":\"316952840423604224\",\"ticketid\":\"\",\"remark\":\"\"}]");
                message.setAppOrderGoodsLstDetail("[{\"supplierid\":\"315419241111093248\",\"goodsmid\":\"316952840381661184\",\"goodsskuid\":\"316952840423604224\",\"goodscount\":\"1\",\"ticketid\":\"\",\"isvirtual\":0}]");
                message.setAppOrderAddressDetail("{\"receivename\":\"您猪骨头图\",\"receivephone\":\"15675885127\",\"area\":\"[420000, 420100, 420102, 420102003]\",\"detailaddress\":\"湖北省武汉市江岸区一元街明无辜\"}");
                msgProducer.sendMsg(UserId, objectMapper.writeValueAsString(message), RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTING_KEY_A);
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
        redisTemplate.opsForValue().set("316952840381661184", 4500);
        return "添加抢购成功";
    }

    @Transactional(rollbackFor = Exception.class)
    public void insetOrder(String str) {
        qiangGouDao.insertOrder(Long.parseLong(str));
    }
}
