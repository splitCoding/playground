package split.nplus1.cache;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;

public class CacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(final Object target, final Method method, final Object... params) {
        return method.getName() + "-" + params.length;
    }
}
