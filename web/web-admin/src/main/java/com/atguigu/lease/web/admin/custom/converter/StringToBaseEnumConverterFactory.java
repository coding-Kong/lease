package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * @author kdz
 * @create 2025-06-25-15:23
 */

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String source) {
                T[] enumConstants = targetType.getEnumConstants();
                for (T enumConstant : enumConstants) {
                    if(enumConstant.getCode().equals(Integer.valueOf(source))){
                        return enumConstant;
                    }
                }
                throw new IllegalArgumentException("code" + source + "非法");
            }
        };
    }
}
