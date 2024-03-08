package com.example.kafkatest.config;

import lombok.Data;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class KafkaTools {
 
    private static KafkaTools _this;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    @PostConstruct
    public void init() {
        _this = this;
    }

    /**
     * 获取分组下的表述信息
     **/
    private static long[] getDescribe(String topic) {
        long[] describe = new long[3];
        Consumer<String, String> consumer = createConsumer();

        List<PartitionInfo> partitionInfos = _this.kafkaTemplate.partitionsFor(topic);
        List<TopicPartition> tp = new ArrayList<>();
        partitionInfos.forEach(str -> {
            TopicPartition topicPartition = new TopicPartition(topic, str.partition());
            tp.add(topicPartition);
            long logEndOffset = consumer.endOffsets(tp).get(topicPartition);

            consumer.assign(tp);
            //consumer.position(topicPartition);
            long currentOffset = consumer.position(topicPartition);

            //System.out.println("logEndOffset : " + logEndOffset + ", currentOffset : "+ currentOffset);
            describe[0] += currentOffset;
            describe[1] += logEndOffset;
            describe[2] = describe[1] - describe[0];

            tp.clear();
        });

        //System.out.println(Arrays.toString(describe));
        return describe;
    }

    /**
     * 创建消费者
     **/
    private static Consumer<String, String> createConsumer() {
        return _this.consumerFactory.createConsumer();
    }

    /**
     * 获取topic的lag
     * @param topic
     * @return
     */
    public static Long getLag(String topic) {
        return getDescribe(topic)[2];
    }

    public static KafkaTopic getTopicDetail(String topic) {
        KafkaTopic detail = new KafkaTopic();

        detail.setName(topic);

        List<PartInfo> list = new ArrayList<>();
        Consumer<String, String> consumer = createConsumer();
        long[] lag = new long[1];

        List<PartitionInfo> partitionInfos = _this.kafkaTemplate.partitionsFor(topic);
        List<TopicPartition> tp = new ArrayList<>();
        partitionInfos.forEach(str -> {
            PartInfo info = new PartInfo();

            info.setPartName(str.toString());
            TopicPartition topicPartition = new TopicPartition(topic, str.partition());
            tp.add(topicPartition);
            long logEndOffset = consumer.endOffsets(tp).get(topicPartition);

            consumer.assign(tp);
            //consumer.position(topicPartition);
            long currentOffset = consumer.position(topicPartition);

            info.setCurrentOffset(currentOffset);
            info.setEndOffset(logEndOffset);

            info.setLag(logEndOffset - currentOffset);
            lag[0] += (logEndOffset - currentOffset);
            list.add(info);

            tp.clear();
        });

        detail.setPartInfos(list);
        detail.setPartition(partitionInfos.size());
        detail.setLag((int)lag[0]);

        return detail;
    }

    /**
     * 创建topic，指定partition
     * @param topicName
     * @param numPar
     * @return
     */
    public static boolean createToipc(String topicName, int numPar) {
        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());

        if(client !=null) {
            try {
                Collection<NewTopic> newTopics = new ArrayList<>(1);
                newTopics.add(new NewTopic(topicName, numPar, (short) 1));
                client.createTopics(newTopics);
            }
            catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
            finally {
                client.close();
            }
        }

        return true;
    }

    /**
     * 删除topic
     * @param topic
     * @return
     * @throws Exception
     */
    public static boolean deleteTopic(String topic) {
        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());

        // 服务端server.properties需要设置delete.topic.enable=true，才可以使用同步删除，否则只是将主题标记为删除
        try {
            client.deleteTopics(Arrays.asList(topic));
        }
        catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        finally {
            client.close();
        }

        return true;
    }

    /**
     * 列出所有topic名称
     * @return
     */
    public static String listTopics() {
        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());
        String r = "";
        if (client != null) {
            try {
                ListTopicsResult result = client.listTopics();
                Set<String> topics = result.names().get();
                r = topics.toString();
            }
            catch (Throwable e) {
                e.printStackTrace();
            }
            finally {
                client.close();
            }
        }

        return r;
    }

    /**
     * 获取指定topic的分区数
     * @param topic
     * @return
     */
    public static int getPartition(String topic) {
        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());
        int num = 0;
        try {
            TopicDescription description = client.describeTopics(Arrays.asList(topic)).all().get().get(topic);
            //r = description.toString();
            num = description.partitions().size();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            client.close();
        }

        return num;
    }

    /**
     * 修改指定topic的分区数
     * @param topic
     * @param numPartitions
     * @return：如果指定的新分区数小于现有分区数，不成功，返回false
     */
    public static boolean updatePartitions(String topic, Integer numPartitions) {
        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());

        NewPartitions newPartitions = NewPartitions.increaseTo(numPartitions);
        Map<String, NewPartitions> map = new HashMap<>(1, 1);
        map.put(topic, newPartitions);

        try {
            client.createPartitions(map).all().get();
        }
        catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        finally {
            client.close();
        }

        return true;
    }

    /**
     * 查询Topic的配置信息
     */
    public static void describeConfig() {
        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());

        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, "test1");
        Collection<ConfigResource> coll = new ArrayList<ConfigResource>();
        coll.add(configResource);

        DescribeConfigsResult result = client.describeConfigs(coll);

        try {
            Map<ConfigResource, Config> map = result.all().get();

            map.forEach((key, value) ->
                    System.out.println("name: " + key.name() + ", desc: " + value));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            client.close();
        }
    }

    /**
     * 修改Topic的配置信息
     */
    public static void incrementalAlterConfig() {
        // 指定ConfigResource的类型及名称
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, "MyTopic");

        Collection<ConfigResource> coll = new ArrayList<ConfigResource>();
        coll.add(configResource);

        // 配置项同样以ConfigEntry形式存在，只不过增加了操作类型
        // 以及能够支持操作多个配置项，相对来说功能更多、更灵活
        Collection<AlterConfigOp> configs = new ArrayList<AlterConfigOp>();
        configs.add(new AlterConfigOp(
                new ConfigEntry("preallocate", "false"),
                AlterConfigOp.OpType.SET
        ));

        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());
        Map<ConfigResource, Collection<AlterConfigOp>> configMaps = new HashMap<>();
        configMaps.put(configResource, configs);
        AlterConfigsResult result = client.incrementalAlterConfigs(configMaps);

        try {
            System.out.println(result.all().get());
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            client.close();
        }
    }

    /**
     * 修改Topic的配置信息
     */
    public static void alterConfig() {
        // 指定ConfigResource的类型及名称
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, "test1");
        // 配置项以ConfigEntry形式存在
        Collection<ConfigEntry> coll = new ArrayList<ConfigEntry>();
        coll.add(new ConfigEntry("preallocate", "true"));
        Config config = new Config(coll);

        AdminClient client = AdminClient.create(_this.kafkaProperties.buildAdminProperties());
        Map<ConfigResource, Config> configMaps = new HashMap<>();
        configMaps.put(configResource, config);
        AlterConfigsResult result = client.alterConfigs(configMaps);
 
        try {
            System.out.println(result.all().get());
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {
            client.close();
        }
    }
}


@Data
 class KafkaTopic {
    String name;
    int partition;
    int lag;
    List<PartInfo> partInfos;
}

@Data
 class PartInfo {
    String partName;
    long currentOffset;
    long endOffset;
    long lag;
}
