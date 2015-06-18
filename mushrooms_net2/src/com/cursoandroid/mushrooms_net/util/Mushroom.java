package com.cursoandroid.mushrooms_net.util;

public class Mushroom {
	private String id;
	private String commonName;
	private String binomialName;
	private String description;

	public Mushroom(String id, String commonName, String binomialName,
			String description) {
		this.commonName = commonName;
		this.binomialName = binomialName;
		this.description = description;
		this.id = id;
	}

	public String getId() {
		return id;
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

}
