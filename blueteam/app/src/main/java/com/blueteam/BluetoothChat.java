package com.blueteam;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.blueteam.Bluetooth.BluetoothChatService;
import com.blueteam.Bluetooth.Data_syn;
import com.blueteam.Bluetooth.DeviceListActivity;
import com.blueteam.fragment.NowFragment;
import com.blueteam.fragment.ProfileFragment;
import com.blueteam.fragment.ReportFragment;
import com.blueteam.fragment.TextFragment;
import com.blueteam.fragment.WikiFragment;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * bluetooth communication Activity。
 */
public class BluetoothChat extends AppCompatActivity {
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    private boolean exit = false;

    // type from BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // intent request
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private TextView mTitle;
    private TextView mConversationView;
    private TextView outcount;
    private TextView incount;

    private TextView view;

    private CheckBox in16;
    private CheckBox autosend;
    private CheckBox out16;

    private Button mSendButton;

    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;

    private int level = 1; //current level
    private int limit = 5; //level limit

    private Button search;
    private Button calibrate;
    private Button disc;
    public String filename = "";
    private String fmsg = "";

    // for count
    private int countIn = 0;
    private int countOut = 0;

    // connected devices name
    private String mConnectedDeviceName;

    private StringBuffer mOutStringBuffer;

    private BluetoothAdapter mBluetoothAdapter;
    // communication service
    private BluetoothChatService mChatService;
    // CheckBox
    private boolean inhex = true;
    private boolean outhex = true;
    private boolean auto = false;

    ImageView imageView;
    private Bitmap bitmap;


    // Tab bar components
    private AlphaTabsIndicator alphaTabsIndicator;
    private ViewPager mViewPager;

    // for fragments
    public static FragmentManager manager;
    private LinearLayout cv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D)
            Log.e(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.activity_bluetooth_chat_layout);

        initView();

        // get bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        Log.d("BluetoothAdapter: ", String.valueOf(mBluetoothAdapter));

        // if no bluetooth adapter available, finish
