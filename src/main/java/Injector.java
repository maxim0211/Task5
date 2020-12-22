
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Класс Injector c
 * параметризированным методом inject, который принимает  в
 * качестве параметра объект любого класса и, используя
 * механизмы рефлексии осуществляет поиск полей, помеченных
 * этой аннотацией(в качестве типа поля используются некоторый
 * интерфейс), и осуществляет  инициализацию этих полей
 * экземплярами классов, которые указаны в качестве реализации
 * соответствующего интерфейса в  файле
 * настроек(properites)

 */

public class Injector {
    /** Поле , хранящее путь к настройкам */
    private String path = "src/main/resources/propereties.json";

    /**
     * Метод, принимающий в качестве параметра объект любого класса и, используя
     * механизмы рефлексии осуществляющий поиск полей, помеченных аннотацией
     * и осуществляющий  инициализацию этих полей экземплярами классов,
     * @param obj  - объект без параметров
     * @return возвращает готовый объект с  параметрами
     */
    public Object inject(Object obj) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] AllField = obj.getClass().getDeclaredFields();                //Получим все поля нужного класса

        for (int i = 0; i < AllField.length; i++) {                               //массив по всем полям класса
            if (AllField[i].isAnnotationPresent(AutoInjectable.class)) {          //проверим ли , есть ли у поля нужная аннотация
                AllField[i].setAccessible(true);                                   //получаю доступ
                String value = getValueByKey(AllField[i].getType().getName());    //получаю название класса, зная какой интерфейс указан у поля
                Constructor<?> constructor = Class.forName(value).getConstructor();
                AllField[i].set(obj, constructor.newInstance());                //внедрение зависимостей (запись экземпляров класса)
            }
        }
        return obj;                                                              //готовый объект с зависимостями
    }

    /**
     * Метод, принимающий в качестве параметра название интерфейса и
     * возврающий название класса , реализующий этот интерфейс ( из файла узнаем)
     * @param key  - ключ
     * @return возвращает название класса
     */
    private String getValueByKey(String key) throws IOException {
        String propereties = new String(Files.readAllBytes(Paths.get(path)));   //получим настройки, указанные в файле
        JSONObject jsonProps = new JSONObject(propereties);                     //создадим объект JSON
        return jsonProps.get(key).toString();                                   //по заданному типу поля, найдем класс, реализующий интерфейс
    }
}
