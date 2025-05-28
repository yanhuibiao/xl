package com.xl.common.utils;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class NacosConfigUtil {

    private static final String SERVER_ADDR = "192.168.224.128:8848"; // Nacos 服务器地址
    private static final String NAMESPACE = ""; // 可选："" 表示默认命名空间
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";
    private static final long TIMEOUT_MS = 5000;

    public ConfigService configService;
    public NamingService namingService;
    public String propertiesValue;
    public ConcurrentHashMap<String, Listener> configListenerMap = new ConcurrentHashMap<>();

    private NacosConfigUtil() {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", SERVER_ADDR);
            properties.setProperty(PropertyKeyConst.USERNAME, "nacos");
//            properties.setProperty(PropertyKeyConst.NAMESPACE, "${namespaceId}");
            properties.setProperty(PropertyKeyConst.PASSWORD, "nacos");
//            初始化配置中心的Nacos Java SDK(配置中心)
            this.configService = NacosFactory.createConfigService(properties);
//            初始化配置中心的Nacos Java SDK(服务注册与发现)
            this.namingService = NacosFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            throw new RuntimeException("初始化 NacosConfigUtil 失败", e);
        }
    }

    private static class Holder {
        private static final NacosConfigUtil INSTANCE = new NacosConfigUtil();
    }

    // 单例模式
    public static NacosConfigUtil getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 查询配置
     * @param dataId
     * @param group  DEFAULT_GROUP
     * @param timeoutMs ms
     * @return String
     */
    public String getConfig(String dataId , String group, long timeoutMs) {
        try {
            return configService.getConfig(dataId, group, timeoutMs);
        } catch (NacosException e) {
            throw new RuntimeException("获取配置失败: " + dataId, e);
        }
    }

    public String getConfig(String dataId) {
        return getConfig(dataId, DEFAULT_GROUP, TIMEOUT_MS);
    }

    /**
     * 发布配置
     */
    public boolean publishConfig(String dataId, String group, String content) {
        try {
            return configService.publishConfig(dataId, group, content);
        } catch (NacosException e) {
            throw new RuntimeException("发布配置失败: " + dataId, e);
        }
    }

    public boolean publishConfig(String dataId, String content) {
        return publishConfig(dataId, DEFAULT_GROUP, content);
    }

    /**
     * 删除配置
     */
    public boolean removeConfig(String dataId,String group) {
        try {
            return configService.removeConfig(dataId, group);
        } catch (NacosException e) {
            throw new RuntimeException("删除配置失败: " + dataId, e);
        }
    }

    public boolean removeConfig(String dataId) {
        return removeConfig(dataId, DEFAULT_GROUP);
    }

    public String getPropertiesConfig(String data_id ,String group,String key,long timeoutMs) {
        Properties properties = new Properties();
        String config = getConfig(data_id, group, timeoutMs);
        try {
            properties.load(new StringReader(config));
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getPropertiesConfig(String data_id ,String group,String key) {
        return getPropertiesConfig(data_id,group,key,TIMEOUT_MS);
    }

    /**
     * 获取nacos中yaml类型的配置,key为"spring.data.redis.host"格式
     * @param data_id
     * @param group
     * @param key
     * @param timeoutMs
     * @return
     */
    public String getYamlConfig(String data_id ,String group,String key,long timeoutMs) {
        YAMLMapper yamlMapper = new YAMLMapper();
        String config = getConfig(data_id, group, timeoutMs);
        JsonNode yamlNode = null;
        try {
            yamlNode = yamlMapper.readTree(config);
            return getJsonNodeKey(yamlNode,key);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getYamlConfig(String data_id ,String group,String key) {
        return getYamlConfig(data_id,group,key,5000);
    }

    /**
     *
     * @param jsonNode
     * @param key
     * @return
     */
    public String getJsonNodeKey(JsonNode jsonNode,String key){
        String[] keys = key.split("\\.");
        for (int i = 1; i < keys.length; i++) {
            jsonNode = jsonNode.get(keys[i-1]);
        }
        return jsonNode.get(keys[keys.length-1]).asText();
    }

    /**
     * 监听某个参数的变化，将变化的值返回给实例的propertiesValue属性
     */
    public void addYamlListener(String dataId , String group, String key) {
        Listener listener = new Listener(){
            // 返回 null 表示使用默认的执行器
            @Override
            public Executor getExecutor() {
                return null;
            }
            // 在这里处理配置变化的逻辑
            @Override
            public void receiveConfigInfo(String configInfo) {
                YAMLMapper yamlMapper = new YAMLMapper();
                try {
                    JsonNode yamlNode = yamlMapper.readTree(configInfo);
                    NacosConfigUtil.getInstance().propertiesValue = getJsonNodeKey(yamlNode, key);
                } catch (Exception e) {
                    throw new RuntimeException("nacos 监听配置出现问题",e);
                }
            }
        };
        this.configListenerMap.put(dataId+"_"+group, listener);
        try {
            this.configService.addListener(dataId, group, listener);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public void removeYamlListener(String dataId ,String group) {
        this.configService.removeListener(dataId, group, configListenerMap.get(dataId+"_"+group));
    }

    public void registerInstance(String serviceName, String groupName, String ip, int port, String clusterName) throws NacosException {
        this.namingService.registerInstance(serviceName, groupName, ip,port, clusterName);
    }
}