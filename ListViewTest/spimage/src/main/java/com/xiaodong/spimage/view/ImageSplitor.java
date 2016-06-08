package com.xiaodong.spimage.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxd on 2016/6/1.
 */
public class ImageSplitor {

    public static List<ImagePiece> split(Bitmap bitmap,int count){
        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = Math.min(width,height)/count;
        for (int i=0;i<count;i++){
            for (int j=0;j<count;j++){
                ImagePiece piece = new ImagePiece();
                piece.index = j+i*count;
                //注意j和i
                int dx = j*pieceWidth;
                int dy = i*pieceWidth;
                Bitmap bmp = Bitmap.createBitmap(bitmap,dx,dy,pieceWidth,pieceWidth);
                piece.bitmap = bmp;
                imagePieces.add(piece);
            }

        }
        return imagePieces;
    }
}
