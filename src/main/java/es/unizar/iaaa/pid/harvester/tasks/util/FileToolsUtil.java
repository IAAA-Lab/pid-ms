package es.unizar.iaaa.pid.harvester.tasks.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileToolsUtil {

	private static final int	TIMEOUT		= 20000;
	private static final int	BUFFER_SIZE	= 102400;									// 100Kb

	private static final Logger LOGGER = LoggerFactory.getLogger(FileToolsUtil.class);

	public static boolean downloadFileHTTP(String url, String pathFile) {
        File downloadFile = new File(pathFile);
        File dir = new File (downloadFile.getParent());
        dir.mkdirs();

		RequestConfig config = RequestConfig.custom()
            .setSocketTimeout(TIMEOUT)
            .setConnectionRequestTimeout(TIMEOUT)
            .setConnectTimeout(TIMEOUT)
            .build();

		HttpGet request = new HttpGet(url);
		request.setConfig(config);
        CloseableHttpClient client = HttpClients.createDefault();
        try(CloseableHttpResponse response = client.execute(request)){
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try(FileOutputStream fos = new FileOutputStream(pathFile);
                    InputStream is = entity.getContent()) {
                    byte[] buff = new byte[BUFFER_SIZE];
                    int inByte;
                    while((inByte = is.read(buff)) != -1) {
                        fos.write(buff, 0, inByte);
                    }
                }
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Error downloading the file " + url, e);
            File f = new File(pathFile);
            if (f.exists() && f.isFile()) {
                f.delete();
            }
        }
        return false;
	}

	public static boolean unzipFile(File zipFile, String unZipDirectory){

		byte[] buffer = new byte[1024];

		try(FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(fis)
        ){
			//create output directory is not exists
			File folder = new File(unZipDirectory);
			if(!folder.exists()){
				folder.mkdir();
			}

			//get the zip file content
			//get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while(ze!=null){
				if(!ze.isDirectory()){
					//create parents directories
					String fileName = ze.getName();

					File newFile = new File(unZipDirectory + File.separator + fileName);
					newFile.getParentFile().mkdirs();

					try(FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }

				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();

			return true;

		}
		catch(Exception e){
			LOGGER.error("Error descomprimiendo fichero " + zipFile.getAbsolutePath(),e);
			return false;
		}
	}

	public static String getFilePath(File dir, String fileName){
		if(dir.exists() && dir.isDirectory()){
			File[] fileList = dir.listFiles();

            for (File file : fileList) {
                if (file.isFile()) {
                    if (file.getName().equals(fileName)) {
                        return file.getAbsolutePath();
                    }
                } else if (file.isDirectory()) {
                    String result = getFilePath(file, fileName);
                    if (!result.equals("")) {
                        return result;
                    }
                }
            }
		}
		return "";
	}

	public static void deleteDirectory(File directory){
		File[] entries = directory.listFiles();
		for(File entry : entries){
			if(entry.isDirectory()){
				deleteDirectory(entry);
			}
			entry.delete();
		}
		directory.delete();
	}

}
