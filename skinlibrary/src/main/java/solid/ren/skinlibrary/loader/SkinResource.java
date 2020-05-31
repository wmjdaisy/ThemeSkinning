package solid.ren.skinlibrary.loader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import dalvik.system.DexClassLoader;

/**
 * 支持换肤的Resources类
 * Created by hxd on 2015/9/11.
 */
public class SkinResource extends Resources {

    SkinManager skinManager;
    Resources originResources;
    String skinPackageName;
    DexClassLoader classLoader;
    Map<String, Integer> mResourceMap = new HashMap<>();
    Map<String, Class> mClassMap = new HashMap<>();
    Map<String, Field> mFieldMap = new HashMap<>();
    Map<String, String> mColorsMap;

    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when
     *                selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     */
    public SkinResource(SkinManager skinManager, AssetManager assets, DisplayMetrics metrics,
                         Configuration config, String packageName) {
        super(assets, metrics, config);
        this.skinManager = skinManager;
        this.originResources = skinManager.getContext().getResources();
        this.skinPackageName = packageName;
    }
    /**
     * @param typeName  type
     * @param entryName entryName
     * @return id
     * @throws NotFoundException
    int obtainSkinId(String typeName, String entryName, int originId) throws NotFoundException {
        if (originId > 0) {
            entryName = originResources.getResourceEntryName(originId);
            typeName = originResources.getResourceTypeName(originId);
        }
        return obtainSkinId(typeName, entryName);
    }
    int obtainSkinIdForColor(String typeName, String entryName, int originId) throws NotFoundException {
        if (originId > 0) {
            entryName = originResources.getResourceEntryName(originId);
            typeName = originResources.getResourceTypeName(originId);
        }
        if(typeName.contains("drawable"))
            return -1;
        return obtainSkinId(typeName, entryName);
    }

    public synchronized Map<String, String> getColorsMap(){
        if(mColorsMap != null){
            return mColorsMap;
        }
        mColorsMap = new HashMap<>();
        try {
            if (classLoader != null) {
                try {
                    String clazzName = skinPackageName + ".R$color";

                    Class resClazz = mClassMap.get(clazzName);
                    if (resClazz == null) {
                        resClazz = classLoader.loadClass(clazzName);
                        mClassMap.put(clazzName, resClazz);
                    }

                    if(resClazz != null) {
                        Field[] fields = resClazz.getFields();
                        for (Field field : fields) {
                            String colorName = field.getName();
                            Integer color = field.getInt(null);
                            mColorsMap.put(colorName, "#" + Integer.toHexString(getColor(color)));
                        }
                    }
                } catch (Exception e) {
                    // nothing
                }

            }else{
                String clazzName = skinPackageName + ".R$color";
                Class resClazz = mClassMap.get(clazzName);
                if (resClazz == null) {
                    try {
                        resClazz = Class.forName(clazzName);//classLoader.loadClass(clazzName);
                        mClassMap.put(clazzName, resClazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }


                if(resClazz != null) {
                    Field[] fields = resClazz.getFields();
                    for (Field field : fields) {
                        String colorName = field.getName();
                        Integer color = field.getInt(null);
                        mColorsMap.put(colorName, "#" + Integer.toHexString(getColor(color)));
                    }
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return mColorsMap;
    }

    int obtainSkinId(String typeName, String entryName) throws NotFoundException {
        int newId = 0;
        try {
            if (classLoader != null) {
                try {
                    String clazzName = skinPackageName + ".R$" + typeName;
                    String key = clazzName + "." + entryName;
                    if (mResourceMap.get(key) != null) {
                        return mResourceMap.get(key);
                    }

                    Class resClazz = mClassMap.get(clazzName);
                    if (resClazz == null) {
                        resClazz = classLoader.loadClass(clazzName);
                        mClassMap.put(clazzName, resClazz);
                    }

                    Field field = mFieldMap.get(entryName);
                    if (field == null) {
                        field = resClazz.getDeclaredField(entryName);
                        mFieldMap.put(entryName, field);
                    }
                    newId = field.getInt(null);
                    mResourceMap.put(key, newId);
                } catch (Exception e) {
                    // nothing
                }

            }else{
                String clazzName = skinPackageName + ".R$" + typeName;
                String key = clazzName + "." + entryName;
                if (mResourceMap.get(key) != null) {
                    return mResourceMap.get(key);
                }

                Class resClazz = mClassMap.get(clazzName);
                if (resClazz == null) {
                    try {
                        resClazz = Class.forName(clazzName);//classLoader.loadClass(clazzName);
                        mClassMap.put(clazzName, resClazz);
                    } catch (ClassNotFoundException e) {
                        //e.printStackTrace();
                    }

                }

                Field field = mFieldMap.get(entryName);
                if (field == null && resClazz!=null) {
                    try {
                        field = resClazz.getDeclaredField(entryName);
                        mFieldMap.put(entryName, field);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    if(field!=null)
                        newId = field.getInt(null);
                    mResourceMap.put(key, newId);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }catch (Exception ex){
            //ex.printStackTrace();
        }

        return newId;
    }


    @Override
    public String getResourceEntryName(int resid) throws NotFoundException {
        return originResources.getResourceEntryName(resid);
    }

    @Override
    public String getResourceTypeName(int resid) throws NotFoundException {
        return originResources.getResourceTypeName(resid);
    }

    public void setClassLoader(DexClassLoader classLoader) {
        this.classLoader = classLoader;
    }*/
   /* @Override
    public String getResourceEntryName(int resid) throws NotFoundException {
        return skinManager.getContext().getResources().getResourceEntryName(resid);
    }

    @Override
    public String getResourceTypeName(int resid) throws NotFoundException {
        return skinManager.getContext().getResources().getResourceTypeName(resid);
    }*/
}
