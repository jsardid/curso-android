package com.csc.jsuarezardid.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Mushroom {
	private String commonName;
	private String binomialName;
	private String description;
	private byte[] image;

	public Mushroom(String commonName, String binomialName, String description,
			byte[] image) {
		this.commonName = commonName;
		this.binomialName = binomialName;
		this.description = description;
		this.image = image;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getBinomialName() {
		return binomialName;
	}

	public String getDescription() {
		return description;
	}

	public byte[] getImage() {
		return image;
	}

	public Bitmap getImageBitMap() {
		if (image.length != 0)
			return BitmapFactory.decodeByteArray(image, 0, image.length);
		else
			return null;
	}

}
