package parentshour.spinlogics.com.parentshour.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import parentshour.spinlogics.com.parentshour.R;
import parentshour.spinlogics.com.parentshour.utilities.PreferenceUtils;

/**
 * Created by SPINLOGICS ic_on 12/3/2016.
 */

public class PreLoginActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton parentImageButton,assistantImageButton;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_pre_login);
        mContext = PreLoginActivity.this;
        initializeControls();
        parentImageButton.setOnClickListener(this);
        assistantImageButton.setOnClickListener(this);
    }

    private void initializeControls()
    {
        parentImageButton=(ImageButton)findViewById(R.id.ib_parent);
        assistantImageButton = (ImageButton)findViewById(R.id.ib_assistant);
    }

    @Override
    public void onClick(View view) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(getApplicationContext());
        switch (view.getId())
        {
            case R.id.ib_parent:
                preferenceUtils.saveString("select","parent");
                startActivity(new Intent(mContext, LoginActivity.class));
                break;

            case R.id.ib_assistant:
                preferenceUtils.saveString("select","assistant");
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
        }
    }
}