//        if (mBluetoothAdapter == null) {
//            Toast.makeText(this, "BlueTooth Not Available", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }

        initEvent();

    }


    private void initView() {
        in16 = (CheckBox) findViewById(R.id.in16);
        autosend = (CheckBox) findViewById(R.id.autosend);
        out16 = (CheckBox) findViewById(R.id.out16);

        search = findViewById(R.id.search);  // search for devices
        calibrate = findViewById(R.id.calibrate);  // calibrate device
        disc = findViewById(R.id.discoverable1);

        mSendButton = findViewById(R.id.button_send);

        // level change part
        etAmount = findViewById(R.id.etAmount);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);

        view = findViewById(R.id.edit_text_out);

        imageView = findViewById(R.id.main_pic);


        //tab bar part
        mViewPager = findViewById(R.id.mViewPager);
        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mainAdapter);
        mViewPager.addOnPageChangeListener(mainAdapter);

        alphaTabsIndicator = findViewById(R.id.alphaIndicator);
        alphaTabsIndicator.setViewPager(mViewPager);

        manager = getSupportFragmentManager();
    }


    private void initEvent() {
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                search();
            }
        });

        disc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(BluetoothChat.this, "该设备已设置为可在300秒内发现，且可连接",
                        Toast.LENGTH_SHORT).show();
                ensureDiscoverable();
            }
        });

        // set listeners
        in16.setOnCheckedChangeListener(listener);
        autosend.setOnCheckedChangeListener(listener);
        out16.setOnCheckedChangeListener(listener);

    }


    private class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private List<Fragment> fragments = new ArrayList<>();
        private String[] titles = {"Now", "Report", "Wiki", "Profile"};

        public MainAdapter(FragmentManager fm) {
            super(fm);
            // for test
            fragments.add(TextFragment.newInstance(titles[0]));
            fragments.add(TextFragment.newInstance(titles[1]));
            fragments.add(TextFragment.newInstance(titles[2]));
            fragments.add(TextFragment.newInstance(titles[3]));
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            FragmentTransaction transaction = manager.beginTransaction();

            cv = findViewById(R.id.fragment_container);
            cv.removeAllViewsInLayout();

            if (0 == position) {
                Log.d("Select Now", "To Now");
                transaction.replace(R.id.fragment_container, new NowFragment());
            } else if (1 == position) {
                Log.d("Select Report", "To Report");
                transaction.replace(R.id.fragment_container, new ReportFragment());
            } else if (2 == position) {
                Log.d("Select Wiki", "To Wiki");

                transaction.replace(R.id.fragment_container, new WikiFragment());
            } else if (3 == position) {
                Log.d("Select Profile", "To Profile");
                transaction.replace(R.id.fragment_container, new ProfileFragment());
            }
            transaction.commit();

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /* ------------------------------ BlueTooth code -------------------------------------------------*/


    public void search() {
        Intent serverIntent = new Intent(BluetoothChat.this,
                DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    private OnCheckedChangeListener listener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // in16 is selected
            if (buttonView.getId() == R.id.in16) {
                if (isChecked) {
                    Toast.makeText(BluetoothChat.this, "16进制显示",
                            Toast.LENGTH_SHORT).show();
                    inhex = true;
                } else
                    inhex = false;
            }

            //out16 is selected
            if (buttonView.getId() == R.id.out16) {
                if (isChecked) {
                    Toast.makeText(BluetoothChat.this, "16进制发送",
                            Toast.LENGTH_SHORT).show();
                    outhex = true;
                } else
                    outhex = false;
            }
            // autosend is selected
            if (buttonView.getId() == R.id.autosend) {
                if (isChecked) {
                    Toast.makeText(BluetoothChat.this, "自动发送",
                            Toast.LENGTH_SHORT).show();
                    auto = true;
                } else
                    auto = false;
            }
        }
    };


//    @Override
//    public void onStart() {
//        super.onStart();
//        if (D)
//            Log.e(TAG, "++ ON START ++");
//
//
//        //如果BT未打开，请求启用。
//        // 然后在onActivityResult期间调用setupChat（）
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(
//                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//            // 否则，设置聊天会话
//        } else {
//
//            if (mChatService == null)
//                setupChat();
//            else {
//                try {
//                    mChatService.wait(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    //300秒内搜索
    private void ensureDiscoverable() {
        if (D)
            Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            //设置本机蓝牙可让发现
            discoverableIntent.putExtra(
                    BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    //自动发送
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String message = view.getText().toString();
            sendMessage(message);
            // 初始化输出流缓冲区
            mOutStringBuffer = new StringBuffer("");

        }
    };


    //初始化
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        mConversationView = (TextView) findViewById(R.id.in);
        mConversationView.setMovementMethod(ScrollingMovementMethod
                .getInstance());// 使TextView接收区可以滚动
        outcount = (TextView) findViewById(R.id.outcount);
        incount = (TextView) findViewById(R.id.incount);
        outcount.setText("0");
        incount.setText("0");

        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 使用编辑文本小部件的内容发送消息
//				TextView view = (TextView) findViewById(R.id.edit_text_out);

//				String message = view.getText().toString();
                String message = "f" + etAmount.getText().toString() + "e";
                sendMessage(message);

            }
        });

        btnDecrease.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (level > 1) {
                    level--;

                    etAmount.setText(String.valueOf(level));
                }
            }
        });

        btnIncrease.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (level < limit) {
                    level++;

                    etAmount.setText(String.valueOf(level));
                }
            }
        });

        // 初始化BluetoothChatService以执行app_incon_bluetooth连接
        mChatService = new BluetoothChatService(this, mHandler);

        //初始化外发消息的缓冲区
        mOutStringBuffer = new StringBuffer("");
    }


    //重写发送函数，参数不同。
    private void sendMessage(String message) {
        // 确保已连接
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // check message length
        if (message.length() > 0) {
            // 获取 字符串并告诉BluetoothChatService发送
//			if (outhex == true) {
//				Toast.makeText(this, "sukepp: hex",
//						Toast.LENGTH_SHORT).show();
//				byte[] send = Data_syn.hexStr2Bytes(message);
//				mChatService.write(send);//回调service
//
//			} else if (outhex == false) {
//				Toast.makeText(this, message,
//						Toast.LENGTH_SHORT).show();
//				byte[] send = message.getBytes();
//				mChatService.write(send);//回调service
//			}
//			Toast.makeText(this, message,
//					Toast.LENGTH_SHORT).show();
            byte[] send = message.getBytes();
            mChatService.write(send);//回调service

            // 清空输出缓冲区
            mOutStringBuffer.setLength(0);
        } else {
            Toast.makeText(this, "发送内容不能为空",
                    Toast.LENGTH_SHORT).show();
        }
    }


    // 该Handler从BluetoothChatService中获取信息
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);


                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(mConnectedDeviceName);
                            mConversationView.setText(null);
                            break;

                        case BluetoothChatService.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            break;

                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // 自动发送
                    if (auto == true) {

                        // 自动发送模块
                        mHandler.postDelayed(runnable, 1000);
                    } else if (auto == false) {
                        mHandler.removeCallbacks(runnable);
                    }
                    // 发送计数
                    if (outhex == true) {
                        String writeMessage = Data_syn.Bytes2HexString(writeBuf);
                        countOut += writeMessage.length() / 2;
                        outcount.setText("" + countOut);
                    } else if (outhex == false) {
                        String writeMessage = null;
                        try {
                            writeMessage = new String(writeBuf, "GBK");
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                        countOut += writeMessage.length();
                        outcount.setText("" + countOut);
                    }
                    break;
                case MESSAGE_READ:

                    byte[] readBuf = (byte[]) msg.obj;

                    //检错误码计算函数

                    if (inhex == true) {
                        String readMessage = " "
                                + Data_syn.bytesToHexString(readBuf, msg.arg1);
                        fmsg += readMessage;
                        mConversationView.append(readMessage);
                        // 接收计数，更显UI
                        countIn += readMessage.length() / 2;
                        incount.setText("" + countIn);
                    } else if (inhex == false) {
                        String readMessage = null;
                        try {
                            readMessage = new String(readBuf, 0, msg.arg1, "GBK");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        fmsg += readMessage;
                        mConversationView.append(readMessage);
                        // 接收计数，更新UI
                        countIn += readMessage.length();
                        incount.setText("" + countIn);
                    }

                    switch (readBuf[0]) {
                        case 1:
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
                            break;
                        case 2:
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
                            break;
                        case 3:
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
                            break;
                        case 4:
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
                            break;
                        case 5:
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
                            break;
                        default:
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
                            break;
                    }
                    imageView.setImageBitmap(bitmap);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 保存已连接设备的名称
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "连接到 " + mConnectedDeviceName, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }
    };


    //返回该Activity回调函数
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (D)
            Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {

//search返回的
            case REQUEST_CONNECT_DEVICE:

                // DeviceListActivity返回时要连接的设备
                if (resultCode == Activity.RESULT_OK) {
                    // 获取设备的MAC地址
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    // 获取BLuetoothDevice对象
                    BluetoothDevice device = mBluetoothAdapter
                            .getRemoteDevice(address);
                    // 尝试连接到设备
                    mChatService.connect(device);
                }
                break;
//start返回的（遇到蓝牙不可用退出）
            case REQUEST_ENABLE_BT:
                // 当启用蓝牙的请求返回时
                if (resultCode == Activity.RESULT_OK) {
                    //蓝牙已启用，因此设置聊天会话
                    setupChat();//初始化文本
                } else {
                    // 用户未启用蓝牙或发生错误
                    Log.d(TAG, "BT not enabled");

                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    // 保存按键响应函数
    public void onSaveButtonClicked(View v) {
        Save();
    }

    // 清屏按键响应函数
    public void onClearButtonClicked(View v) {
        fmsg = "";
        mConversationView.setText(null);
        view.setText(null);
        return;
    }

    // 清除计数按键响应函数
    public void onClearCountButtonClicked(View v) {
        countIn = 0;
        countOut = 0;
        outcount.setText("0");
        incount.setText("0");
        return;
    }


    // 保存功能实现
    private void Save() {
        // 显示对话框输入文件名
        LayoutInflater factory = LayoutInflater.from(BluetoothChat.this); // 图层模板生成器句柄
        final View DialogView = factory.inflate(R.layout.sname, null); // 用sname.xml模板生成视图模板
        new AlertDialog.Builder(BluetoothChat.this).setTitle("文件名")
                .setView(DialogView) // 设置视图模板
                .setPositiveButton("确定", new DialogInterface.OnClickListener() // 确定按键响应函数
                {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        EditText text1 = (EditText) DialogView
                                .findViewById(R.id.sname); // 得到文件名输入框句柄
                        filename = text1.getText().toString(); // 得到文件名

                        try {
                            if (Environment.getExternalStorageState()
                                    .equals(Environment.MEDIA_MOUNTED)) { // 如果SD卡已准备好

                                filename = filename + ".txt"; // 在文件名末尾加上.txt
                                File sdCardDir = Environment
                                        .getExternalStorageDirectory(); // 得到SD卡根目录
                                File BuildDir = new File(sdCardDir,
                                        "/BluetoothSave"); // 打开BluetoothSave目录，如不存在则生成
                                if (BuildDir.exists() == false)
                                    BuildDir.mkdirs();
                                File saveFile = new File(BuildDir,
                                        filename); // 新建文件句柄，如已存在仍新建文档
                                FileOutputStream stream = new FileOutputStream(
                                        saveFile); // 打开文件输入流
                                stream.write(fmsg.getBytes());
                                stream.close();
                                Toast.makeText(BluetoothChat.this,
                                        "存储成功！", Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(BluetoothChat.this,
                                        "没有存储卡！", Toast.LENGTH_LONG)
                                        .show();
                            }
                        } catch (IOException e) {
                            return;
                        }
                    }
                })
                .setNegativeButton("取消", // 取消按键响应函数,直接退出对话框不做任何处理
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show(); // 显示对话框
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D)
            Log.e(TAG, "+ ON RESUME +");
        // 在onResume（）中执行此检查包括在onStart（）期间未启用BT的情况，
        // 因此我们暂停启用它...
        // onResume（）将在ACTION_REQUEST_ENABLE活动时被调用返回.
        if (mChatService != null) {
            // 只有状态是STATE_NONE，我们知道我们还没有启动蓝牙
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // 启动BluetoothChat服务
                mChatService.start();
            }
        }

    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D)
            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D)
            Log.e(TAG, "-- ON STOP --");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 停止蓝牙通信连接服务
        if (mChatService != null)
            mChatService.stop();
        if (D)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    public void exit() {
        exit = true;


        if (exit == true) {
            this.finish();
        }

    }
}