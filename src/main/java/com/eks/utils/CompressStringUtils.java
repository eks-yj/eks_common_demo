package com.eks.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressStringUtils {
	public static String compress(String uncompressedString, String inputCharsetName, String outputCharsetName) throws IOException {
		if (uncompressedString == null || "".equals(uncompressedString)) {
			return uncompressedString;
		}
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
			byte[] byteArray = uncompressedString.getBytes(inputCharsetName);
			gzipOutputStream.write(byteArray);
			gzipOutputStream.close();
			return byteArrayOutputStream.toString(outputCharsetName);
		}finally {
			if(byteArrayOutputStream != null){
				byteArrayOutputStream.close();
			}
		}
	}
	public static String decompress(String compressedString, String inputCharsetName, String outputCharsetName) throws IOException {
		if (compressedString == null || "".equals(compressedString)) {
			return compressedString;
		}
		ByteArrayOutputStream byteArrayOutputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;
		GZIPInputStream gzipInputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			byteArrayInputStream = new ByteArrayInputStream(compressedString.getBytes(inputCharsetName));
			gzipInputStream = new GZIPInputStream(byteArrayInputStream);
			byte[] bufferByteArray = new byte[1024];
			int readLengthInt = 0;
			while ((readLengthInt = gzipInputStream.read(bufferByteArray)) >= 0) {
				byteArrayOutputStream.write(bufferByteArray, 0, readLengthInt);
			}
			return byteArrayOutputStream.toString(outputCharsetName);
		}finally {
			if(gzipInputStream != null){
				gzipInputStream.close();
			}
			if(byteArrayInputStream != null){
				byteArrayInputStream.close();
			}
			if(byteArrayOutputStream != null){
				byteArrayOutputStream.close();
			}
		}
	}
}
