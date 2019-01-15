package com.genius.hotfix;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashSet;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * 该修复不是及时生效需要等待应用下次重启
 *
 * @描述 热修复工具(只认后缀是dex 、 apk 、 jar 、 zip的补丁)
 */
public final class HotFix {

    private static final String DEX_SUFFIX = ".dex";
    private static final String APK_SUFFIX = ".apk";
    private static final String JAR_SUFFIX = ".jar";
    private static final String ZIP_SUFFIX = ".zip";
    public static final String DEX_DIR = "odex";
    private static final String OPTIMIZE_DEX_DIR = "optimize_dex";
    private static final int BUF_SIZE = 2048;

    private static HashSet<File> loadedDex = new HashSet<>();

    static {
        loadedDex.clear();
    }

    /**
     * 复制补丁到 data/data/包名/odex
     * @param context
     * @param dex
     * @return
     */
    public static boolean copyDex(Context context, File dex) {
        File fileDir = context.getDir(DEX_DIR, Context.MODE_PRIVATE);
        String name = dex.getName();
        String filePath = fileDir.getAbsolutePath() + File.separator + name;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(dex.getAbsolutePath());
            fos = new FileOutputStream(filePath);
            int len = 0;
            byte[] buffer = new byte[BUF_SIZE];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            Log.d("genius", "dex file wrote to private file");
            loadFixedDex(context);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 加载补丁，使用默认目录：data/data/包名/files/odex
     *
     * @param context
     */
    public static void loadFixedDex(Context context) {
        loadFixedDex(context, null);
    }

    /**
     * 加载补丁
     *
     * @param context       上下文
     * @param patchFilesDir 补丁所在目录
     */
    public static void loadFixedDex(Context context, File patchFilesDir) {
        if (context == null) {
            return;
        }
        // 遍历所有的修复dex
        File fileDir = patchFilesDir != null ? patchFilesDir : context.getDir(DEX_DIR, Context.MODE_PRIVATE);// data/data/包名/odex（这个可以任意位置）
        File[] listFiles = fileDir.listFiles();
        for (File file : listFiles) {
            if ((file.getName().endsWith(DEX_SUFFIX)
                            || file.getName().endsWith(APK_SUFFIX)
                            || file.getName().endsWith(JAR_SUFFIX)
                            || file.getName().endsWith(ZIP_SUFFIX))) {
                loadedDex.add(file);// 存入集合
            }
        }
        // dex合并之前的dex
        doDexInject(context, loadedDex);
    }

    private static void doDexInject(Context appContext, HashSet<File> loadedDex) {
        String optimizeDir = appContext.getDir(DEX_DIR, Context.MODE_PRIVATE).getAbsolutePath() + File.separator + OPTIMIZE_DEX_DIR;// data/data/包名/odex/optimize_dex（这个必须是自己程序下的目录）
        File fopt = new File(optimizeDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }
        try {
            // 1.加载应用程序的dex
            PathClassLoader pathLoader = (PathClassLoader) appContext.getClassLoader();
            for (File dex : loadedDex) {
                // 2.加载指定的修复的dex文件
                DexClassLoader dexLoader = new DexClassLoader(
                        dex.getAbsolutePath(),// 修复好的dex（补丁）所在目录
                        fopt.getAbsolutePath(),// 存放dex的解压目录（用于jar、zip、apk格式的补丁）
                        null,// 加载dex时需要的库
                        pathLoader// 父类加载器
                );
                // 3.合并
                Object dexPathList = getPathList(dexLoader);
                Object pathPathList = getPathList(pathLoader);
                Object leftDexElements = getDexElements(dexPathList);
                Object rightDexElements = getDexElements(pathPathList);
                // 合并完成
                Object dexElements = combineArray(leftDexElements, rightDexElements);
                // 重写给PathList里面的Element[] dexElements;赋值
                Object pathList = getPathList(pathLoader);// 一定要重新获取，不要用pathPathList，会报错
                setField(pathList, pathList.getClass(), "dexElements", dexElements);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射给对象中的属性重新赋值
     */
    private static void setField(Object obj, Class<?> cl, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = cl.getDeclaredField(field);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }

    /**
     * 反射得到对象中的属性值
     */
    private static Object getField(Object obj, Class<?> cl, String field) throws NoSuchFieldException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }


    /**
     * 反射得到类加载器中的pathList对象
     */
    private static Object getPathList(Object baseDexClassLoader) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    /**
     * 反射得到pathList中的dexElements
     */
    private static Object getDexElements(Object pathList) throws NoSuchFieldException, IllegalAccessException {
        return getField(pathList, pathList.getClass(), "dexElements");
    }

    /**
     * 数组合并
     */
    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> componentType = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);// 得到左数组长度（补丁数组）
        int j = Array.getLength(arrayRhs);// 得到原dex数组长度
        int k = i + j;// 得到总数组长度（补丁数组+原dex数组）
        Object result = Array.newInstance(componentType, k);// 创建一个类型为componentType，长度为k的新数组
        System.arraycopy(arrayLhs, 0, result, 0, i);
        System.arraycopy(arrayRhs, 0, result, i, j);
        return result;
    }
}