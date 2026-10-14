package ms_digitalfix_notify.listener;

import tools.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import ms_digitalfix_notify.config.RabbitMqConfig;
import ms_digitalfix_notify.dto.WorkOrderEvent;
import ms_digitalfix_notify.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReportListener {

    private static final Logger log =
            LoggerFactory.getLogger(ReportListener.class);

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    public ReportListener(
            ObjectMapper objectMapper,
            NotificationService notificationService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    @RabbitListener(
            queues = RabbitMqConfig.REPORT_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumir(
            String mensaje,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws IOException {

        try {

            WorkOrderEvent evento =
                    objectMapper.readValue(
                            mensaje,
                            WorkOrderEvent.class
                    );

            notificationService.procesarReporte(evento);

            channel.basicAck(
                    deliveryTag,
                    false
            );

            log.info(
                    "ACK enviado para mensaje REPORT. Orden={}",
                    evento.workOrderId()
            );

        } catch (Exception e) {

            log.error(
                    "Error procesando REPORT. Mensaje enviado a DLQ",
                    e
            );

            channel.basicNack(
                    deliveryTag,
                    false,
                    false
            );
        }
    }
}