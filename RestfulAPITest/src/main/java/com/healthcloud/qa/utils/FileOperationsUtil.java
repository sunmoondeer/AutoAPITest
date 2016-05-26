package com.healthcloud.qa.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileOperationsUtil {

   public static File findAndReplaceText(String oldfileAbsPath, String newFileAbsPath, HashMap inputMap) throws IOException {
      // read input file into a byte array
      byte[] pInput = readFile(oldfileAbsPath);

      String strInput = new String(pInput);

      for (Object findText : inputMap.keySet())
      {
         strInput = strInput.replaceAll(findText.toString(), inputMap.get(findText).toString());

// commenting this since it does multiple replacements
         //This is from old code, believe this is for Resource file where search text could be in upper case
         //strInput = strInput.replaceAll(findText.toString().toUpperCase(), inputMap.get(findText).toString().toUpperCase());
      }

      // write the output string to new file
      writeFile(newFileAbsPath, strInput.getBytes());

      return new File(newFileAbsPath);
   }

	public static final byte[] readFile(String strFile) throws IOException {
		int nSize = 32768;
		// open the input file stream
		BufferedInputStream inStream = new BufferedInputStream(
				new FileInputStream(strFile), nSize);
		byte[] pBuffer = new byte[nSize];
		int nPos = 0;
		// read bytes into a buffer
		nPos += inStream.read(pBuffer, nPos, nSize - nPos);
		// while the buffer is filled, double the buffer size and read more
		while (nPos == nSize) {
			byte[] pTemp = pBuffer;
			nSize *= 2;
			pBuffer = new byte[nSize];
			System.arraycopy(pTemp, 0, pBuffer, 0, nPos);
			nPos += inStream.read(pBuffer, nPos, nSize - nPos);
		}
		// close the input stream
		inStream.close();
		if (nPos == 0) {
			return "".getBytes();
		}
		// return data read into the buffer as a byte array
		byte[] pData = new byte[nPos];
		System.arraycopy(pBuffer, 0, pData, 0, nPos);
		return pData;
	}

	// helper function to write a byte array into a file
	public static final void writeFile(String strFile, byte[] pData)
			throws IOException {
		BufferedOutputStream outStream = new BufferedOutputStream(
				new FileOutputStream(strFile), 32768);
		if (pData.length > 0)
			outStream.write(pData, 0, pData.length);
		outStream.close();
	}
	
	public static void deleteFile(File deleteFile) throws IllegalArgumentException {
		File objFile = new File(deleteFile.getAbsolutePath());
		System.out.println("#### deleteFile(): Absolute path: "+objFile.getAbsolutePath());
		// Make sure the file or directory exists and isn't write protected
		if (!objFile.exists()){
			System.out.println("deleteFile():no such file or directory");
			throw new IllegalArgumentException(
					"Delete: no such file or directory: " + objFile.getName());
		}

		if (!objFile.canWrite()){
			System.out.println("deleteFile():write protected");
			throw new IllegalArgumentException("Delete: write protected: "
					+ objFile.getName());
		}

		// If it is a directory, make sure it is empty
		if (objFile.isDirectory()) {
			System.out.println("deleteFile(): Dir");
			String[] files = objFile.list();
			if (files.length > 0)
				throw new IllegalArgumentException(
						"Delete: directory not empty: " + objFile.getName());
		}

		// Attempt to delete it
		boolean success = objFile.delete();
		System.out.println("deleteFile(): success: "+success);

		if (!success)
			throw new IllegalArgumentException("Delete: deletion failed");

	}

    public static void copyDirectory(File srcPath, File dstPath) throws IOException {
	    if (srcPath.isDirectory()) {
	    	if (!dstPath.exists()) {
	    		dstPath.mkdir();
	    	}
	
	    	String files[] = srcPath.list();
	    	for(int i = 0; i < files.length; i++) {
	    		copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
	    	}
	    } else {
	    	if(!srcPath.exists()) {
	    		throw new FileNotFoundException("File or directory does not exist.");
	    	} else {
	    		InputStream in = new FileInputStream(srcPath);
	    		OutputStream out = new FileOutputStream(dstPath);
	
	    		// Transfer bytes from in to out
	    		byte[] buf = new byte[1024];
	    		int len;
	    		while ((len = in.read(buf)) > 0) {
	    			out.write(buf, 0, len);
	    		}
	    		in.close();
	    		out.close();
	    	}
	    }
	    System.out.println("Directory copied.");
    }

    private static void copyInputStream(InputStream in, OutputStream out) throws IOException {
	   byte[] buffer = new byte[1024];
	   int len;

	   while((len = in.read(buffer)) >= 0)
	       out.write(buffer, 0, len);

	   in.close();
	   out.close();
	}

    public static String unzipToTarget (String zipFileWithFullPath, String targetLocWithFullPath) throws Exception {
        Enumeration entries;
        ZipFile zipFile;

        boolean rootPathFound = false;
        String rootPath = null;

        if (zipFileWithFullPath == null || zipFileWithFullPath.trim().equals("")) {
            throw new Exception ("Invalid zip file");
        }

        try {
            zipFile = new ZipFile(zipFileWithFullPath);

            entries = zipFile.entries();

            String topLevelPath = null;

            while (entries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry)entries.nextElement();

                if (entry.isDirectory()) {

                    // Assume directories are stored parents first then children.
                    System.out.println("Extracting directory: " + entry.getName());

                    // This is not robust, just for demonstration purposes.
                    topLevelPath = targetLocWithFullPath + File.separator + entry.getName();

                    if (rootPathFound == false) {
                        rootPath = topLevelPath;
                        rootPathFound = true;
                        System.out.println("rootPath::" + rootPath);
                        System.out.println("rootPathFound::" + rootPathFound);
                    }

                    // create dir
                    (new File(topLevelPath)).mkdir();
                    continue;
                }

                System.out.println("Extracting file: " + targetLocWithFullPath + File.separator + entry.getName());
                File f = new File(targetLocWithFullPath + File.separator + entry.getName());
                f.createNewFile();
                copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream (new FileOutputStream(f)));
            }

            zipFile.close();

        } catch (IOException ioe) {

            System.out.println("Unhandled exception::" + ioe.getMessage());
            ioe.printStackTrace();
        }
        return rootPath;
    }

    public static String copyFile(String fileName){
    	String userDir = System.getProperty("user.dir");
    	String oldFile = userDir + File.separator + fileName;
    	System.out.println("old File: " + oldFile);
    	String testReportDir = userDir + File.separator + "testReport" + File.separator + TimeUtil.getCurrentTime() + fileName;
    	String newFile = testReportDir.replace("Result.xls",".xls");
    	System.out.println(" newFile: " + newFile);
       	String recString = "未执行复制！";
           try { 
               int bytesum = 0; 
               int byteread = 0; 
               File oldfile = new File(oldFile); 

               if (oldfile.exists()) { //文件存在时 
                   InputStream inStream = new FileInputStream(oldFile); //读入原文件 
                   FileOutputStream files = new FileOutputStream(newFile); 
                   byte[] buffer = new byte[1444]; 
                   //int length; 
                   while ( (byteread = inStream.read(buffer)) != -1) { 
                       bytesum += byteread; //字节数 文件大小 
                       files.write(buffer, 0, byteread); 
                       
                   } 
                   inStream.close(); 
                   files.close();
                   recString = "文件复制成功！";
               } 
           } 
           catch (Exception e) { 
               recString = "文件复制异常失败！";
               e.printStackTrace(); 

           } 
    	return recString;
       } 
    public static void main(String[] args) {
    	FileOperationsUtil.copyFile("Http_Request_workbook_Data.xlsx");
    }
}
