package com.atguigu.lease.web.admin.custom.converter;

import com.atguigu.lease.model.enums.ItemType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author kdz
 * @create 2025-06-25-14:54
 */

//@Component
public class StringToItemTermConverter implements Converter<String, ItemType> {
    @Override
    public ItemType convert(String code) {

        ItemType[] values = ItemType.values();
        for(ItemType itemType : values){
            if(itemType.getCode().equals(Integer.valueOf(code))){
                return itemType;
            }
        }
        throw new IllegalArgumentException("code" + code + "非法");
    }
}
