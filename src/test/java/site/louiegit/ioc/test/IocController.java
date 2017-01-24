package site.louiegit.ioc.test;

import site.louiegit.ioc.annotation.Bean;
import site.louiegit.ioc.annotation.Inject;

/**
 * Created by tianxiang.luo on 17/1/23.
 */
@Bean("iocController")
public class IocController {


    @Inject(value = "iocService")
    private IocService iocService;

    public void callService(){
        iocService.sayIOC();
    }
}
