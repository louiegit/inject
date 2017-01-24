package site.louiegit.ioc.context;

import site.louiegit.ioc.annotation.Bean;
import site.louiegit.ioc.annotation.Inject;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;



/**
 * Created by tianxiang.luo on 17/1/23.
 */
public class ApplicationContext {

    private Map<String, String> depencies = new HashMap<String, String>();
    private Map<String, Object> beans = new HashMap<String, Object>();

    private ClassLoader classLoader = null;

    public Map<String, Object> getBeans() {
        return beans;
    }

    public void setBeans(Map<String, Object> beans) {
        this.beans = beans;
    }

    public void init(String filePath) throws Exception {
        //找到类文件
        Set<Class<?>> classes = findClassesByPackage(filePath);
        //然后初始化类
        initClasses(classes);
    }


    public Set<Class<?>> findClassesByPackage(String packageName) throws Exception {
        //http://stackoverflow.com/questions/1771679/difference-between-threads-context-class-loader-and-normal-classloader
        classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> set = new LinkedHashSet<Class<?>>();
        String packagePath = packageName.replace('.', '/');
        Enumeration<URL> dirs = classLoader.getResources(packagePath);
        URL url = dirs.nextElement();
        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
        File dir = new File(filePath);

        if (!dir.exists() || !dir.isDirectory()) {
            throw new Exception("目录不存在");
        }
        File[] classFiles = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".class");
            }
        });
        for (File file : classFiles) {
            try {
                Class<?> clsFile = classLoader.loadClass(packageName+"."+file.getName().split("\\.")[0]);
                set.add(clsFile);
            } catch (ClassNotFoundException e) {
                throw new Exception("类不存在于" + packagePath);
            }
        }
        return set;
    }


    public void initClasses(Set<Class<?>> classes) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        //初始化Bean
        for (Class<?> cla : classes) {
            Bean bean = cla.getAnnotation(Bean.class);
            String beanName =null;
            if (bean != null) {
                beanName = bean.value();
            }

            beans.put(beanName, cla.newInstance());

            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                Inject inj = field.getAnnotation(Inject.class);
                String fieldName = inj.value();
                if (beanName != null) {
                    depencies.put(beanName.concat(".").concat(fieldName), fieldName);
                }else{
                    String[] split = cla.getName().split("\\.");
                    //AppTest.IocService
                    depencies.put(split[split.length-1].concat(".").concat(fieldName),fieldName);
                }
            }
        }

        for (Map.Entry<String, String> entry : depencies.entrySet()) {
            String[] split = entry.getKey().split("\\.");
            Field field = beans.get(split[0]).getClass().getDeclaredField(split[1]);
            field.setAccessible(true);
            field.set(beans.get(split[0]),beans.get(split[1]));
        }

    }
}
