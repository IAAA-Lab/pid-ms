package es.unizar.iaaa.pid.harvester.connectors.wfs;

import com.ximpleware.VTDGen;

public class WFSResponse {
	public static final WFSResponse FAIL = new WFSResponse(null, null, ResponseStatus.FAIL);

	public enum ResponseStatus{
	    FAIL, TIMEOUT, SUCCESS
	}
	
	private ResponseStatus responseStatus;
	 
    private VTDGen document;

    private String src;

    @SuppressWarnings("unused")
    private WFSResponse() {
    }

    public WFSResponse(VTDGen document, String src, ResponseStatus responseStatus) {
    	this.responseStatus = responseStatus;
        this.document = document;
        this.src = src;
    }

    public VTDGen getDocument() {
        return document;
    }

    public String getSrc() {
        return src;
    }

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

}
