package com.example.calculate;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private int k;
    private int num;
    private int col;
    private List<String> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // 获取从MainActivity传递过来的数据
        k = getIntent().getIntExtra("k", 10);
        num = getIntent().getIntExtra("num", 5);
        col = getIntent().getIntExtra("col", 1);

        // 初始化RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, col));
        questions = Arrays.asList(generateQuestions(k, num));
        recyclerView.setAdapter(new QuestionsAdapter(questions));
        findViewById(R.id.btn_return).setOnClickListener(v -> btn_return());
        // 设置导出PDF按钮的点击事件
        findViewById(R.id.btn_export_pdf).setOnClickListener(v -> exportToPDF());
    }

    private String[] generateQuestions(int k, int num) {
        String[] questions = new String[k];
        for (int i = 0; i < k; i++) {
            int a = (int) (Math.random() * num) + 1;
            int b = (int) (Math.random() * num) + 1;
            int operation = (int) (Math.random() * 2); // 0 for addition, 1 for subtraction
            if (operation == 0) {
                questions[i] = a + " + " + b + " = ";
            } else {
                if (a >= b) {
                    questions[i] = a + " - " + b + " = ";
                } else {
                    questions[i] = b + " - " + a + " = ";
                }
            }
        }
        return questions;
    }
    private void btn_return(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void exportToPDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20); // 设置字体大小
        paint.setTextAlign(Paint.Align.LEFT); // 设置文本对齐方式为左对齐

        // PDF 页面大小 A4 (595.276f x 841.89f)
        float pdfWidth = 595.276f; // 宽度，单位：pt
        float pdfHeight = 841.89f; // 高度，单位：pt
        float margin = 52; // 边距，单位：pt

        // 计算每列的宽度
        float columnWidth = (pdfWidth - 2 * margin) / col;
        float usableHeight = pdfHeight - 2 * margin;

        // 计算行数
        int rows = (int) Math.ceil(k / (float) col);
        float lineHeight = usableHeight / rows; // 根据行数计算行高

        // 创建新页面
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder((int) pdfWidth, (int)pdfHeight, 0).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        float yPos = margin;

        for (int i = 0; i < k; i++) {
            // 计算列的位置
            float xPos = margin + (i % col) * columnWidth;

            // 生成题目并绘制
            String question = generateQuestion(num);
            canvas.drawText(question, xPos, yPos, paint);

            // 每行绘制完成后，更新y坐标
            if ((i + 1) % col == 0) {
                yPos += lineHeight;
                if (yPos > pdfHeight - margin) {
                    // 如果超出页面高度，停止绘制
                    break;
                }
            }
        }

        pdfDocument.finishPage(page);

        // 保存 PDF 文件
        File storageDir = new File(getExternalFilesDir(null), "PDFs");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File pdfFile = new File(storageDir, "questions.pdf");

        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            pdfDocument.writeTo(outputStream);
            Toast.makeText(this, "PDF保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF保存失败", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }

        // 通知系统媒体库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pdfFile)));
    }

    private String generateQuestion(int num) {
        int a = (int) (Math.random() * num) + 1;
        int b = (int) (Math.random() * num) + 1;
        int operation = (int) (Math.random() * 2); // 0 for addition, 1 for subtraction
        if (operation == 0) {
            return a + " + " + b + " = ";
        } else {
            return a + " - " + b + " = ";
        }
    }
}