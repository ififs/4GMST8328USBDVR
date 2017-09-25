package com.syu.dvr.upgrade;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.syu.dvr.utils.LogCatUtils;
import com.syu.dvr.widget.DownLoadLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.StatFs;
import android.provider.CallLog.Calls;
import android.widget.Toast;

@SuppressLint("NewApi") public class OkhttpDownLoadManage {
	private static OkhttpDownLoadManage manage;
	private OkHttpClient mOkHttpClient;
	private DownLoadLayout layout;
	private long contentLength;
	private List<Call>mCalls;
	private boolean downLoad=false;
	/**
	 * 1 存储空间不足
	 * -1下载失败
	 * 0 下载成功
	 * */
	private void sendFailedStringCallback(int type) {
		layout.downLoadSuccess(false,type,"");
		stopTimer();
    }

    private void sendSuccessResultCallback(String mFile) {
    	LogCatUtils.showString("====成功==");
    	layout.downLoadSuccess(true,0,mFile);
    	stopTimer();
    }


    private OkhttpDownLoadManage() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mCalls=new ArrayList<Call>();
    }
    public static OkhttpDownLoadManage getInstance() {
        if (manage == null) {
            synchronized (OkhttpDownLoadManage.class) {
                if (manage == null) {
                    manage = new OkhttpDownLoadManage();
                    
                }
            }
        }
        return manage;
    }
	public void downloadAsyn(final String url, final String destFileDir, final String name,
			final long startPoints,final DownLoadLayout layout) {
		
		LogCatUtils.showString("===url=="+url);
		this.layout=layout;
		if (downLoad) {
			LogCatUtils.showString("===正在下载==");
			layout.setContentLength(contentLength);
			return;
		}
		downLoad=true;
        final Request request = new Request.Builder().url(url).header("RANGE", "bytes=" + startPoints + "-")
        		.build();
        LogCatUtils.showString("====startPoints=="+startPoints);
        final Call call = mOkHttpClient.newCall(request);
        File file=new File(destFileDir);
        if (!file.exists())file.mkdirs();
        mCalls.add(call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(-1);
            }
            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                BufferedOutputStream outputStream = null;
                FileChannel channel=null;
                RandomAccessFile mAccessFile=null;
                try {
                	File file = new File(destFileDir, name+"_temp");                	
                    is = response.body().byteStream();
                    contentLength=response.body().contentLength()+startPoints;
                    if (contentLength==0) {
						sendFailedStringCallback(-1);
					}
                    if (!getSDAvailableSpace(contentLength)) {
                    	sendFailedStringCallback(1);
						return;
					}
                    layout.setContentLength(contentLength);
                    LogCatUtils.showString("===contentLength()=="+contentLength);
                    outputStream = new BufferedOutputStream(new FileOutputStream(file, true));
                    RunTask(destFileDir, name);

                    while ((len = is.read(buf)) != -1){
                    	outputStream.write(buf, 0, len);
                    }
                    thempFileToTargetFile(destFileDir,name);
                    
                } catch (IOException e) {
                	e.printStackTrace();
                    sendFailedStringCallback(-1);
                } finally {
                    try
                    {
                    	if (outputStream != null) outputStream.close();
                        if (is != null) is.close();
                        if (mAccessFile!=null) {
							mAccessFile.close();
						}
                        if (channel!=null) {
							channel.close();
						}
                    } catch (IOException e) {
                    }
                    
                }
            }
        });
    }
	public void removeUpLayout(){
		this.layout=null;
	}
    public void getAsString(String url,final NetworkCheckCallback callback) throws IOException{
    	final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        mCalls.add(call);
        call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Response response) throws IOException {
				callback.networkcallback(response.body().string());
			}
			@Override
			public void onFailure(Request request, IOException arg1) {
				callback.networkcallback("failure");
				LogCatUtils.showString("failure");
			}
		});
    }
    public void cancelWork(){
    	if (mCalls!=null&&mCalls.size()>0) {
			for (int i = 0; i < mCalls.size(); i++) {
				mCalls.get(i).cancel();
			}
		}
    	mCalls.clear();
    	stopTimer();
    }
    private void thempFileToTargetFile(String dir,String name) {
        File tempFile=new File(dir+name+"_temp");
        if (!tempFile.exists())return;
        if (tempFile.length()<=0){
            tempFile.delete();
            return;
        }
        tempFile.renameTo(new File(dir+name));
        sendSuccessResultCallback(dir+name);
    }
    class MyTimerTask extends TimerTask {
        private String dir;String name;
        public MyTimerTask(String dir, String name) {
            this.dir=dir;this.name=name;
        }
        @Override
        public void run() {
            File file=new File(dir+name+"_temp");
            if (file.exists()) {
                
                if (layout!=null) {
                	layout.setmDownLoadProgressBar(file.length());
				}
            }
        }

    }
    private MyTimerTask task;
    private Timer timer;
    private int mCount=0;
    private void RunTask(String dir,String name) {
        timer=new Timer();
        if (task!=null) {
            task.cancel();
        }
        task=new MyTimerTask(dir,name);
        timer.schedule(task, 1, 100);
    }
    private void stopTimer() {
        if (task!=null) {
            task.cancel();
            task=null;
        }
        mCount=0;
        if (timer!=null) {
			timer.cancel();
			timer.purge();
		}
        downLoad=false;
    }
    @SuppressLint("NewApi") private boolean getSDAvailableSpace(long value) {
		boolean  boolValue=true;
		StatFs mFs = new StatFs("/sdcard");
		long blocSize = mFs.getBlockSizeLong();
		long availBlock = mFs.getAvailableBlocksLong();
		long mSpaceSize=(blocSize*availBlock-value)/1024/1024;
		LogCatUtils.showString("==mSpaceSize=="+mSpaceSize);
		if (mSpaceSize<200) {
			boolValue=false;
		}
		return boolValue;
	}
    
    public boolean isDownLoad() {
		return downLoad;
	}

	public void setDownLoad(boolean downLoad) {
		this.downLoad = downLoad;
	}
	public interface NetworkCheckCallback{
		public void networkcallback(String message);
	}
}
