package solid.ren.skinlibrary.utils;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import solid.ren.skinlibrary.loader.SkinManager;
import solid.ren.skinlibrary.loader.SkinResource;

/**
 * Created by _SOLID
 * Date:2016/12/27
 * Time:17:44
 * Desc:兼容处理类
 */

public class ResourcesCompat {

    public static Resources getResources(SkinManager skinManager, AssetManager assetManager,
                                         DisplayMetrics displayMetrics,
                                         Configuration configuration, String packageName) {
        Resources SkinResource= null;
        try {
            SkinResource = new SkinResource(skinManager,assetManager, displayMetrics, configuration,packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SkinResource;
    }
}
