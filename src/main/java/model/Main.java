package model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Main{

	@SerializedName("temp")
	private double temp;

	@SerializedName("temp_min")
	private double tempMin;

	@SerializedName("humidity")
	private int humidity;

	@SerializedName("pressure")
	private int pressure;

	@SerializedName("feels_like")
	private double feelsLike;

	@SerializedName("temp_max")
	private double tempMax;
}