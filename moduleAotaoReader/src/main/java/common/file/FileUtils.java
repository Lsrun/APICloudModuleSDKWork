package common.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;

import common.CommFunAndroid;


public class FileUtils {

    private static final String TAG = "FileUtils";



    /**
     * 文件在SD卡中存储的根路径
     */

    private static final String LOG_NAME = CommFunAndroid.getDateString("yyyy_MM_dd_HH_mm_ss_SSS") + ".txt";


    /**
     * 获取文件在SD卡中存储的根路径
     *
     * @return
     */
    public static String getExternalStorageDirectoryPath() {

        String externalStorageDirectory = Environment.getExternalStorageDirectory() + "/";

        return externalStorageDirectory;

    }

    /**
     * 获取文件在SD卡中存储的根路径
     *
     * @param dir 跟路径下的自定义目录（最好是包名）
     * @return
     */
    public static String getExternalStorageDirectoryPath(String dir) {
        String externalStorageDirectory = getExternalStorageDirectoryPath() + "/" + dir + "/";

        return externalStorageDirectory;

    }

    /**
     * 获取图片文件夹Pictures在SD卡中存储的根路径
     *
     * @return path=/storage/emulated/0/Pictures/
     */
    public static String getPicturesDirectoryPath() {

        String path_1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/";

        return path_1;
    }

    /**
     * 获取包名下files的路径
     *
     * @param context
     * @return 包名/files:红米note4:/data/user/0/com.ysy15350.myutil/files
     */
    public static String getFilePath(Context context) {
        return context.getApplicationContext().getFilesDir().getPath();
    }


    public static void writeErrorLog(Throwable ex) {
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            baos = new ByteArrayOutputStream();
            printStream = new PrintStream(baos);
            ex.printStackTrace(printStream);
            byte[] data = baos.toByteArray();
            info = new String(data);
            data = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        writeLog(info, "error");

        //saveFile(info, SDCARDPATH + "/log", LOG_NAME);

    }

    /**
     * 写日志
     *
     * @param str
     */
    public static void writeJsonLog(String str) {
        writeLog(str, "json");
    }

    /**
     * 写日志
     *
     * @param str
     */
    public static void writeActivityLog(String str) {
        writeLog(str, "activity");
    }

    public static void writeLog(String str, String dir) {
        writeLog(str, dir, LOG_NAME);
    }

