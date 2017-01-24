package site.louiegit.ioc;

import org.junit.Test;
import site.louiegit.ioc.context.ApplicationContext;
import site.louiegit.ioc.test.IocController;

/**
 * Created by tianxiang.luo on 17/1/23.
 */

public class AppTest {


    @Test
    public void testIOC() throws Exception {

        ApplicationContext context = new ApplicationContext();
        context.init("site.louiegit.ioc.test");

        IocController controller = (IocController) context.getBeans().get("iocController");
        controller.callService();

    }
}
