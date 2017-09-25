package com.syu.dvr.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syu.dvr.R;
import com.syu.dvr.TheApp;
import com.syu.dvr.control.FileManager;

public class PublicClass {
	
	public  static PublicClass publicClass;
	public static PublicClass getInstance(){
		if (publicClass==null) {
			synchronized (PublicClass.class) {
				if (publicClass==null) {
					publicClass=new PublicClass();
				}
			}
		}
		return publicClass;
	}
	public void installSlient(File file) {
		if (!file.exists()||!file.getName().endsWith(".apk")) {
			return ;
		}
        String cmd = "pm install -r "+file.getAbsolutePath();
        Process process = null;
        DataOutputStream os = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        try {
            //静默安装需要root权限
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.writeBytes("exit\n");
            os.flush();
            //执行命令
            process.waitFor();
            //获取返回结果
            successMsg = new StringBuilder();
            errorMsg = new StringBuilder();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //显示结果
        TheApp.mApp.MyTosat("安装完成");
    }
	public void openFile(File file) {
		if (file.exists()&&file.getName().endsWith(".apk")) {
	        Intent intent = new Intent();
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setAction(android.content.Intent.ACTION_VIEW);
	        intent.setDataAndType(Uri.fromFile(file),
	                        "application/vnd.android.package-archive");
	        TheApp.mApp.startActivity(intent);
        }
	}
	public boolean networkAvailable(Context context) {
        try {
        	if (context==null) {
				return false;
			}
            //得到网络连接信息
           ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                // 获取NetworkInfo对象
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                //去进行判断网络是否连接
                if (networkInfo != null &&networkInfo.isAvailable()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	public  boolean isadd=false;
	
	public  final String RECORDING_HINT = "recording-hint";
    public  final String TRUE = "true";
    public  final String FALSE = "false";
    
    
    private  boolean  isWrite=false;
    
    public   Intent intentService;
   
    public  Point getDefaultDisplaySize(Point size) {
        TheApp.mApp.activity.getWindowManager().getDefaultDisplay().getSize(size);
        
        return size;
    }
    public Rect getDefaultDisplaySize() {
    	int [] point =getWindowManeger();
    	if (point==null||point.length>0) {
			return null;
		}
        return new Rect(0, 0, point[0], point[1]);
    }


	public  void StartNotficationService(String action){
		intentService=new Intent();
		intentService.setAction("android.com.syu.dvr.action."+action);
		TheApp.mApp.startService(intentService);
		
	}
	
    public void clearCacheDir(){//清缓存
    	File mCacheDir=TheApp.mApp.getCacheDir();
    	if (mCacheDir!=null&&mCacheDir.exists()&&mCacheDir.isDirectory()) {
			for (File item:mCacheDir.listFiles()) {
				if (item!=null) {
					item.delete();
				}
			}
		}
    }
   
    public boolean isAnableCamera(){//camera的使用不能处在acc或者不经服务允许下

		return !Config.isCanOpenCamera();
	}
	public  Dialog getDialog(Context context,int souceID){
    	Dialog adddalog;
    	adddalog=new Dialog(context);
		adddalog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		adddalog.setContentView(R.layout.layout_progress);
		Window dWindow=adddalog.getWindow();
		WindowManager.LayoutParams lParams=dWindow.getAttributes();
		
		lParams.width=250;
		lParams.height=160;
		dWindow.setAttributes(lParams);
		TextView mTextView=(TextView) adddalog.findViewById(R.id.text_title);
		mTextView.setText(souceID);
		adddalog.show();
    	return adddalog;
    }
	public boolean recordStatuToast(RecordingStatus status){
		if (status==null) {
			return false;
		}
		if (status.getmScardStatus()==0) {
			return true;
		}
		showStatuToast(status.getmScardStatus());
		return false;
	}
	public void showStatuToast(int status){
		handler.sendEmptyMessage(status);
	}
	private Handler handler=new Handler(Looper.getMainLooper()){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				TheApp.mApp.MyTosat(R.string.str_load_success);
				break;
			case 1:
				TheApp.mApp.MyTosat(R.string.str_tip_no_sdcard);
				break;
			case 2:
				TheApp.mApp.MyTosat(R.string.str_low_speed_card);
				break;
			case 3:
				TheApp.mApp.MyTosat(R.string.str_load_sysfile);
				break;
			case 4:
				TheApp.mApp.MyTosat(R.string.str_file_islock_video);
				break;
			default:
				break;
			}
			
			
		};
	};
	
	public int addTime=0;
	public int clickTiem=0;
	public int outTime=0;
	public int handleTiemSMH=0;
	public int handleTiemSS=0;
	public int getSystemTimeSMH=0;
	public int getSystemTimeSS=0;
	
	public boolean isCheckeEnable(){
    	boolean isCheckeEnable=false;
    	String tempTime=FileManager.getHandleTime();
    	getSystemTimeSMH=Integer.valueOf(tempTime.substring(0, 6));
    	getSystemTimeSS=Integer.valueOf(tempTime.substring(6));
    	if (((Math.abs(getSystemTimeSMH-handleTiemSMH)*1000+getSystemTimeSS)-handleTiemSS)>1000) {
    		handleTiemSMH=getSystemTimeSMH;
    		handleTiemSS=getSystemTimeSS;
			isCheckeEnable= true;
		}else {
			isCheckeEnable=false;
		}
    	return isCheckeEnable;
    }
	
	public  boolean isSupported(String value, List<String> supported) {
        return supported == null ? false : supported.indexOf(value) >= 0;
    }
	
	public  int readExposure() {
        String exposure = TheApp.mApp.getString(
                Config.KEY_EXPOSURE,
                Config.EXPOSURE_DEFAULT_VALUE);
        try {
            return Integer.parseInt(exposure);
        } catch (Exception ex) {
        }
        return 0;
    }
	public boolean isVerticalScreen (){
		/*int [] size=getWindowManeger();
		if (size!=null&&(size.length==2)&&(size[0]==768)&&(size[1]==675)) {
			return true;
		}*/
		return  TheApp.mApp.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT;
	}
	//获取屏幕的高度和宽度
	public  int [] getWindowManeger(){
		int [] array=new int[2];
		WindowManager w=(WindowManager) TheApp.mApp.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics=new DisplayMetrics();
		w.getDefaultDisplay().getMetrics(metrics);
		/*LogCatUtils.showString("屏幕的宽度＝＝＝"+metrics.widthPixels);
		LogCatUtils.showString("屏幕的高度＝＝＝"+metrics.heightPixels);*/
		array[0]=metrics.widthPixels;
		array[1]=metrics.heightPixels;
		return array;
	}
	
	public  int[] getMaxPreviewFpsRange(Parameters params) {
        @SuppressWarnings("deprecation")
		List<int[]> frameRates = params.getSupportedPreviewFpsRange();
        if (frameRates != null && frameRates.size() > 0) {
            // The list is sorted. Return the last element.
            return frameRates.get(frameRates.size() - 1);
        }
        return new int[0];
    }
	 public  Size getOptimalVideoSnapshotPictureSize(
	            List<Size> sizes, double targetRatio) {
	        // Use a very small tolerance because we want an exact match.
	        final double ASPECT_TOLERANCE = 0.001;
	        if (sizes == null) return null;

	        Size optimalSize = null;

	        // Try to find a size matches aspect ratio and has the largest width
	        for (Size size : sizes) {
	            double ratio = (double) size.width / size.height;
	            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	            if (optimalSize == null || size.width > optimalSize.width) {
	                optimalSize = size;
	            }
	        }

	        // Cannot find one that matches the aspect ratio. This should not happen.
	        // Ignore the requirement.
	        if (optimalSize == null) {
	            for (Size size : sizes) {
	                if (optimalSize == null || size.width > optimalSize.width) {
	                    optimalSize = size;
	                }
	            }
	        }
	        return optimalSize;
	    }
	 public  int[]getSupporPreviewSize(Parameters mParameters){
		 int []mSupporPreview = new int[2];
		 
		 List<Camera.Size> sizes = mParameters.getSupportedPreviewSizes();
         int[] a = new int[sizes.size()];
         int[] b = new int[sizes.size()];
         for (int i = 0; i < sizes.size(); i++) {
             int supportH = sizes.get(i).height;
             int supportW = sizes.get(i).width;
             LogCatUtils.showString("==supportW=="+supportW);
             LogCatUtils.showString("==supportH=="+supportH);
             
             a[i] = Math.abs(supportW - getWindowManeger()[0]);
             b[i] = Math.abs(supportH - getWindowManeger()[1]);
         }
         int minW=0,minA=a[0];
         for( int i=0; i<a.length; i++){
             if(a[i]<=minA){
                 minW=i;
                 minA=a[i];
             }
         }
         int minH=0,minB=b[0];
         for( int i=0; i<b.length; i++){
             if(b[i]<=minB){
                 minH=i;
                 minB=b[i];
             }
         }
         mSupporPreview[0]=sizes.get(minW).width;//这里要注意
         mSupporPreview[1]=sizes.get(minH).height;
		
		 return mSupporPreview;
	 }
	 private boolean isNotips;
	 private Dialog mFormatsdDialog;
	 public  Dialog NoCameraWarning(int flag){
			if (mFormatsdDialog!=null) {
				mFormatsdDialog.cancel();
			}
			isNotips=false;
	    	int []size;
	    	size=getWindowManeger();
	    	if (size==null||size.length!=2) {
	    		size=new int[]{1024,600};
			}
			if (mFormatsdDialog==null) {
				ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(size[0]/2, size[1]/2);
				View view =LinearLayout.inflate(TheApp.mApp, R.layout.format_sd_layout, null);
				mFormatsdDialog=new Dialog(TheApp.mApp,R.style.add_dialog);
				mFormatsdDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				mFormatsdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				mFormatsdDialog.setContentView(view,params);
			}
			TextView cancle=(TextView) mFormatsdDialog.findViewById(R.id.dialog_cancle);
			TextView message=(TextView) mFormatsdDialog.findViewById(R.id.dialog_messege);
			TextView mTittle=(TextView) mFormatsdDialog.findViewById(R.id.dialog_tittle);
			message.setMovementMethod(ScrollingMovementMethod.getInstance());
			cancle.setVisibility(View.GONE);
			message.setTextColor(TheApp.mApp.getResources().getColor(R.color.red));
			if (flag==1) {
				mTittle.setText(R.string.usb_low);
				message.setText(R.string.switch_usb);
				message.setTextColor(TheApp.mApp.getResources().getColor(R.color.white));
			}else if(flag==2){
				int ID=SystemProperties.getInt("ro.build.fytmanufacturer",-1);
				String temp="";
				if ((HandleConstant.MANUFACTURER_CONTACT_METHOD.length>ID)&&(ID!=-1)) {
					temp=HandleConstant.MANUFACTURER_CONTACT_METHOD[ID];
				}
				String string=TheApp.mApp.getString(R.string.purchasea_dvice);
				string=string.replace("@#", temp);
				message.setText(string);
				mTittle.setText(R.string.no_camera);
			}else if (flag==3) {
				mTittle.setText(R.string.i2c_error);
				message.setText(R.string.no_supplier);
				message.setTextColor(TheApp.mApp.getResources().getColor(R.color.red));
			}else if (flag==4) {
				mTittle.setText(R.string.system_is_low);
				message.setText(R.string.system_low_message);
				message.setTextColor(TheApp.mApp.getResources().getColor(R.color.white));
			}else if (flag==5) {
				mTittle.setText(R.string.dvr_unavailable);
				message.setTextColor(TheApp.mApp.getResources().getColor(R.color.red));
				message.setText(R.string.check_no_fyt_dvr);
			}else if (flag==6) {
//				layout.setVisibility(View.VISIBLE);
				cancle.setVisibility(View.GONE);
				mTittle.setText(R.string.str_tip_title);
				message.setTextColor(TheApp.mApp.getResources().getColor(R.color.white));
				message.setText(R.string.camera_loading_prompt);
			}
			mFormatsdDialog.findViewById(R.id.dialog_sure).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mFormatsdDialog.dismiss();
					if (isNotips) {
						TheApp.mApp.saveBoolean("com.syu.dvr.no.tips", true);
					}
				}
			});
			mFormatsdDialog.setCanceledOnTouchOutside(false);
			return mFormatsdDialog;
		}
	 public  Size getOptimalPreviewSize(Point surfaceViewSize,
	            List<Size> sizes, double targetRatio) {
	        if (surfaceViewSize == null) return null;
	        Point[] points = new Point[sizes.size()];
	        int index = 0;
	        for (Size s : sizes) {
	            points[index++] = new Point(s.width, s.height);
	        }

	        int optimalPickIndex = getOptimalPreviewSize(surfaceViewSize, points, targetRatio);
	        return (optimalPickIndex == -1) ? null : sizes.get(optimalPickIndex);
	    }
	 public  int getOptimalPreviewSize(Point surfaceViewSize,
	            Point[] sizes, double targetRatio) {
	        // Use a very small tolerance because we want an exact match.
	        final double ASPECT_TOLERANCE = 0.01;
	        if (sizes == null) return -1;

	        int optimalSizeIndex = -1;
	        double minDiff = Double.MAX_VALUE;

	        // Because of bugs of overlay and layout, we sometimes will try to
	        // layout the viewfinder in the portrait orientation and thus get the
	        // wrong size of preview surface. When we change the preview size, the
	        // new overlay will be created before the old one closed, which causes
	        // an exception. For now, just get the screen size.
	        Point point = surfaceViewSize;
	        int targetHeight = Math.min(point.x, point.y);
	        // Try to find an size match aspect ratio and size
	        for (int i = 0; i < sizes.length; i++) {
	            Point size = sizes[i];
	            double ratio = (double) size.x / size.y;
	            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	            if (Math.abs(size.y - targetHeight) < minDiff) {
	                optimalSizeIndex = i;
	                minDiff = Math.abs(size.y - targetHeight);
	            }
	        }
	        // Cannot find the one match the aspect ratio. This should not happen.
	        // Ignore the requirement.
	        if (optimalSizeIndex == -1) {
	            minDiff = Double.MAX_VALUE;
	            for (int i = 0; i < sizes.length; i++) {
	                Point size = sizes[i];
	                if (Math.abs(size.y - targetHeight) < minDiff) {
	                    optimalSizeIndex = i;
	                    minDiff = Math.abs(size.y - targetHeight);
	                }
	            }
	        }
	        return optimalSizeIndex;
	    }
	 
	 // Check for supported quality
	 public  int getSupportedVideoQuality(int cameraId) {
		 
	       int usppor=TheApp.mApp.getInt(Config.CAMERE_PROFIEL, 1);//默认为 720P
	       int returnValeu=5;
	       if (cameraId==1) {
			usppor= 1;//后录制默认480
	       }
	       switch (usppor) {
		case 0:
			if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
				
	            returnValeu=6;
	        }else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
				returnValeu=5;
			}else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
				
			}
			break;
		case 1:
			
			if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
				returnValeu=5;
			}else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
	            returnValeu=6;
	        }else  if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
				returnValeu=4;
			}
			break;
		case 2:
			if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
				returnValeu = 4;
			}else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
	            returnValeu=6;
	        }else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
				returnValeu=5;
			}
			break;
		default:
			break;
		}
	        
	       return returnValeu;
	    }
	 
}
