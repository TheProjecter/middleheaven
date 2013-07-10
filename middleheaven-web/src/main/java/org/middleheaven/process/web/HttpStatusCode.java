package org.middleheaven.process.web;

/**
 * Enum for HTTP 1.1 protocol commom response codes (4xx and 5xx)
 * 
 * @see http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
 */
public enum HttpStatusCode {
	
	/**
	 *  Status code (202) indicating that a request was accepted for processing, but was not completed.
	 */
	ACCEPTED(202), 
	/** TODO translate to enum
static int 	SC_BAD_GATEWAY
          Status code (502) indicating that the HTTP server received an invalid response from a server it consulted when acting as a proxy or gateway.
static int 	SC_BAD_REQUEST
          Status code (400) indicating the request sent by the client was syntactically incorrect.
static int 	SC_CONFLICT
          Status code (409) indicating that the request could not be completed due to a conflict with the current state of the resource.
static int 	SC_CONTINUE
          Status code (100) indicating the client can continue.
static int 	SC_CREATED
          Status code (201) indicating the request succeeded and created a new resource on the server.
static int 	SC_EXPECTATION_FAILED
          Status code (417) indicating that the server could not meet the expectation given in the Expect request header.
static int 	SC_FORBIDDEN
          Status code (403) indicating the server understood the request but refused to fulfill it.
static int 	SC_GATEWAY_TIMEOUT
          Status code (504) indicating that the server did not receive a timely response from the upstream server while acting as a gateway or proxy.
static int 	SC_GONE
          Status code (410) indicating that the resource is no longer available at the server and no forwarding address is known.
static int 	SC_HTTP_VERSION_NOT_SUPPORTED
          Status code (505) indicating that the server does not support or refuses to support the HTTP protocol version that was used in the request message.
static int 	SC_INTERNAL_SERVER_ERROR
          Status code (500) indicating an error inside the HTTP server which prevented it from fulfilling the request.
static int 	SC_LENGTH_REQUIRED
          Status code (411) indicating that the request cannot be handled without a defined Content-Length.
static int 	SC_METHOD_NOT_ALLOWED
          Status code (405) indicating that the method specified in the Request-Line is not allowed for the resource identified by the Request-URI.
static int 	SC_MOVED_PERMANENTLY
          Status code (301) indicating that the resource has permanently moved to a new location, and that future references should use a new URI with their requests.
static int 	SC_MOVED_TEMPORARILY
          Status code (302) indicating that the resource has temporarily moved to another location, but that future references should still use the original URI to access the resource.
static int 	SC_MULTIPLE_CHOICES
          Status code (300) indicating that the requested resource corresponds to any one of a set of representations, each with its own specific location.
static int 	SC_NO_CONTENT
          Status code (204) indicating that the request succeeded but that there was no new information to return.
static int 	SC_NON_AUTHORITATIVE_INFORMATION
          Status code (203) indicating that the meta information presented by the client did not originate from the server.
static int 	SC_NOT_ACCEPTABLE
          Status code (406) indicating that the resource identified by the request is only capable of generating response entities which have content characteristics not acceptable according to the accept headerssent in the request.
static int 	SC_NOT_FOUND
          Status code (404) indicating that the requested resource is not available.
static int 	SC_NOT_IMPLEMENTED
          Status code (501) indicating the HTTP server does not support the functionality needed to fulfill the request.
static int 	SC_NOT_MODIFIED
          Status code (304) indicating that a conditional GET operation found that the resource was available and not modified.
static int 	SC_OK
          Status code (200) indicating the request succeeded normally.
static int 	SC_PARTIAL_CONTENT
          Status code (206) indicating that the server has fulfilled the partial GET request for the resource.
static int 	SC_PAYMENT_REQUIRED
          Status code (402) reserved for future use.
static int 	SC_PRECONDITION_FAILED
          Status code (412) indicating that the precondition given in one or more of the request-header fields evaluated to false when it was tested on the server.
static int 	SC_PROXY_AUTHENTICATION_REQUIRED
          Status code (407) indicating that the client MUST first authenticate itself with the proxy.
static int 	SC_REQUEST_ENTITY_TOO_LARGE
          Status code (413) indicating that the server is refusing to process the request because the request entity is larger than the server is willing or able to process.
static int 	SC_REQUEST_TIMEOUT
          Status code (408) indicating that the client did not produce a requestwithin the time that the server was prepared to wait.
static int 	SC_REQUEST_URI_TOO_LONG
          Status code (414) indicating that the server is refusing to service the request because the Request-URI is longer than the server is willing to interpret.
static int 	SC_REQUESTED_RANGE_NOT_SATISFIABLE
          Status code (416) indicating that the server cannot serve the requested byte range.
static int 	SC_RESET_CONTENT
          Status code (205) indicating that the agent SHOULD reset the document view which caused the request to be sent.
static int 	SC_SEE_OTHER
          Status code (303) indicating that the response to the request can be found under a different URI.
static int 	SC_SERVICE_UNAVAILABLE
          Status code (503) indicating that the HTTP server is temporarily overloaded, and unable to handle the request.
static int 	SC_SWITCHING_PROTOCOLS
          Status code (101) indicating the server is switching protocols according to Upgrade header.
static int 	SC_UNAUTHORIZED
          Status code (401) indicating that the request requires HTTP authentication.
static int 	SC_UNSUPPORTED_MEDIA_TYPE
          Status code (415) indicating that the server is refusing to service the request because the entity of the request is in a format not supported by the requested resource for the requested method.
static int 	SC_USE_PROXY
          Status code (305) indicating that the requested resource MUST be accessed through the proxy given by the Location field.
	 */
	/**
	 * The server has not found anything matching the Request-URI.
	 *  No indication is given of whether the condition is temporary or permanent. 
	 *  The 410 (Gone) status code SHOULD be used if the server knows, 
	 *  through some internally configurable mechanism, that an old resource is permanently 
	 *  unavailable and has no forwarding address. 
	 *  This status code is commonly used when the server does not wish to reveal exactly 
	 *  why the request has been refused, or when no other response is applicable. 
	 */
	NOT_FOUND(404), 
	