    public static void writeLog(String str, String dir, String fileName) {
        try {
            if (str != null) {
                saveFile(str, CommFunAndroid.getDiskCachePath() + "/aandroid_log/" + dir, fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存文件
     *
     * @param data     数据内容
     * @param path     绝对路径
     * @param fileName 文件名
     * @return true 保存成功，false 保存失败
     */
    public static boolean saveFile(String data, String path, String fileName) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(file, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(data.getBytes());
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取文件
     *
     * @param path  文件路径
     * @return   文件字符串
     */
    public static String readFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            try {
                throw new Exception("it's not a file,please check!");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        StringBuffer sb = new StringBuffer();
        try {
            FileInputStream in = new FileInputStream(file);
            byte[] b = new byte[in.available()];
            int read = in.read(b);
            while (read != -1) {
                sb.append(new String(b));
                read = in.read(b);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


    /**
     * 删除路径下的：文件和文件夹，包括当前文件夹
     *
     * @param absoluteFilePath ：绝对路径
     * @return true:删除成功，false：删除失败
     * 注意：当前路径不存在时，也返回true
     */
    public static boolean deletFile(String absoluteFilePath) {
        File file = new File(absoluteFilePath);
        try {
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                file.delete();
                return true;
            }
            if (!absoluteFilePath.endsWith(File.separator)) {
                absoluteFilePath = absoluteFilePath + File.separator;
            }
            if (file.isDirectory()) {
                if (file.listFiles().length == 0) {
                    file.delete();
                } else {
                    File[] files = file.listFiles();
                    for (File dirFile : files) {
                        deletFile(dirFile.getAbsolutePath());
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void deleteFile(File file) {
        try {
            if (!file.exists())
                return;
            if (file.isFile()) {
                file.delete();
                return;
            }
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 保存文本内容到缓存文件夹
     *
     * @param data     文本内容
     * @param dir      指定文件夹名称
     * @param fileName 文件名称
     * @return
     */
    public static boolean saveToCahe(String data, String dir, String fileName) {
        String path = CommFunAndroid.getCahePath() + dir;
        return saveFile(data, path, fileName);
    }


    /**
     * 删除指定缓存文件夹，如:crash
     *
     * @param dir crash 目录
     * @return
     */
    public static boolean deletCaheByDir(String dir) {

        String path = CommFunAndroid.getCahePath() + "/" + dir;

        Log.d(TAG, "deletCaheByDir() called with: dir = [" + dir + "] , path = [" + path + "]");

        return deletFile(path);
    }


    /**
     * 将文件保存到应用包名/files目录下
     *
     * @param data     要保存的内容
     * @param dir      保存的相对路径，不包括文件名："/myproject/function1/aa"
     * @param fileName 文件名称："1.txt"
     * @return
     */
    public static boolean saveToFile(String data, String dir, String fileName, Context context) {
        String path = getFilePath(context) + dir;
        return saveFile(data, path, fileName);
    }

    /**
     * 从应用包名/files目录下读取文件
     *
     * @param dir     ："/dd/1.txt"
     * @param context
     * @return
     */
    public static String readFromFile(String dir, Context context) {
        String path = getFilePath(context) + dir;
        return readFile(path);
    }

    public static boolean deletInFile(String dir, Context context) {
        String path = getFilePath(context) + dir;
        return deletFile(path);
    }


    /**
     * 获取文件数据
     *
     * @param pathName 文件路径
     * @return
     */
    public static byte[] getFile(String pathName) {
        File file = new File(pathName);
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;

    }

    /**
     * 计算文件大小文件大小
     *
     * @param filePath 文件路径例如：E:\\imgData\\afr\\9211496189393485.jpg
     * @return 文件大小 Kb
     */
    public static long getFileSize(String filePath) {
        long fileSize = 0l;
        FileChannel fc = null;
        try {
            File f = new File(filePath);
            if (f.exists() && f.isFile()) {
                FileInputStream fis = new FileInputStream(f);
                fc = fis.getChannel();
                fileSize = fc.size() / 1024;
                //logger.info(fileSize);
            } else {
                //logger.info("file doesn't exist or is not a file");
            }
        } catch (FileNotFoundException e) {
            //logger.error(e);
        } catch (IOException e) {
            //logger.error(e);
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    //logger.error(e);
                }
            }
        }

        return fileSize;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    public static String getCacheSizeStr() {
        try {
            String cachePath = CommFunAndroid.getCahePath();
            File cacheFile = new File(cachePath);
            long size = getDirSize(cacheFile);


            Log.d(TAG, "getCacheSizeStr() called size=" + size);

            if (size > 4100) {
                return formatFileSize(size);
            } else return "0KB";


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    /**
     * 打印文件夹所有文件信息
     *
     * @param path
     */
    public static void logDirChildFilePath(String path) {

        Log.d(TAG, "logDirChildFilePath() called with: path = [" + path + "]");


        File dir = new File(path);


        File[] files = dir.listFiles();
        if (files != null) {

            for (int i = 0; i < files.length; i++) {
                if (files[i] != null) {

                    if (files[i].isDirectory()) {
                        logDirChildFilePath(files[i].getPath());
                    } else {
                        Log.d(TAG, "logDirChildFilePath file path: " + files[i].getPath());
                    }
                }
            }

        }
    }

    /**
     * 打印缓存文件信息
     */
    public static void logCacheInfo() {
        String path = CommFunAndroid.getCahePath();
        logDirChildFilePath(path);
    }

    /**
     * 打印SharedPreferences内容
     */
    public static void logSharedPreferences() {
        String path = CommFunAndroid.getCahePath();
        String sharedPreferencesPath = path.replace("cache", "") + "/shared_prefs/sharedPreferences.xml";

        String content = FileUtils.readFile(sharedPreferencesPath);

        Log.d(TAG, "logSharedPreferences() called SharedPreferences content=" + content);
    }


}