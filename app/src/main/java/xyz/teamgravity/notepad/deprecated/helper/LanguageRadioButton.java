package xyz.teamgravity.notepad.deprecated.helper;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import xyz.teamgravity.notepad.R;


public class LanguageRadioButton extends RelativeLayout implements RadioCheckable {
    private TextView mValueTextView;
    private ImageView mImageView;

    // Constants
    public static final int DEFAULT_TEXT_COLOR = Color.TRANSPARENT;

    // Attribute Variables
    private String mValue;
    private Drawable mDrawable;
    private int mDrawableRes;
    private int mValueTextColor;
    private int mPressedTextColor;

    // Variables
    private Drawable mInitialBackgroundDrawable;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private boolean mChecked;
    private boolean mIsCheckable = true;
    private ArrayList<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();


    //================================================================================
    // Constructors
    //================================================================================

    public LanguageRadioButton(Context context) {
        super(context);
        setupView();
    }

    public LanguageRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public LanguageRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LanguageRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    //================================================================================
    // Init & inflate methods
    //================================================================================

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.LanguageRadioButton, 0, 0);
        Resources resources = getContext().getResources();
        try {
            mValue = a.getString(R.styleable.LanguageRadioButton_valueText);
            mDrawable = a.getDrawable(R.styleable.LanguageRadioButton_presetButtonImage);
            mValueTextColor = a.getColor(R.styleable.LanguageRadioButton_valueTextColor, resources.getColor(R.color.deep_grey));
            mPressedTextColor = a.getColor(R.styleable.LanguageRadioButton_pressedTextColor, Color.WHITE);
        } finally {
            a.recycle();
        }
    }

    // Template method
    private void setupView() {
        inflateView();
        bindView();
        setCustomTouchListener();
    }

    protected void inflateView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.custom_language_button, this, true);
        mValueTextView = findViewById(R.id.text_2_value);
        mImageView = findViewById(R.id.text_1_value);
        mInitialBackgroundDrawable = getBackground();
    }

    protected void bindView() {
        if (mValueTextColor != DEFAULT_TEXT_COLOR) {
            mValueTextView.setTextColor(mValueTextColor);
        }
        mImageView.setImageDrawable(mDrawable);
        mValueTextView.setText(mValue);
    }

    //================================================================================
    // Overriding default behavior
    //================================================================================

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    private void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    private void onTouchDown(MotionEvent motionEvent) {
        setChecked(true);
    }

    private void onTouchUp(MotionEvent motionEvent) {
        // Handle user defined click listeners
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }
    //================================================================================
    // Public methods
    //================================================================================

    public void setCheckedState() {
        setBackgroundResource(R.drawable.background_shape_preset_button__pressed);
        mValueTextView.setTextColor(mPressedTextColor);
    }

    public void setNormalState() {
        setBackground(mInitialBackgroundDrawable);
        mValueTextView.setTextColor(mValueTextColor);
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
        mValueTextView.setText(mValue);
    }

    public void setValueColorResource(int color) {
        mValueTextView.setTextColor(color);
    }

    public void setImageResource(int imageResource) {
        mDrawableRes = imageResource;
        mImageView.setImageResource(mDrawableRes);
    }

    //================================================================================
    // Checkable implementation
    //================================================================================

    @Override
    public void setChecked(boolean checked) {
        if (mIsCheckable) {
            if (mChecked != checked) {
                mChecked = checked;
                if (!mOnCheckedChangeListeners.isEmpty()) {
                    for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                        mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                    }
                }
                if (mChecked) {
                    setCheckedState();
                } else {
                    setNormalState();
                }
            }
        }
    }

    public void setIsCheckable(boolean isCheckable) {
        mIsCheckable = isCheckable;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);
    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }

    //================================================================================
    // Inner classes
    //================================================================================
    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }
}
