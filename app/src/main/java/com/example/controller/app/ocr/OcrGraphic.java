/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.controller.app.ocr;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.List;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class OcrGraphic extends GraphicOverlay.Graphic {

    private static final String TAG = OcrGraphic.class.getSimpleName();
    private int mId;

    private static final int TEXT_COLOR = Color.GREEN;

//    private static Paint sRectPaint;
    private static Paint sTextPaint;
    private final TextBlock mText;

    OcrGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);

        mText = text;

//        if (sRectPaint == null) {
//            sRectPaint = new Paint();
//            sRectPaint.setColor(TEXT_COLOR);
//            sRectPaint.setStyle(Paint.Style.STROKE);
//            sRectPaint.setStrokeWidth(3.0f);
//        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(20.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public TextBlock getTextBlock() {
        return mText;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        if (mText == null) {
            return false;
        }
        RectF rect = new RectF(mText.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (mText == null) {
            return;
        }

        // Draws the bounding box around the TextBlock.
        RectF rect = new RectF(mText.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
//        canvas.drawRect(rect, sRectPaint);
        // Break the text into multiple lines and draw each one according to its own bounding box.
        List<? extends Text> textComponents = mText.getComponents();
        for(Text currentText : textComponents) {
            float left = translateX(currentText.getBoundingBox().left);
            float bottom = translateY(currentText.getBoundingBox().bottom);
            float top = translateY(currentText.getBoundingBox().top);
            float ts = (bottom-top)*0.75f;
            sTextPaint.setTextSize(ts);
//            Log.e(TAG, LogUtil.prependCallLocation("draw: "+currentText.getValue()+" "+(bottom-top) ));
            canvas.drawText(currentText.getValue(), left, bottom, sTextPaint);
            // Break the line into multiple words and draw each one according to its own bounding box.
//            for(Text word : currentText.getComponents()) {
//
//                float left = translateX(word.getBoundingBox().left);
//                float bottom = translateY(word.getBoundingBox().bottom);
//                float top = translateY(word.getBoundingBox().top);
//                float ts1 = (bottom-top)*0.75f;
//                float ts2 = (word.getBoundingBox().bottom - word.getBoundingBox().top )*0.75f;
//                sTextPaint.setTextSize(ts1);
//                Log.e(TAG, LogUtil.prependCallLocation("draw: "+word.getValue()+" "+ts1+" "+ + ts2 ));
//                canvas.drawText(word.getValue(), left, bottom, sTextPaint);
//            }
        }
    }
}
