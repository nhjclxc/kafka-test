package com.example.kafkatest.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.HashMap;
import java.util.Map;

@ComponentScan(basePackages = "com.example.kafkatest")
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(KafkaTemplate.class)
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
	public KafkaAdmin admin() {
		return new KafkaAdmin(this.properties.buildProducerProperties());
	}


}
