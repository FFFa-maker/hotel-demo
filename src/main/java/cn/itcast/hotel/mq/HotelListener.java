package cn.itcast.hotel.mq;

import cn.itcast.hotel.constants.MqConstants;
import cn.itcast.hotel.service.IHotelService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotelListener {
    @Autowired
    private IHotelService hotelService;
    @Autowired
    private RestHighLevelClient client;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqConstants.HOTEL_INSERT_QUEUE),
            exchange = @Exchange(name = MqConstants.HOTEL_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.HOTEL_INSERT_KEY
    ))
    public void listenHotelInsert(Long hotelId) {
        // 新增
        hotelService.insertById(hotelId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqConstants.HOTEL_DELETE_QUEUE),
            exchange = @Exchange(name = MqConstants.HOTEL_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = MqConstants.HOTEL_DELETE_KEY
    ))
    public void listenHotelDelete(Long hotelId) {
        // 删除
        hotelService.deleteById(hotelId);
    }
}
