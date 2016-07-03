package com.drizzard.annihilationdw.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;

/*
 * Util by ThunderWaffeMC
 * http://bukkit.org/threads/util-unload-delete-copy-worlds.182814/
 */

public class CopyWorld {
	
	public static void copyWorld(File source, File target){
	    try {

		    FileFilter filter = new FileFilter() {
			    @Override
			    public boolean accept(File pathname) {
				    return !(pathname.getName().equals("uid.dat") || pathname.getName().equals("session.dat"));
			    }
		    };

		    FileUtils.copyDirectory(source, target,filter);

//	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
//	        if(!ignore.contains(source.getName())) {
//	            if(source.isDirectory()) {
//	                if(!target.exists())
//	                target.mkdirs();
//	                String files[] = source.list();
//	                for (String file : files) {
//	                    File srcFile = new File(source, file);
//	                    File destFile = new File(target, file);
//	                    copyWorld(srcFile, destFile);
//	                }
//	            } else {
//	                InputStream in = new FileInputStream(source);
//	                OutputStream out = new FileOutputStream(target);
//	                byte[] buffer = new byte[1024];
//	                int length;
//	                while ((length = in.read(buffer)) > 0)
//	                    out.write(buffer, 0, length);
//	                in.close();
//	                out.close();
//	            }
//	        }
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	}

}
