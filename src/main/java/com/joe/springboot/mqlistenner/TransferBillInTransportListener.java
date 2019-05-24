package com.joe.springboot.mqlistenner;

import com.joe.springboot.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @author maqiao
 * @create create on 2019-03-20 11:15
 * @desc
 **/
@Component
@RabbitListener(queues = RabbitConfig.ITM_GPS_IN_TRANSFER_QUEUE)
@Slf4j
public class TransferBillInTransportListener {

	/**
	 * 运单进入运输进行中，在redis中添加相关的key
	 *
	 * @param transferId
	 */
	@RabbitHandler
	public void process(String transferId) {

		// 判断transferId是否能够转换成Long类型
		try {
			Long l = Long.parseLong(transferId);
		} catch (Exception e) {
			log.error("transferId类型错误");
		}

	}

}
