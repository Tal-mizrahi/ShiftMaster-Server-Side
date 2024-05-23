package demo;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {

	private Double lat;
	private Double lng;
	
	public Location() {

	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Location [lat=" + lat + ", lng=" + lng + "]";
	}
	
	
}
