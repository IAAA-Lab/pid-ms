package es.unizar.iaaa.pid.web.rest.util;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

public class MvcResultUtils {
    public static Long extractIdFromLocation(MvcResult result) {
        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        List<String> segments = UriComponentsBuilder.fromUriString(location).build().getPathSegments();
        String last = segments.get(segments.size()-1);
        return Long.parseLong(last);
    }

    public static UUID extractUuidFromLocation(MvcResult result) {
        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        List<String> segments = UriComponentsBuilder.fromUriString(location).build().getPathSegments();
        String last = segments.get(segments.size()-1);
        return UUID.fromString(last);
    }

}
