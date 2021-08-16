package net.gaven.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: lee
 * @create: 2021/8/16 8:45 上午
 **/
@Data
@ApiModel
public class NewUserCouponRequest {

    @ApiModelProperty(value = "用户Id",example = "19")
    @JsonProperty("user_id")
    private long userId;


    @ApiModelProperty(value = "名称",example = "二当家小D")
    @JsonProperty("name")
    private String name;

}
