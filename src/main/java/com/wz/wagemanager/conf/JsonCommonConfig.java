package com.wz.wagemanager.conf;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.Collections;

@Configuration
public class JsonCommonConfig {
    @Bean
    public HttpMessageConverters fastjsonHttpMessageConverter(){
        //定义一个转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        fastConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        //添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializeFilters ((ValueFilter) (object, name, value) -> {
            if (value == null || "".equals (value)) {
                return value;
            }
            if("creditCard".equals (name) || "iDNumber".equals (name)){
                return formatStr (value);
            }
            return value;
        });
        fastJsonConfig.setSerializerFeatures(
                //禁止循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat
        );
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        //在转换器中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters((HttpMessageConverter<?>) fastConverter);

    }

    private Object formatStr(Object object){
        String str= (String) object;
        if(str.length ()<4){
            return object;
        }
        str=str.substring (str.length ()-4,str.length ());
        return "******"+str;
    }

}
