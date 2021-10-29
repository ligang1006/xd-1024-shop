echo "=======构建脚本执⾏完毕====="
#登录
docker login --username=13081855579lggmail registry.cn-beijing.aliyuncs.com --password=19931006Lg
#构建整个项⽬，或者单独构建common项⽬,避免依赖未被构建
上去
cd ../xdclass-common
mvn install

#构建⽹关
cd ../xdclass-gateway
mvn install -Dmaven.test.skip=true dockerfile:build
docker tag xdclass-cloud/xdclass-gateway:latest registry.cn-beijing.aliyuncs.com/xdclass1024cloud/api-gateway:v1.2
docker push registry.cn-beijing.aliyuncs.com/xdclass1024cloud/api-gateway:v1.2
echo "⽹关构建推送成功"

#⽤户服务
cd ../xdclass-user-service
mvn install -Dmaven.test.skip=true dockerfile:build
docker tag xdclass-cloud/xdclass-user-service:latest registry.cn-beijing.aliyuncs.com/xdclass1024cloud/user-server:v1.2
docker push registry.cn-beijing.aliyuncs.com/xdclass1024cloud/user-server:v1.2
echo "⽤户服务构建推送成功"
#
#商品服务
cd ../xdclass-product-service
mvn install -Dmaven.test.skip=true dockerfile:build

docker tag xdclass-cloud/xdclass-product-service:latest registry.cn-beijing.aliyuncs.com/xdclass1024cloud/produce-server:v1.2
docker push registry.cn-beijing.aliyuncs.com/xdclass1024cloud/produce-server:v1.2
echo "商品服务构建推送成功"
#
#订单服务
cd ../xdclass-order-service
mvn install -Dmaven.test.skip=true dockerfile:build
docker tag xdclass-cloud/xdclass-order-service:latest registry.cn-beijing.aliyuncs.com/xdclass1024cloud/order-server:v1.2
docker push registry.cn-beijing.aliyuncs.com/xdclass1024cloud/order-server:v1.2
echo "订单服务构建推送成功"

#优惠券服务
cd ../xdclass-coupon-service
mvn install -Dmaven.test.skip=true dockerfile:build
docker tag xdclass-cloud/xdclass-coupon-service:latest registry.cn-beijing.aliyuncs.com/xdclass1024cloud/coupon-server:v1.2
docker push registry.cn-beijing.aliyuncs.com/xdclass1024cloud/coupon-server:v1.2
echo "优惠券服务构建推送成功"
echo "=======构建脚本执⾏完毕====="
