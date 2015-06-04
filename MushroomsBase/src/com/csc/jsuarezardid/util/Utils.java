package com.csc.jsuarezardid.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Utils {


	public static byte[] getByteArrayFromDrawable(Drawable drawable) {
		Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();
		return getByteArrayFromBitmap(imageBitmap);
	}


	public static byte[] getByteArrayFromBitmap(Bitmap imageBitmap) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
		byte[] byteArray = bos.toByteArray();
		return byteArray;
	}

	public static Bitmap getScaledBitMapFromBitMap(Bitmap imageBitmap) {
		float ratio = Math.min((float) 800 / imageBitmap.getWidth(),
				(float) 600 / imageBitmap.getHeight());
		int width = Math.round((float) ratio * imageBitmap.getWidth());
		int height = Math.round((float) ratio * imageBitmap.getHeight());

		Bitmap scalledImageBitmap = Bitmap.createScaledBitmap(imageBitmap,
				width, height, true);
		return scalledImageBitmap;
	}

	public static byte[] getScaledByteArrayFromDrawable(Drawable drawable) {
		Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();
		return getScaledByteArrayFromBitmap(imageBitmap);
	}

	public static byte[] getScaledByteArrayFromBitmap(Bitmap imageBitmap) {
		Bitmap scalledImageBitmap = getScaledBitMapFromBitMap(imageBitmap);
		return getByteArrayFromBitmap(scalledImageBitmap);
	}


}
