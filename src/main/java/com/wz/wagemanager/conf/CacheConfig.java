package com.wz.wagemanager.conf;

/**
 * @author WindowsTen
 * @date 2018/6/21 15:49
 * @description
 */
//@Configuration
public class CacheConfig {
    /**
     * 必须要指定这个Bean，refreshAfterWrite=5s这个配置属性才生效
     *
     * @return
     */
//    @Bean
//    public CacheLoader<Object, Object> cacheLoader() {
//
//        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
//
//            @Override
//            public Object load(Object key) throws Exception {
//                return null;
//            }
//
//            // 重写这个方法将oldValue值返回回去，进而刷新缓存
//            @Override
//            public Object reload(Object key, Object oldValue) throws Exception {
//                return oldValue;
//            }
//        };
//
//        return cacheLoader;
//    }
}
