package googlemap.example.com.devicelockandunlock;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.TimePickerDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.konylabs.android.KonyMain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;


public class MainActivity extends Activity implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    static final int ACTIVATION_REQUEST = 47; // identifies our request id
    Button btn_devicesttings, btn_devicelock;
    int count;

    DevicePolicyManager devicePolicyManager;
    ComponentName demoDeviceAdmin;

    TextView textView_lock_time, textView_unlock_time;
    Button btn_lock_timepicker, btn_unlock_timepicker;
    String status_picker_time;

    private int seconds_count = 5000;
    Context contex_ko9ney = KonyMain.getActivityContext();

    TimePickerDialog timePickerDialog;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        demoDeviceAdmin = new ComponentName(this, DemoDeviceAdminReceiver.class);


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);

        timePickerDialog = new TimePickerDialog(MainActivity.this, MainActivity.this, hour, minute, true);


       /* final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                long epoch_time = 1501074595186L;
                long current_epoch_time = System.currentTimeMillis();


                String S = "1501076907290";

                //convert unix epoch timestamp (seconds) to milliseconds
                long timestamp = Long.parseLong(S) * 1000L;
                System.out.println("Result :???" + getDates(timestamp));


                String current_dates_cur = getDate(current_epoch_time, "HH:mm:a");
                String dates = getDate(epoch_time, "HH:mm:a");
                System.out.println("Result :" + current_dates_cur);

                System.out.println("Result : ?" + dates);
                StringTokenizer st = new StringTokenizer(dates, ":");

                int hh = Integer.parseInt(st.nextToken());
                int min = Integer.parseInt(st.nextToken());
                String am = st.nextToken();

                System.out.println("Result : ?" + dates);
                StringTokenizer current_st = new StringTokenizer(current_dates_cur, ":");

                int current_hour = Integer.parseInt(current_st.nextToken());
                int current_min = Integer.parseInt(current_st.nextToken());
                String current_a = current_st.nextToken();


                if (hh == current_hour && am.equalsIgnoreCase(current_a) && current_min >= min) {
                    devicePolicyManager.lockNow();
                } else {
                    KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                    final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
                    kl.disableKeyguard();


                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
                    wakeLock.acquire();
                }


                handler.postDelayed(this, 5000);
            }
        }, 5000);*/

        textView_lock_time = (TextView) findViewById(R.id.textView_lock_time);
        textView_unlock_time = (TextView) findViewById(R.id.textView_unlock_time);

        btn_lock_timepicker = (Button) findViewById(R.id.btn_lock_timepicker);
        btn_lock_timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status_picker_time = "lock";
                timePickerDialog.show();
            }
        });
        btn_unlock_timepicker = (Button) findViewById(R.id.btn_unlock_timepicker);
        btn_unlock_timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                status_picker_time = "unlock";
                timePickerDialog.show();
            }
        });

        btn_devicesttings = (Button) findViewById(R.id.btn_devicesttings);
        btn_devicesttings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        demoDeviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Your boss told you to do this");
                startActivity(intent);

            }
        });
        btn_devicelock = (Button) findViewById(R.id.btn_devicelock);
        btn_devicelock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

                        String lock = textView_lock_time.getText().toString().trim();
                        String unlock = textView_unlock_time.getText().toString().trim();

                        if (!lock.startsWith("lock") && !unlock.startsWith("unlock")) {






                            StringTokenizer st = new StringTokenizer(lock, ":");
                            int p_hr = Integer.parseInt(st.nextToken());
                            int p_min = Integer.parseInt(st.nextToken());
                            String p_ampm = st.nextToken();

                            StringTokenizer unlock_st = new StringTokenizer(lock, ":");
                            int unlock_st_hr = Integer.parseInt(unlock_st.nextToken());
                            int unlock_st_min = Integer.parseInt(unlock_st.nextToken());
                            String unlock_st_ampm = unlock_st.nextToken();

                            final Calendar c_time = Calendar.getInstance();
                            int year = c_time.get(Calendar.YEAR);
                            int month = c_time.get(Calendar.MONTH);
                            int day = c_time.get(Calendar.DAY_OF_MONTH);

                            int hour = c_time.get(Calendar.HOUR_OF_DAY);
                            int minute = c_time.get(Calendar.MINUTE);
                            int seconds = c_time.get(Calendar.SECOND);
                            String ampm = DateUtils.getAMPMString(c_time.get(Calendar.AM_PM));
                            Log.i(TAG,"Timing :"+ p_hr+" =="+ hour+" && "+p_ampm+".equalsIgnoreCase+"+(ampm));
                            Log.i(TAG,"Timing :"+ unlock_st_hr+" =="+ hour+" && "+unlock_st_ampm+".equalsIgnoreCase+"+(ampm));

                            if (p_hr == hour && p_ampm.equalsIgnoreCase(ampm)) {
                                devicePolicyManager.lockNow();
                            } else if (unlock_st_hr == hour && unlock_st_ampm.equalsIgnoreCase(ampm)) {
                                KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                                final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
                                kl.disableKeyguard();

                                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                                        | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
                                wakeLock.acquire();
                            }
                        }
                        handler.postDelayed(this, seconds_count);
                    }
                }, seconds_count);







               /* PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                pm.reboot("recovery");*/
               /* Process p=null;
                try {
                    p = new ProcessBuilder()
                            .command("/sdcard/script/reboot.sh")
                            .start();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(p!=null) p.destroy();
                }*/




                /*try {
                    File path = Environment.getExternalStorageDirectory();
                    Toast.makeText(getApplicationContext(),"path"+path.getAbsolutePath(),Toast.LENGTH_SHORT).show();

                    Process process = Runtime.getRuntime().exec("sh "+path+"/"+"reboot.sh");
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String listOfFiles = "";
                    String line;
                    while ((line = in.readLine()) != null) {
                        listOfFiles += line;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/


                /*count = 0;
                devicePolicyManager.lockNow();
                ScheduledExecutorService scheduler =
                        Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(new Runnable() {

                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

                                boolean screenOn;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                                    screenOn = pm.isInteractive();
                                } else {
                                    screenOn = pm.isScreenOn();
                                }

                                Toast.makeText(getApplicationContext(),""+screenOn,Toast.LENGTH_SHORT).show();

                                if (screenOn) {
                                    devicePolicyManager.lockNow();

                                } else {

                                    KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                                    final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
                                    kl.disableKeyguard();

                                    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                                            | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
                                    wakeLock.acquire();
                                }
                            }
                        });

                    }
                }, 0, 5000, TimeUnit.SECONDS);


*/
/*
                try{
                    Process su = Runtime.getRuntime().exec("adb shell");
                    DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());

                    outputStream.writeBytes("screenrecord --time-limit 10 /sdcard/MyVideo.mp4\n");
                    outputStream.flush();

                    outputStream.writeBytes("reboot\n");
                    outputStream.flush();
                    su.waitFor();
                }catch(IOException e){
                    Log.e(TAG,e.toString());
                }catch(InterruptedException e){
                    try {
                        throw new Exception(e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }*/


            }
        });
    }



/*    */

    /**
     * Called when startActivityForResult() call is completed. The result of
     * activation could be success of failure, mostly depending on user okaying
     * this app's request to administer the device.
     *//*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    //Log.i(TAG, "Administration enabled!");
                   // toggleButton.setChecked(true);
                } else {
                    // Log.i(TAG, "Administration enable FAILED!");
                    //toggleButton.setChecked(false);
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    private Date getDate(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    private String getDates(long timeStamp) {

        try {
            DateFormat sdf = new SimpleDateFormat("HH:MM:a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String am_pm = (hourOfDay < 12) ? "AM" : "PM";

        if (status_picker_time.equalsIgnoreCase("lock")) {
            textView_lock_time.setText(hourOfDay + ":" + minute + ":" + am_pm);
        } else {
            textView_unlock_time.setText(hourOfDay + ":" + minute + ":" + am_pm);
        }
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("button", String.valueOf(event.getKeyCode()));
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.e("Focus debug", "Focus changed !");

        if (!hasFocus) {
            Log.e("Focus debug", "Lost focus !");
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }
}
