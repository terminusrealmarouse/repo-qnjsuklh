package com.example.wtoolsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.easy.wtool.sdk.MessageEvent;
import com.easy.wtool.sdk.OnMessageListener;
import com.easy.wtool.sdk.OnTaskEndListener;
import com.easy.wtool.sdk.TaskEndEvent;
import com.easy.wtool.sdk.WToolSDK;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private EditText appIdInput;
    private EditText authCodeInput;
    private EditText talkerInput;
    private EditText messageInput;
    private TextView logView;
    private WToolSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createContentView());
        appendLog("示例已启动。请填写 appId/authCode 后初始化 SDK。");
    }

    private View createContentView() {
        ScrollView scrollView = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(16);
        root.setPadding(padding, padding, padding, padding);
        scrollView.addView(root);

        TextView title = new TextView(this);
        title.setText("WToolSDK Android 示例");
        title.setTextSize(22);
        root.addView(title);

        appIdInput = addInput(root, "appId", "");
        authCodeInput = addInput(root, "authCode", "");
        talkerInput = addInput(root, "接收方 wxid / 群 id", "wxid_xxx");
        messageInput = addInput(root, "文本消息", "Hello from WToolSDK sample");

        addButton(root, "初始化 SDK", view -> runSdkCall("init", () ->
                ensureSdk().init(textOf(appIdInput), textOf(authCodeInput))));
        addButton(root, "获取 SDK 版本", view -> runSdkCall("getVersion", () ->
                ensureSdk().getVersion()));
        addButton(root, "获取模块版本", view -> runSdkCall("getModuleVersion", () ->
                ensureSdk().getModuleVersion()));
        addButton(root, "启动消息监听", view -> runSdkCall("startMessageListener", () ->
                ensureSdk().startMessageListener("")));
        addButton(root, "发送文本任务", view -> runSdkCall("sendTask(action=1)", () ->
                ensureSdk().sendTask(buildSendTextTask())));
        addButton(root, "停止消息监听", view -> runSdkCall("stopMessageListener", () ->
                ensureSdk().stopMessageListener()));
        addButton(root, "卸载 SDK", view -> runSdkCall("unload", () ->
                ensureSdk().unload()));

        logView = new TextView(this);
        logView.setTextSize(14);
        root.addView(logView);
        return scrollView;
    }

    private EditText addInput(LinearLayout root, String hint, String value) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setText(value);
        editText.setSingleLine(true);
        root.addView(editText);
        return editText;
    }

    private void addButton(LinearLayout root, String text, View.OnClickListener listener) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(listener);
        root.addView(button);
    }

    private WToolSDK ensureSdk() {
        if (sdk == null) {
            sdk = new WToolSDK();
            sdk.setOnMessageListener(new OnMessageListener() {
                @Override
                public void messageEvent(MessageEvent event) {
                    runOnUiThread(() -> appendLog("messageEvent: type=" + event.getMsgType()
                            + ", talker=" + event.getTalker()
                            + ", from=" + event.getFrom()
                            + ", atMe=" + event.isAtMe()
                            + ", content=" + event.getContent()));
                }
            });
            sdk.setOnTaskEndListener(new OnTaskEndListener() {
                @Override
                public void taskEndEvent(TaskEndEvent event) {
                    runOnUiThread(() -> appendLog("taskEndEvent: type=" + event.getType()
                            + ", taskId=" + event.getTaskId()
                            + ", content=" + event.getContent()));
                }
            });
        }
        return sdk;
    }

    private String buildSendTextTask() throws Exception {
        JSONObject content = new JSONObject();
        content.put("talker", textOf(talkerInput));
        content.put("atwxids", new JSONArray());
        content.put("text", textOf(messageInput));
        content.put("timeout", -1);

        JSONObject task = new JSONObject();
        task.put("type", 1);
        task.put("taskid", System.currentTimeMillis());
        task.put("content", content);
        return task.toString();
    }

    private void runSdkCall(String label, SdkCall call) {
        try {
            appendLog(label + " 请求开始");
            appendLog(label + " 返回: " + call.run());
        } catch (Throwable throwable) {
            appendLog(label + " 失败: " + throwable.getClass().getSimpleName()
                    + ": " + throwable.getMessage());
        }
    }

    private void appendLog(String message) {
        String current = logView == null ? "" : logView.getText().toString();
        logView.setText(current + "\n" + message);
    }

    private String textOf(EditText editText) {
        return editText.getText().toString().trim();
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private interface SdkCall {
        String run() throws Exception;
    }
}
