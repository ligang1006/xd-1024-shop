package net.gaven.enums;


/**
 * @author: lee
 * @create: 2021/8/11 9:36 上午
 **/
public enum DefaultAddressStatusEnum {


    DEFAULT_ADDRESS(1),

    COMMON_ADDRESS(0);
    private Integer code;

    DefaultAddressStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
