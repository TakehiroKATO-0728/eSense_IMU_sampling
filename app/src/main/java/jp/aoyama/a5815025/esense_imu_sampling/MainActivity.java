package jp.aoyama.a5815025.esense_imu_sampling;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.esense.esenselib.*;

public class MainActivity extends Activity implements ESenseConnectionListener, ESenseSensorListener{

    TextView textView;
    OutputCsv outputCsv = new OutputCsv();
    int flag = 0;
    private long startTime;
    private long progress_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.text);
        textView.setText("STARTを押して計測開始");

        ESenseManager manager = new ESenseManager("eSense-0041", this,this);
        manager.connect(2000);
    }

    @Override
    public void onDeviceFound(ESenseManager eSenseManager) {

    }

    @Override
    public void onDeviceNotFound(ESenseManager eSenseManager) {

    }

    @Override
    public void onConnected(ESenseManager eSenseManager) {
        //センサーリスナーを登録し、センサーデータを受信するサンプリングレートを指定
        eSenseManager.registerSensorListener(this,100);
    }

    @Override
    public void onDisconnected(ESenseManager eSenseManager) {

    }

    public void onClick(View v){
        Log.d("ESENSE"," button clicked");
        switch (v.getId()){
            case R.id.button1:
                Log.d("ESENSE","start button clicked");
                flag = 1;
                startTime = System.currentTimeMillis();
                break;
            case R.id.button2:
                //outputCsv.write("afjkla");
                outputCsv.close();
                textView.setText("計測終了");
                flag = 0;
                break;
        }
    }

    @Override
    public void onSensorChanged(ESenseEvent eSenseEvent) {
        short[] val=eSenseEvent.getAccel();
        Log.d("ESENSE x",String.valueOf(val[0]));
        Log.d("ESENSE y",String.valueOf(val[1]));
        Log.d("ESENSE z",String.valueOf(val[2]));
        if(flag == 1){
            progress_time = System.currentTimeMillis() - startTime;
            outputCsv.write( String.valueOf(val[0])+ "," + String.valueOf(val[1])+ "," +String.valueOf(val[2])+ "," +String.valueOf(progress_time));
            //textView.setText("x = " + String.valueOf(val[0]) + "\n" + "y = " + String.valueOf(val[1])+ "\n" + "z = " + String.valueOf(val[2]) + "\n" + "経過時間(ミリ秒):" + String.valueOf(progress_time));
            textView.setText("計測開始");
        }
    }
}
