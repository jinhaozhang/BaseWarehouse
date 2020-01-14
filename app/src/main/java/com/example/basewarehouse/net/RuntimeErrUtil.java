package com.example.basewarehouse.net;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;

public class RuntimeErrUtil {
	/**
	 * 指定日志输出的根目录下文件夹名
	 */
	public static String LOG_DIR = "dingdinglog";
	/**
	 * 日志的文件名
	 */
	public final static String LOG_FILENAME = "log.txt";
	/**
	 * 运行时异常日志的最大文件大小 单位是k
	 */
	public final static int RUNTIME_ERR_LOG_FILE_SIZE = 20;

	/**
	 * 在activity里添加这个方法可以捕获activity的运行是异常，并将异常以对话框的形式显示出来，同时将日志信息写入sdcard的文件夹（
	 * LOG_DIR）的LOG_FILENAME文件中
	 * 
	 * @param context
	 */
	public static void addRuntimeErrListener(final Context context) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {// 给主线程设置一个处理运行时异常的handler

			@Override
			public void uncaughtException(Thread thread, final Throwable ex) {
				Log.e("err", "程序异常", ex);
				final StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				// 将错误信息输出到log文件
				Log2file("出错的版本号："+getVersion(context)+"\n"+sw.toString(), LOG_FILENAME, RUNTIME_ERR_LOG_FILE_SIZE);
				// 调用系统默认的异常处理器来处理
				// mDefaultHandler.uncaughtException(thread, ex);
				if (context instanceof Activity) {
					
				}else {
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(0);
					return;
				}
			}
		});
	}
	interface ErrListenner {
		void onErr();
	}
	/**
	 * 在activity里添加这个方法可以捕获activity的运行是异常，并将异常以对话框的形式显示出来，同时将日志信息写入sdcard的文件夹（
	 * LOG_DIR）的LOG_FILENAME文件中
	 * 
	 * @param context
	 */
	public static void addRuntimeErrListener(final ErrListenner errListenner) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {// 给主线程设置一个处理运行时异常的handler

			@Override
			public void uncaughtException(Thread thread, final Throwable ex) {
				Log.e("err", "程序异常", ex);
				final StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				// 将错误信息输出到log文件
				Log2file(sw.toString(), LOG_FILENAME, RUNTIME_ERR_LOG_FILE_SIZE);
				// 调用系统默认的异常处理器来处理
				// mDefaultHandler.uncaughtException(thread, ex);
				new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						errListenner.onErr();
						Looper.loop();
					}
				}.start();
			}
		});
	}
	/**
	 * 在Application里添加这个方法可以捕获该项目的所有的运行时异常，同时将日志信息写入sdcard的文件夹（
	 * LOG_DIR）的LOG_FILENAME文件中，当addRuntimeErrListener(final Context
	 * context)被调用时候，该方法被屏蔽掉。
	 * 
	 * @param application
	 */
	public static void addRuntimeErrListener() {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {// 给主线程设置一个处理运行时异常的handler

			@Override
			public void uncaughtException(Thread thread, final Throwable ex) {
				Log.e("err", "程序异常", ex);
				final StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				// 将错误信息输出到log文件
				Log2file(sw.toString(), LOG_FILENAME, RUNTIME_ERR_LOG_FILE_SIZE);
				// 调用系统默认的异常处理器来处理
				// mDefaultHandler.uncaughtException(thread, ex);
				// new Thread() {
				// @Override
				// public void run() {
				// Looper.prepare();
				// // 这里可以处理异常后的操作
				// Looper.loop();
				// }
				// }.start();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		});
	}

	/**
	 * 将日志信息写入文件
	 * 
	 * @param logString
	 *            日志信息
	 * @param logFileName
	 *            要写入的文件名 文件存放于SDcard/指定的日志目录下
	 *            <p>
	 * @param fileSize
	 *            指定日志文件的最大大小，如果超于这个值，文件将清空，单位是k
	 *            <p>
	 *            例子：Log2file(sw.toString(),"log.txt",20);
	 */
	public static void Log2file(String logString, String logFileName, int fileSize) {
		try {
			File AppRootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LOG_DIR);
			if (!AppRootDir.exists()) {
				AppRootDir.mkdir();
			}
			File file = new File(AppRootDir + "/" + logFileName);
			if (!file.exists()) {
				file.createNewFile();
			} else if (file.length() > fileSize * 1024) {
				file.delete();
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file, true);
			fw.flush();
			fw.append("\n**********************************\n");
			fw.append(getTime() + "\n");
			fw.append(logString);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取运行时异常产生的日志的内容
	 * 
	 * @param logFileName
	 * @return
	 */

	public static String getLog(String logFileName) {
		File AppRootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LOG_DIR);
		if (!AppRootDir.exists()) {
			return null;
		}
		File file = new File(AppRootDir + "/" + logFileName);
		if (!file.exists()) {
			return null;
		}
		return readTxtFile(file.toString());
	}

	/**
	 * 根据路径读取文本内容
	 * 
	 * @param strFilePath
	 * @return
	 */
	public static String readTxtFile(String strFilePath) {
		String path = strFilePath;
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(path);
		try {
			InputStream instream = new FileInputStream(file);
			if (instream != null) {
				InputStreamReader inputreader = new InputStreamReader(instream);
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				// 分行读取
				while ((line = buffreader.readLine()) != null) {
					content += line + "\n";
				}
				instream.close();
			}
		} catch (java.io.FileNotFoundException e) {
			Log.w("TestFile", "The File doesn't not exist.");
		} catch (IOException e) {
			Log.w("TestFile", e.getMessage());
		}
		return content;
	}

	/**
	 * 该方法用于获取手机的系统时间
	 * 
	 */

	private static String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	
	/**
	 * 
	 * 清除制定日志文件
	 * @param logFileName
	 * @return
	 */

	public static void ClearLog(String logFileName) {
		File AppRootDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + LOG_DIR);
		if (!AppRootDir.exists()) {
			return ;
		}
		File file = new File(AppRootDir + "/" + logFileName);
		if (!file.exists()) {
			return ;
		}else {
			file.delete();
		}
		
	}
	public static String getVersion(Context context)// 获取版本号
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
