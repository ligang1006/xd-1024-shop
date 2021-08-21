package net.gaven.util.yonyou;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lee
 * @Date 2021/8/20 21:01
 */
public class BeanTest {


    public static  <T,V> List<T> copyBean(List<V> sources,Class<T> target) throws IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();

        sources.forEach(e -> {
            try {
                T t = target.newInstance();
                BeanUtils.copyProperties(e,t);
                result.add(t);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }

        });

        return result;

    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        List<A> aList = new ArrayList<>();
        A a = new A();
        a.setA("11");
        A a1 = new A();
        a1.setA("22");
        aList.add(a);
        aList.add(a1);
        List<B> b = copyBean(aList,B.class);
        System.out.println(b);

        aList.stream().map(e -> BeanTest.copy(e,new B())).collect(Collectors.toList());



    }

    public static <T> T copy(Object source,T t) {
         BeanUtils.copyProperties(source,t);
         return t;
    }
    static class  A{

        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    static class B{
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }


}