	/**
	* The server understood the request, but is refusing to fulfill it. 
	* Authorization will not help and the request SHOULD NOT be repeated. 
	* If the request method was not HEAD and the server wishes to make public why the request has not been fulfilled, 
	* it SHOULD describe the reason for the refusal in the entity. 
	* If the server does not wish to make this information available to the client, 
	* the status code 404 (Not Found) can be used instead.
	* */ 
	FORBIDDEN(403),
	
	/**
	 *  Status code (408) indicating that the client did not produce a request within the time that the server was prepared to wait.
	 */
	REQUEST_TIMEOUT(408),
  
	/**
	 *  The requested resource is no longer available at the server and no forwarding address is known. 
	 *  This condition is expected to be considered permanent. 
	 *  Clients with link editing capabilities SHOULD delete references to the Request-URI after user approval. 
	 *  If the server does not know, or has no facility to determine, whether or not the condition is permanent, the status code 404 (Not Found) SHOULD be used instead. 
	 *  This response is cacheable unless indicated otherwise.
	 *  The 410 response is primarily intended to assist the task of web maintenance by notifying the recipient 
	 *  that the resource is intentionally unavailable and that the server owners desire that remote links to that
	 *   resource be removed. Such an event is common for limited-time, promotional services and for 
	 *   resources belonging to individuals no longer working at the server's site. 
	 *   It is not necessary to mark all permanently unavailable resources as "gone" or to keep the mark for any 
	 *   length of time -- that is left to the discretion of the server owner. 
	 */
	GONE(410),
	
	/**
	 * The server encountered an unexpected condition which prevented it from fulfilling the request. 
	 */
	INTERNAL_SERVER_ERROR(500),
	
	/**
	 * The server does not support the functionality required to fulfill the request. 
	 * This is the appropriate response when the server does not recognize the request 
	 * method and is not capable of supporting it for any resource. 
	 */
	NOT_IMPLEMENTED(501),
	
	
	/**
	 * The server, while acting as a gateway or proxy, 
	 * received an invalid response from the upstream server it accessed in attempting to fulfill the request. 
	 */
	BAD_GATEWAY(502),
	
	/**
	 * The server is currently unable to handle the request due to a temporary overloading or maintenance 
	 * of the server. 
	 * The implication is that this is a temporary condition which will be alleviated after some delay. 
	 * If known, the length of the delay MAY be indicated in a Retry-After header. 
	 * If no Retry-After is given, the client SHOULD handle the response as it would for a INTERNAL_SERVER_ERROR (500) response. 
	 */
	SERVICE_UNAVAILABLE(503), 
	
	HTTP_VERSION_NOT_SUPPORTED(505), 
	
	/**
	 * All is correct
	 */
	OK(200),
	
	MOVED(300),
	MOVED_PERMANENTLY(301);

	
	public static HttpStatusCode valueOf(int errorCode) {
		for (HttpStatusCode error : HttpStatusCode.values()){
			if (error.code == errorCode){
				return error;
			}
		}
		return null;
	}
	
	
	private int code;

	private HttpStatusCode(int code){
		this.code = code;
	}
	
	public int intValue(){
		return code;
	}


}
