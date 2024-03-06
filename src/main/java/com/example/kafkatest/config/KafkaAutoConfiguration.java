package com.example.kafkatest.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ComponentScan(basePackages = "com.example.kafkatest")
//@Configuration(proxyBeanMethods = false)
//@ConditionalOnClass(KafkaTemplate.class)
@EnableConfigurationProperties(KafkaProperties.class)
//@Import({ KafkaAnnotationDrivenConfiguration.class, KafkaStreamsAnnotationDrivenConfiguration.class })
public class KafkaAutoConfiguration {

	private final KafkaProperties properties;

	public KafkaAutoConfiguration(KafkaProperties properties) {
		this.properties = properties;
	}

	@Bean
	@Primary
	@ConditionalOnMissingBean(KafkaTemplate.class)
	public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
											 ProducerListener<Object, Object> kafkaProducerListener,
											 ObjectProvider<RecordMessageConverter> messageConverter) {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
		kafkaTemplate.setProducerListener(kafkaProducerListener);
		kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}


	@Bean(name = "defaultKafkaTemplate")
	@ConditionalOnMissingBean(KafkaTemplate.class)
	public KafkaTemplate<Object, Object> defaultKafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory) {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		kafkaTemplate.setDefaultTopic("topic-default");
		return kafkaTemplate;
	}
	/**
	 * 获取生产者工厂
	 */
	@Bean(name="newKafkaProducerFactory")
	public ProducerFactory<Object, Object> newProducerFactory() {
		Map<String, Object> producerProperties = this.properties.buildProducerProperties();
		// 修改参数名称
		producerProperties.put(ProducerConfig.ACKS_CONFIG,"all");
		DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(
				producerProperties);
		String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
		if (transactionIdPrefix != null) {
			factory.setTransactionIdPrefix(transactionIdPrefix);
		}

		return factory;
	}

	@Bean(name="newKafkaTemplate")
	public KafkaTemplate<?, ?> newKafkaTemplate(@Qualifier("newKafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory,
												ProducerListener<Object, Object> kafkaProducerListener,
												ObjectProvider<RecordMessageConverter> messageConverter) {
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
		kafkaTemplate.setProducerListener(kafkaProducerListener);
		kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(ProducerListener.class)
	public ProducerListener<Object, Object> kafkaProducerListener() {
		return new LoggingProducerListener<>();
	}

	@Bean
	@ConditionalOnMissingBean(ConsumerFactory.class)
	public ConsumerFactory<?, ?> kafkaConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties());
	}

	@Bean
	@ConditionalOnMissingBean(ProducerFactory.class)
	public ProducerFactory<?, ?> kafkaProducerFactory() {
		DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(
				this.properties.buildProducerProperties());
		String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
		if (transactionIdPrefix != null) {
			factory.setTransactionIdPrefix(transactionIdPrefix);
		}
		return factory;
	}


	@Bean
	public KafkaAdmin kafkaAdmin() {
		return new KafkaAdmin(this.properties.buildProducerProperties());
	}
	@Bean
	public AdminClient adminClient() {
		return AdminClient.create(this.properties.buildProducerProperties());
	}


	@Bean
	public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
			ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
			ConsumerFactory<Object, Object> kafkaConsumerFactory,
			KafkaTemplate<Object, Object> template) {
		ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		configurer.configure(factory, kafkaConsumerFactory);
		//最大重试三次
		factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template), 3));
		return factory;
	}

	@Bean
	public KafkaListenerContainerFactory<?> batchFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(5);
		factory.getContainerProperties().setPollTimeout(1000);
		factory.setBatchListener(true);//设置为批量消费，每个批次数量在Kafka配置参数中设置
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);//设置手动提交ackMode
		return factory;
	}

	//消费者工厂
	@Bean
	public ConsumerFactory<Integer, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(this.properties.buildProducerProperties());
	}


}
