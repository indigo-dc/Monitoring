package com.deep.qcgprobe;

import com.deep.qcgprobe.beans.QCGResourcesBean;

import feign.Headers;
import feign.RequestLine;

/** Created by damian on 12/07/19. */
public interface QCGFeignClient {
	@Headers("Content-type: application/json")
	@RequestLine("GET /resources/")
	QCGResourcesBean getResources();
}
