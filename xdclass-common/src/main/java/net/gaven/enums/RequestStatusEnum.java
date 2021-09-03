package net.gaven.enums;


/**
 * @author: lee
 * @create: 2021/9/2 1:56 下午
 **/

public enum RequestStatusEnum {
    OK(0, "SUCCESS"),
    FAIL(-1, "fail");
    private Integer code;
    private String desc;

    RequestStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
