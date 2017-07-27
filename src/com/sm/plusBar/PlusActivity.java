package com.sm.plusBar;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



import com.sm.Zxing.Demo.CaptureActivity;
import com.sm.plusBar.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


public class PlusActivity extends Activity implements View.OnClickListener, View.OnTouchListener {
		private View mPanelView;
	    private View mCloseButton;
	   // private View mIdeaButton;
	   // private View mPhotoButton;
	   // private View mWeiboButton;
	    private View mLbsButton;
	    private View scan_btn;
	   // private View mReviewButton;
	   // private View mMoreButton;
	  //  private View panel;
	    private Animation mButtonInAnimation;
	    private Animation mButtonOutAnimation;
	    private Animation mButtonScaleLargeAnimation;
	    private Animation mButtonScaleSmallAnimation;
	    private Animation mCloseRotateAnimation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_add);
		
		initView();
        initAnimation();
        openPanelView();// 添加按钮
	}

	private void initView() {
        //View addButton = findViewById(R.id.add);
        mPanelView = findViewById(R.id.panel);
        mPanelView.getBackground().setAlpha(0);
        mCloseButton = findViewById(R.id.close);
        mLbsButton = findViewById(R.id.lbs_btn);
        scan_btn = findViewById(R.id.scan_btn );
       // panel = findViewById(R.id.panel);
       // addButton.setOnClickListener(this);
        mPanelView.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
        scan_btn.setOnClickListener(this);
        mLbsButton.setOnTouchListener(this);
        mLbsButton.setOnClickListener(this);
        scan_btn.setOnTouchListener(this);
    }
	
	
	// 初始化动画
    private void initAnimation() {
        mButtonInAnimation = AnimationUtils.loadAnimation(this, R.anim.button_in);
        mButtonOutAnimation = AnimationUtils.loadAnimation(this, R.anim.button_out);
        mButtonScaleLargeAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_to_large);
        mButtonScaleSmallAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_to_small);
        mCloseRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.close_rotate);

        mButtonOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 6个按钮的退出动画执行完毕后，将面板隐藏
                mPanelView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    // 打开面板视图
    private void openPanelView() {
        mPanelView.setVisibility(View.VISIBLE);

       
        mLbsButton.startAnimation(mButtonInAnimation);
        scan_btn.startAnimation(mButtonInAnimation);
        mCloseButton.startAnimation(mCloseRotateAnimation);
    }

    // 关闭面板视图
    private void closePanelView() {
       
        mLbsButton.startAnimation(mButtonOutAnimation);
        scan_btn.startAnimation(mButtonOutAnimation);
        mCloseButton.startAnimation(mCloseRotateAnimation);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (mPanelView.getVisibility() == View.VISIBLE) {
            closePanelView();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下，按钮执行放大动画
                v.startAnimation(mButtonScaleLargeAnimation);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 手指移开，按钮执行缩小动画
                v.startAnimation(mButtonScaleSmallAnimation);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 缩小动画执行完毕后，将按钮的动画清除。这里的150毫秒是缩小动画的执行时间。
                        v.clearAnimation();
                    }
                }, 150);
            
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        /*switch(v.getId())
        {
        	case R.id.add:
        		 openPanelView();// 添加按钮
        		break;
        	case R.id.close:
        		closePanelView();
        		break;
        	case R.id.lbs_btn:
        		
        		break;
        	case R.id.scan_btn:
        		
        		break;
        	case R.id.panel:
        		closePanelView();
        		break;
        	default:
        		break;

        }*/
    	if(v.getId() ==R.id.add)
    	{
    		 openPanelView();// 添加按钮
    		
    	}
    	else if (v.getId()==R.id.close)
    	{
    		closePanelView();
    		
    	}
    	else if(v.getId()==R.id.panel)
    	{
    		closePanelView();
    	}
    	else if(v.getId()==R.id.lbs_btn)
    	{
    		goPageURL("http://www.baidu.com");
    	}
    	else if(v.getId()==R.id.scan_btn)
    	{
    		Intent mItent = new Intent(PlusActivity.this,CaptureActivity.class);
    		startActivityForResult(mItent,2);
    	}
    	else
    	{
    		
    	}

    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String strResult;
		if(requestCode==2)
		{
			if(resultCode==RESULT_OK){
				if(data!=null)
				{
					Bundle bundle = data.getExtras();
					strResult = bundle.getString("result");
					goPageURL(strResult);
				}
			}
		}
		
		
	}
	
	/**
	 * 跳转到网页
	 * @param result 解码结果
	 */
	private void goPageURL(String result){
		
		 //判断是否是url链接
		 if(isWebSite( result)){//是完整的url,就直接跳转
			 Uri uri = Uri.parse(result.trim());
			 Intent intent = new Intent(Intent.ACTION_VIEW);
	         intent.addCategory(Intent.CATEGORY_BROWSABLE);
	         intent.setData(uri);
			 startActivity(intent);
		 }
	}
		 
	private boolean isWebSite(String strResult) 
	{   
		        Pattern p = Pattern.compile("(http://|https://){0,1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr)[^\u4e00-\u9fa5\\s]*");   
		        Matcher m = p.matcher(strResult);   
		        System.out.println(m.matches() + "---");   
		        return m.matches();   
	 }   
    
    


}
