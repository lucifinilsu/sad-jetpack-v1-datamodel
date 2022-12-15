package com.sad.jetpack.v1.datamodel.api.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Iterator;
import java.util.Map;

public class MapTraverseUtils {

    public static interface ITraverseAction<K,V>{
        void onTraversed(K k, V v) throws Exception;
    }
    public static  <K,V> void traverseGroup(Map<K,V> map, ITraverseAction<K,V>... actions){
        if (!ObjectUtils.isEmpty(map)){
            Iterator<Map.Entry<K, V>> iterator=map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<K,V> entityEntry=iterator.next();
                K k=entityEntry.getKey();
                V v=entityEntry.getValue();
                if (v!=null){
                    if (ObjectUtils.isNotEmpty(actions)){
                        for (ITraverseAction<K,V> action:actions
                        ) {
                            try {
                                action.onTraversed(k,v);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }
    }

}
