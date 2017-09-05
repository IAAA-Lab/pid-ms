package es.unizar.iaaa.pid.harvester.connectors.wfs;

import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.Source;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class WFSClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WFSClient.class);

    static String createWfsGetFeatureById(Source source, Identifier identifier) {
        return source.getEndpointLocation() + "?service=WFS&version=2.0.0&request=GetFeature&"
                + "STOREDQUERY_ID=urn:ogc:def:query:OGC-WFS::GetFeatureById&ID=" + identifier.getAlternateId();
    }

    static String createWfsGetFeatureRequestBodyPost(String feature,Source source, BoundingBox boundingBox, String type) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wfs:GetFeature xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs/2.0\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" "
                + "xmlns:"+source.getSchemaPrefix()+"=\""+source.getSchemaUri()+"\" xmlns:gml=\""+source.getSchemaUriGML()+"\" "
                + "xmlns:xlink=\"http://www.w3.org/1999/xlink\"	service=\"WFS\" version=\"2.0.0\" resultType=\"" + type + "\" "
                + "startIndex=\"0\" count=\"" + source.getFeaturesThreshold() + "\">"
                + "<wfs:Query typeNames=\"" + source.getSchemaPrefix() + ":" + feature + "\">"
                + "<fes:Filter><fes:Intersects><fes:ValueReference>" + source.getGeometryProperty() + "</fes:ValueReference>"
                + "<gml:Envelope srsName=\""+source.getSrsName()+"\"><gml:lowerCorner>" + boundingBox.getMinY() + " " + boundingBox.getMinX() + "</gml:lowerCorner>"
                + "<gml:upperCorner>" + boundingBox.getMaxY() + " " + boundingBox.getMaxX() + "</gml:upperCorner></gml:Envelope></fes:Intersects></fes:Filter>"
                + "</wfs:Query></wfs:GetFeature>";
    }

    static String createWfsGetFeatureRequestGet(String feature, Source source, BoundingBox boundingBox, String type){
    	return source.getEndpointLocation() + "?request=GetFeature&service=WFS&typeNames=" 
    			+ source.getSchemaPrefix() + ":" + feature + "&resultType=" + type +"&version=2.0.0&srsName=" 
    			+ source.getSrsName() +"&BBOX="+ boundingBox.getMinX() +","+ boundingBox.getMinY() 
    			+ "," + boundingBox.getMaxX() + ","+ boundingBox.getMaxY() + "&startIndex=0&count=" 
    			+ source.getFeaturesThreshold();
    }

    public static WFSResponse executeRequestPOST(String endpoint, String body) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_XML);
        try{
        	CloseableHttpResponse response = httpclient.execute(RequestBuilder.post(endpoint).setEntity(entity).build());
        	WFSResponse wfsResponse = extractEntity(endpoint, response);
        	response.close();
            return wfsResponse;
        } catch (IOException e) {
            LOGGER.error("Error for POST request {}", body, e);
            return WFSResponse.FAIL;
        } catch (ParseException e) {
            LOGGER.error("Error for POST request {}", body, e);
            return WFSResponse.FAIL;
        }
    }

    public static WFSResponse executeRequestGET(String uri) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try{
        	CloseableHttpResponse response = httpclient.execute(RequestBuilder.get(uri).build());
        	WFSResponse wfsResponse = extractEntity(uri, response);
        	response.close();
            return wfsResponse;
        } catch (IOException e) {
            return WFSResponse.FAIL;
        } catch (ParseException e) {
            return WFSResponse.FAIL;
        }
    }

    private static WFSResponse extractEntity(String endpoint, CloseableHttpResponse response) throws IOException, ParseException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        LOGGER.info("Retrieved {} bytes from {}", result.length(), endpoint);
        return new WFSResponse(getDocument(result.toString()), result.toString());
    }

    private static VTDGen getDocument(String s) throws ParseException {
        byte[] bytes;
        try {
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        VTDGen vg = new VTDGen();
        vg.setDoc(bytes);
        try {
            vg.parse(true);
        } catch (ParseException e) {
            LOGGER.error("Error parsing response\n\n{}", s, e);
        }
        return vg;
    }

//    private static Document getDocument(String results) {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setNamespaceAware(true);
//        DocumentBuilder builder;
//        try {
//            builder = factory.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            LOGGER.info("Fallo por no configuración en DocumentBuilderFactory");
//            return null;
//        }
//        try {
//            return builder.parse(new InputSource(new StringReader(results)));
//        } catch (SAXException e) {
//            LOGGER.info("Fallo por al procesar la entidad");
//            return null;
//        } catch ( IOException e1){
//        	LOGGER.info("Fallo por al procesar la entidad");
//            return null;
//        }
//    }
}
