package com.deep.zabbix.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import feign.Client;
import feign.Request;
import feign.Response;

public class AuthenticatedQCGFeignClient implements Client {

	private String qcgToken;
	private Client client = new Client.Default(null, null);

	public AuthenticatedQCGFeignClient(String qcgToken) {
		this.qcgToken = qcgToken;
	}

	public String getQcgToken() {
		return qcgToken;
	}

	public void setQcgToken(String qcgToken) {
		this.qcgToken = qcgToken;
	}

	private Response execute(Request request, Request.Options options, boolean retry) throws IOException {
		Request newRequest = request;
		if (getQcgToken() != null) {
			Map<String, Collection<String>> newHeaders = new HashMap<>(request.headers());
			newHeaders.put("Authorization",
					Arrays.asList(new String[] { "Bearer " + getQcgToken() }));
			newRequest = Request.create(request.httpMethod(), request.url(), newHeaders, request.body(),
					request.charset());
		}
		Response response = client.execute(newRequest, options);
		return (response.status() == 401 && retry) ? execute(request, options, false) : response;
	}

	@Override
	public Response execute(Request request, Request.Options options) throws IOException {
		return execute(request, options, true);
	}
}
