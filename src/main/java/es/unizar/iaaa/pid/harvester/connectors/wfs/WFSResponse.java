package es.unizar.iaaa.pid.harvester.connectors.wfs;

import com.ximpleware.VTDGen;

public class WFSResponse {
    public static final WFSResponse FAIL = new WFSResponse();

    private VTDGen document;

    private String src;

    private WFSResponse() {
    }

    public WFSResponse(VTDGen document, String src) {
        this.document = document;
        this.src = src;
    }


    public boolean isInValid() {
        return document == null;
    }

    public VTDGen getDocument() {
        return document;
    }

    public String getSrc() {
        return src;
    }

}
