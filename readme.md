##



###7
* SwaggerUI3.0
    * 进行配置
    * 编写controller
    * 进行测试
    
swagger的配置
```
@Component
@EnableOpenApi
@Data
public class SwaggerConfiguration {
​
​
    @Bean
    public Docket webApiDoc(){
​
​
        return new Docket(DocumentationType.OAS_30)
                .groupName("用户端接口文档")
                .pathMapping("/")
                // 定义是否开启swagger，false为关闭，可以通过变量控制，线上关闭
                .enable(true)
                //配置api文档元信息
                .apiInfo(apiInfo())
                // 选择哪些接口作为swagger的doc发布
                .select()
                .apis(RequestHandlerSelectors.basePackage("net.gaven"))
                //正则匹配请求路径，并分配至当前分组
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }
​
​
​
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("1024电商平台")
                .description("微服务接口文档")
                .contact(new Contact("小滴课堂-二当家小D", "https://xdclass.net", "794666918@qq.com"))
                .version("12")
                .build();
    }
```
编写controller
```
@Api(tags = "收货地址模块")
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {
    @Autowired
    private IAddressService addressService;

    @ApiOperation("根据id查询详情")
    @GetMapping("/find/{address_id}")
    public AddressDO getAddressById(@ApiParam(value = "地址Id", required = true)
                                    @PathVariable("address_id") Integer addressId) {
        AddressDO addressById = addressService.getAddressById(addressId);
        return addressById;
    }

}
```
进行测试
AddressController配置接口文档

访问地址 http://localhost:9001/swagger-ui/index.html#/