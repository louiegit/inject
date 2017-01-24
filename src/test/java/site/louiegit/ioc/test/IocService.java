package site.louiegit.ioc.test;

import site.louiegit.ioc.annotation.Bean;

/**
 * Created by tianxiang.luo on 17/1/23.
 */
@Bean("iocService")
public class IocService {

    public void sayIOC(){

        System.out.println("I am from ioc");
    }
}
