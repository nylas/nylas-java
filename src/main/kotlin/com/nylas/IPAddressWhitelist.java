package com.nylas;

import java.time.Instant;
import java.util.List;

public class IPAddressWhitelist {

	private List<String> ip_addresses;
	private Long updated_at;
	
	public List<String> getIpAddresses() {
		return ip_addresses;
	}
	
	public Instant getUpdatedAt() {
		return Instants.toNullableInstant(updated_at);
	}

	@Override
	public String toString() {
		return "IPAddressWhitelist [ip_addresses=" + ip_addresses + ", updated_at=" + getUpdatedAt() + "]";
	}
	
}
