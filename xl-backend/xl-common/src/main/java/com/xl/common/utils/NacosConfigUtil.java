package com.xl.common.utils;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;

public class NacosConfigUtil {

    private static final String SERVER_ADDR = "192.168.224.128:8848"; // Nacos 服务器地址
    private static ConfigService configService;
    private static NamingService namingService;
    public String data_id;
    public String group;
    public String propertiesKey;
    public String propertiesValue;
    public String propertiesTempValue;

    static {
        try {
            System.out.println("static exec");
            Properties properties = new Properties();
            properties.put("serverAddr", SERVER_ADDR);
//            指定Nacos-Server的地址
//            properties.setProperty(PropertyKeyConst.SERVER_ADDR, "localhost:8848");
//            指定Nacos-SDK的命名空间
//            properties.setProperty(PropertyKeyConst.NAMESPACE, "${namespaceId}");
            configService = NacosFactory.createConfigService(properties);
            namingService = NacosFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public NacosConfigUtil(String data_id, String group, String propertiesKey) {
        this.data_id = data_id;
        this.group = group;
        this.propertiesKey = propertiesKey;
        this.propertiesValue = getYamlConfig(data_id,group,propertiesKey);
    }

    public static String getConfig(String data_id , String group, long timeoutMs) {
        try {
            return configService.getConfig(data_id, group, timeoutMs);
        } catch (NacosException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getPropertiesConfig(String data_id ,String group,String key,long timeoutMs) {
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

    public static String getPropertiesConfig(String data_id ,String group,String key) {
        return getPropertiesConfig(data_id,group,key,5000);
    }

    /**
     * 获取nacos中yaml类型的配置,key为"spring.data.redis.host"格式
     * @param data_id
     * @param group
     * @param key
     * @param timeoutMs
     * @return
     */
    public static String getYamlConfig(String data_id ,String group,String key,long timeoutMs) {
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

    public static String getYamlConfig(String data_id ,String group,String key) {
        return getYamlConfig(data_id,group,key,5000);
    }

    public static String getJsonNodeKey(JsonNode jsonNode,String key){
        String[] keys = key.split("\\.");
        for (int i = 1; i < keys.length; i++) {
            jsonNode = jsonNode.get(keys[i-1]);
        }
        return jsonNode.get(keys[keys.length-1]).asText();
    }
    /**
     * 监听某个参数的变化
     */
    public void addYamlListener(){
        final NacosConfigUtil nacosConfigUtil = this;
        try {
            configService.addListener(this.data_id, this.group, new Listener() {
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
                        nacosConfigUtil.propertiesValue = getJsonNodeKey(yamlNode,nacosConfigUtil.propertiesKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }



}