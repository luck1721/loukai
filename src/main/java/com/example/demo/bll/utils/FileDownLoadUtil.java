package com.example.demo.bll.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * @author lk
 * @date 2020/6/13
 */
public class FileDownLoadUtil {

	public void download() throws IOException {
		File file = new File("D/123.txt");
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bs = IOUtils.toByteArray(inputStream);
	}

	/**
	 * inputstream转string
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2Str(InputStream is) throws IOException {
		StringBuffer sb;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));

			sb = new StringBuffer();

			String data;
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
		} finally {
			br.close();
		}

		return sb.toString();
	}

}
