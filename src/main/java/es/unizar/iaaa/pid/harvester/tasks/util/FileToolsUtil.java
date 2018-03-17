package es.unizar.iaaa.pid.harvester.tasks.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileToolsUtil {

	private static final int	TIMEOUT		= 20000;
	private static final int	BUFFER_SIZE	= 102400;									// 100Kb

	private static final Logger LOGGER = LoggerFactory.getLogger(FileToolsUtil.class);

	public static boolean downloadFileHTTP(String url, String pathFile) {
		HttpClientParams clientParams = new HttpClientParams();
        clientParams.setSoTimeout(TIMEOUT);
        clientParams.setConnectionManagerTimeout(TIMEOUT);
        //clientParams.setContentCharset("UTF-8");
        HttpClient client = new HttpClient(clientParams);
        HttpMethodBase http = new GetMethod(url);

		try {

			client.executeMethod(http);

            File downloadFile = new File(pathFile);
            File dir = new File (downloadFile.getParent());
            dir.mkdirs();

			try(InputStream responseInputStream = http.getResponseBodyAsStream();
                FileOutputStream fos = new FileOutputStream(pathFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buff = new byte[BUFFER_SIZE];
                int read = responseInputStream.read(buff);
                while (read != -1) {
                    bos.write(buff, 0, read);
                    read = responseInputStream.read(buff);
                }
                bos.flush();
                fos.flush();
            }

			return true;
		}
		catch (Exception e) {
			// System.err.println("Error descargando el fichero.");
			// e1.printStackTrace();

			LOGGER.error("Error downloading the file " + url, e);
			File f = new File(pathFile);
			if (f.exists() && f.isFile()) {
				f.delete();
			}
			return false;
		}
		finally {
            http.releaseConnection();
        }

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
