package es.unizar.iaaa.pid.harvester.connectors.wfs;

import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import es.unizar.iaaa.pid.domain.BoundingBox;
import es.unizar.iaaa.pid.domain.Feature;
import es.unizar.iaaa.pid.domain.Identifier;
import es.unizar.iaaa.pid.domain.Source;
import es.unizar.iaaa.pid.harvester.connectors.wfs.WFSResponse.ResponseStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

public class WFSClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WFSClient.class);

    private WFSClient(){
    }

    static String createWfsGetFeatureById(Source source, Identifier identifier) {
        return source.getEndpointLocation() + "?service=WFS&version=2.0.0&request=GetFeature&"
                + "STOREDQUERY_ID=urn:ogc:def:query:OGC-WFS::GetFeatureById&ID=" + identifier.getAlternateId();
    }

    static String createWfsGetFeatureRequestBodyPost(Feature feature, BoundingBox boundingBox, String type) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wfs:GetFeature xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:schemaLocation=\"http://www.opengis.net/wfs/2.0 http://schemas.opengis.net/wfs/2.0/wfs.xsd\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs/2.0\" xmlns:fes=\"http://www.opengis.net/fes/2.0\" "
                + "xmlns:"+feature.getSchemaPrefix()+"=\""+feature.getSchemaUri()+"\" xmlns:gml=\""+feature.getSchemaUriGML()+"\" "
                + "xmlns:xlink=\"http://www.w3.org/1999/xlink\"	service=\"WFS\" version=\"2.0.0\" resultType=\"" + type + "\" "
                + "startIndex=\"0\" count=\"" + feature.getFeaturesThreshold() + "\">"
                + "<wfs:Query typeNames=\"" + feature.getSchemaPrefix() + ":" + feature.getFeatureType() + "\">"
                + "<fes:Filter><fes:Intersects><fes:ValueReference>" + feature.getGeometryProperty() + "</fes:ValueReference>"
                + "<gml:Envelope srsName=\""+feature.getSrsName()+"\"><gml:lowerCorner>" + boundingBox.getMinY() + " " + boundingBox.getMinX() + "</gml:lowerCorner>"
                + "<gml:upperCorner>" + boundingBox.getMaxY() + " " + boundingBox.getMaxX() + "</gml:upperCorner></gml:Envelope></fes:Intersects></fes:Filter>"
                + "</wfs:Query></wfs:GetFeature>";
    }

    static String createWfsGetFeatureRequestGet(Feature feature,Source source, BoundingBox boundingBox, String type){
    	return source.getEndpointLocation() + "?request=GetFeature&service=WFS&typeNames="
    			+ feature.getSchemaPrefix() + ":" + feature.getFeatureType()  + "&resultType=" + type +"&version=2.0.0&srsName="
    			+ feature.getSrsName() +"&BBOX="+ boundingBox.getMinY() +","+ boundingBox.getMinX()
    			+ "," + boundingBox.getMaxY() + ","+ boundingBox.getMaxX() + "&startIndex=0&count="
    			+ feature.getFeaturesThreshold();
    }

    public static WFSResponse executeRequestPOST(String endpoint, String body) {
        HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_XML);
        HttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute(HttpClientContext.REQUEST_CONFIG, RequestConfig.custom().setSocketTimeout(45000).build());

        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(RequestBuilder.post(endpoint).setEntity(entity).build(),httpContext)
        ) {
            return extractEntity(endpoint, response);
        } catch(SocketTimeoutException e){
        	return new WFSResponse(null,null,ResponseStatus.TIMEOUT);
        } catch (IOException e) {
            LOGGER.error("Error for POST request {}", body, e);
            return WFSResponse.FAIL;
        }
    }

    public static WFSResponse executeRequestGET(String uri) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault();
             CloseableHttpResponse response = httpclient.execute(RequestBuilder.get(uri).build())) {
            return extractEntity(uri, response);
        } catch (IOException e) {
            return WFSResponse.FAIL;
        }
    }

    private static WFSResponse extractEntity(String endpoint, CloseableHttpResponse response) throws IOException {
    	if(response.getStatusLine().toString().contains("504")){
			return new WFSResponse(null,null,ResponseStatus.TIMEOUT);
		}

        StringBuilder result = new StringBuilder();
		try(InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
            BufferedReader rd = new BufferedReader(isr)) {

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        }
        LOGGER.info("Retrieved {} bytes from {}", result.length(), endpoint);
		VTDGen document = getDocument(result.toString());
		return new WFSResponse(document, result.toString(), ResponseStatus.SUCCESS);

    }

    private static VTDGen getDocument(String s) {
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
}
