package com.example.xtwodirs;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;

public class MainActivity extends Activity {
    private static final int REQUEST_PERMISSIONS = 1;
    private static final int EXPORT_TO_DOCUMENTS_REQUEST = 1001;
    private EditText editText;
    private TextView textView;
    private String fileName = "inputtext.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        Button saveButton = findViewById(R.id.saveButton);
        Button showButton = findViewById(R.id.showButton);
        Button copyButton = findViewById(R.id.copyButton);
        Button exportButton = findViewById(R.id.exportButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveText();
            }
        });
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showText();
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToExternal();
            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportToDocuments();
            }
        });
    }

    private File getInternalFile() {
        return new File(getFilesDir(), fileName);
    }

    private File getExternalFile() {
        File dir = getExternalFilesDir(null);
        if (dir != null && !dir.exists()) dir.mkdirs();
        return new File(dir, fileName);
    }

    private void saveText() {
        String text = editText.getText().toString();
        File file = getInternalFile();
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(text + "\n");
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showText() {
        File file = getInternalFile();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            textView.setText(sb.toString());
        } catch (IOException e) {
            textView.setText("Show failed: " + e.getMessage());
        }
    }

    private void copyToExternal() {
        File src = getInternalFile();
        File dst = getExternalFile();
        copyFile(src, dst);
    }

    private void exportToDocuments() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, EXPORT_TO_DOCUMENTS_REQUEST);
    }

    private void copyFile(File src, File dst) {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            Toast.makeText(this, "Copied to " + dst.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Copy failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EXPORT_TO_DOCUMENTS_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try (InputStream in = new FileInputStream(getInternalFile());
                     OutputStream out = getContentResolver().openOutputStream(uri)) {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    Toast.makeText(this, "Exported to Documents", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
} 