package lu.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.lu.util.QRScannerHelper;
import com.lu.util.QRUtil;

public class MainActivity extends AppCompatActivity {
    private ImageView mCodeView;
    private QRScannerHelper mScannerHelper;
    private EditText mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initQRScanner();

    }

    private void initView() {
        mCodeView = (ImageView) findViewById(R.id.image);
        mContentView = (EditText) findViewById(R.id.content);
    }

    /**
     * 在onCreate中调用
     */
    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 开启扫描界面
     *
     * @param view
     */
    public void start(View view) {
        mScannerHelper.startScanner();
    }

    /**
     * 生成二维码
     *
     * @param v
     */
    public void generate(View v) {

        String content = mContentView.getText().toString();

        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入二维码信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap bitmap = QRUtil.createQRBitmap(content, 400);
        mCodeView.setImageBitmap(bitmap);
    }

    /**
     * 识别图中二维码
     *
     * @param view
     */
    public void spotQRCode(View view) {

        Drawable drawable = mCodeView.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "请先生成二维码图片", Toast.LENGTH_SHORT).show();
            return;
        }

        if (drawable instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
            try {
                Result result = QRUtil.spotQRCode(bm);
                Toast.makeText(this, "result:" + result, Toast.LENGTH_SHORT).show();
            } catch (FormatException e) {
                e.printStackTrace();
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "不是二维码图片", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }


}
