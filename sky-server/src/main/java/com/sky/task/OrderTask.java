package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

//定时人物类，定时处理订单状态
//flag{This_is_a_tru

@Component
@Slf4j
public class OrderTask {



    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("现在的时间为：{}", LocalDateTime.now().toString());


        //select * from orders where status = ? and create_time< (now - 15)

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);

        if(list.size()>0 && list != null){
            for(Orders order : list){
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单超时，自动取消");
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("当前的时间{}", LocalDateTime.now().toString());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS,time);

        if(list.size()>0 && list != null){
            for(Orders order : list){
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

//e_fl4g}
}
