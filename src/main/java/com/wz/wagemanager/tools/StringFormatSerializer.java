package com.wz.wagemanager.tools;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author WindowsTen
 * @date 2018/6/20 17:46
 * @description
 */

public class StringFormatSerializer implements ObjectSerializer {

    public static final StringFormatSerializer instance = new StringFormatSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                      int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullStringAsEmpty);
            return;
        }
        if("creditCard".equals (fieldName) || "IDNumber".equals (fieldName)){
            out.writeString(formatStr ((String) object));
        }else{
            out.writeString((String) object);
        }
    }

    private String formatStr(String object){
        if(object.length ()<=4){
            return object;
        }
        object=object.substring (object.length ()-4,object.length ());
        return "******"+object;
    }

}
