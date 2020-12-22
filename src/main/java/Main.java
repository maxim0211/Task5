import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SomeBean sb = new SomeBean();
        SomeBean newSomeBean = (SomeBean) new Injector().inject(sb);
        newSomeBean.foo();
    }
}
