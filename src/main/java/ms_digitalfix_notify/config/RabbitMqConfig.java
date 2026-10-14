package ms_digitalfix_notify.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String DIRECT_EXCHANGE = "cmd.direct";
    public static final String TOPIC_EXCHANGE = "cmd.topic";
    public static final String DEAD_EXCHANGE = "cmd.dead.dlx";

    public static final String EMAIL_QUEUE = "q.cmd.email";
    public static final String DISPATCH_QUEUE = "q.cmd.dispatch";
    public static final String REPORT_QUEUE = "q.cmd.report";

    public static final String EMAIL_DLQ = "q.cmd.email.dlq";
    public static final String DISPATCH_DLQ = "q.cmd.dispatch.dlq";
    public static final String REPORT_DLQ = "q.cmd.report.dlq";

    public static final String EMAIL_ROUTING_KEY = "email.send";
    public static final String DISPATCH_ROUTING_KEY = "dispatch.ticket";
    public static final String REPORT_ROUTING_KEY = "report.gen";

    public static final String EMAIL_TOPIC = "email.*";
    public static final String DISPATCH_TOPIC = "dispatch.#";
    public static final String REPORT_TOPIC = "report.*";

    public static final String EMAIL_DLQ_ROUTING_KEY = "email.dead";
    public static final String DISPATCH_DLQ_ROUTING_KEY = "dispatch.dead";
    public static final String REPORT_DLQ_ROUTING_KEY = "report.dead";


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_EXCHANGE, true, false);
    }


    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(EMAIL_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue dispatchQueue() {
        return QueueBuilder.durable(DISPATCH_QUEUE)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(DISPATCH_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue reportQueue() {
        return QueueBuilder.durable(REPORT_QUEUE)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(REPORT_DLQ_ROUTING_KEY)
                .build();
    }


    @Bean
    public Queue emailDlq() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    @Bean
    public Queue dispatchDlq() {
        return QueueBuilder.durable(DISPATCH_DLQ).build();
    }

    @Bean
    public Queue reportDlq() {
        return QueueBuilder.durable(REPORT_DLQ).build();
    }

    @Bean
    public Binding emailDirectBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(directExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding dispatchDirectBinding() {
        return BindingBuilder.bind(dispatchQueue())
                .to(directExchange())
                .with(DISPATCH_ROUTING_KEY);
    }

    @Bean
    public Binding reportDirectBinding() {
        return BindingBuilder.bind(reportQueue())
                .to(directExchange())
                .with(REPORT_ROUTING_KEY);
    }


    @Bean
    public Binding emailTopicBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(topicExchange())
                .with(EMAIL_TOPIC);
    }

    @Bean
    public Binding dispatchTopicBinding() {
        return BindingBuilder.bind(dispatchQueue())
                .to(topicExchange())
                .with(DISPATCH_TOPIC);
    }

    @Bean
    public Binding reportTopicBinding() {
        return BindingBuilder.bind(reportQueue())
                .to(topicExchange())
                .with(REPORT_TOPIC);
    }

    @Bean
    public Binding emailDlqBinding() {
        return BindingBuilder.bind(emailDlq())
                .to(deadLetterExchange())
                .with(EMAIL_DLQ_ROUTING_KEY);
    }

    @Bean
    public Binding dispatchDlqBinding() {
        return BindingBuilder.bind(dispatchDlq())
                .to(deadLetterExchange())
                .with(DISPATCH_DLQ_ROUTING_KEY);
    }

    @Bean
    public Binding reportDlqBinding() {
        return BindingBuilder.bind(reportDlq())
                .to(deadLetterExchange())
                .with(REPORT_DLQ_ROUTING_KEY);
    }
}