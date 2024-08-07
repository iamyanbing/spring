package com.iamyanbing.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class MQConfig {
    public static void main(String[] args) throws Exception {
        String topic = "msb-items";
        Properties p = new Properties();
//连接kafka服务器
        p.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"node02:9092,node03:9092,node01:9092");
//kafka  持久化数据的MQ  数据-> byte[]，不会对数据进行干预，双方要约定编解码
//kafka是一个app：：使用零拷贝  sendfile 系统调用实现快速数据消费
//key序列化。这就是为什么在ProducerRecord中可以输入字符串进行传输
        p.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//value序列化。这就是为什么在ProducerRecord中可以输入字符串进行传输
        p.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        p.setProperty(ProducerConfig.ACKS_CONFIG, "-1");
        p.setProperty(ProducerConfig.RETRIES_CONFIG, "10");
        p.setProperty(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "10");
        //现在的producer就是一个提供者，面向的其实是broker
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(p);

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "item","val");
        //向topic：msb-items  发送消息内容
        Future<RecordMetadata> send = producer.send(record);
        //阻塞等待发送结果
        RecordMetadata rm = send.get();
        //获取partition
        int partition = rm.partition();
        //获取offset
        long offset = rm.offset();
        producer.close();
    }

}
