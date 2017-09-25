package com.syu.dvr.utils;

import com.syu.dvr.R;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast
{
	Toast toast;
	TextView textView;
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;
	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

	@SuppressWarnings("deprecation")
	public MyToast(Context context)
	{
		textView = new TextView(context);
		textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.toast_bg));
		textView.setMaxWidth(600);
		textView.setMaxHeight(360);
		textView.setGravity(Gravity.CENTER);

		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 60);
		toast.setView(textView);
	}

	/**
	 * apply to update text or more than once hint
	 */
	@SuppressWarnings("deprecation")
	public MyToast(Context context, CharSequence text, int duration, int textColor, int textSize)
	{
		textView = new TextView(context);
		textView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.toast_bg));
		textView.setMaxWidth(600);
		textView.setMaxHeight(360);
		textView.setGravity(Gravity.CENTER);

		textView.setTextColor(textColor);
		textView.setTextSize(textSize);
		textView.setText(text);
		
		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 60);
		toast.setView(textView);
	}

	/**
	 * apply to Don't update text or Only hint once ,if no {@link new ToastView
	 * and Function( setText(),show(),etc.)}
	 * 
	 */
	public static Toast makeText(Context context, CharSequence text, int duration, int textColor,
			int textSize)
	{
		// SpannableString ss = new SpannableString(text);
		// ss.setSpan(new ForegroundColorSpan(textColor), 0, text.length(),
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// toast.setMargin(-0.5f, 0.5f);
		// toast.setText(Html.fromHtml("<font size=\"3\" color=\"red\">How are you ?</font><font size=\"3\" color=\"green\">还可以撒。</font>"));

		MyToast toastView = new MyToast(context, text, duration, textColor, textSize);
		return toastView.toast;
	}

	public void show()
	{
		if (toast != null)
			toast.show();
	}

	public void cancel()
	{
		if (toast != null)
			toast.cancel();
	}

	public void setText(CharSequence s)
	{
		if (textView != null)
		{
			textView.setText(s);
			if (toast != null)
				toast.setView(textView);
		}
	}

	public void setDuration(int duration)
	{
		if (toast != null)
		{
			toast.setDuration(duration);
		}
	}

	public void setTextSize(float size)
	{
		if (textView != null)
		{
			textView.setTextSize(size);
			if (toast != null)
				toast.setView(textView);
		}
	}

	public void setTextColor(int color)
	{
		if (textView != null)
		{
			textView.setTextColor(color);
			if (toast != null)
				toast.setView(textView);
		}
	}

	public CharSequence getText()
	{
		if (toast != null)
		{
			if (textView != null)
			{
				return textView.getText();
			}
		}
		return null;

	}
}

