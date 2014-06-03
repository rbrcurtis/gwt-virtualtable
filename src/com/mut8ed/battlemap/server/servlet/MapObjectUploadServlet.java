package com.mut8ed.battlemap.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mut8ed.battlemap.server.dao.Dao;
import com.mut8ed.battlemap.server.util.Util;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.exception.DuplicateImageFileException;

public class MapObjectUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -7688218457146464379L;
	public static final Logger logger = Logger.getLogger(MapObjectUploadServlet.class);
	public static final String FILE_PATH = "/var/www/html/images";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		resp.getOutputStream().print("you totally suck");
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {

		PrintStream out = new PrintStream(resp.getOutputStream());

		try {
			FileItemFactory factory = new DiskFileItemFactory();
			//			factory.setSizeThreshold(yourMaxMemorySize);
			//			factory.setRepository(yourTempDirectory);
			ServletFileUpload upload = new ServletFileUpload(factory);
			Map<String,FileItem> files = new HashMap<String, FileItem>();
			Map<String,String> params = new HashMap<String, String>();
			List<FileItem> items = (List<FileItem>)upload.parseRequest(req);
			for (FileItem item : items){
				if (item.getName()==null && item.getString()==null)continue;
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					logger.debug("param:"+name+"="+value);
					params.put(name,value);
				} else {
					logger.debug(item.getFieldName()+"/"+item.getName()+" was recieved on upload");
					files.put(item.getFieldName(),item);
				}
			}

			if (params.get("object-type")==null){
				//hax!
				logger.fatal("total hax!");
				logger.fatal(req.getParameterMap());
				@SuppressWarnings("rawtypes")
				Enumeration enumeration = req.getAttributeNames();
				while (enumeration.hasMoreElements()){
					logger.fatal(enumeration.nextElement());
				}
				return;
				//				return;
			} else {
				MapObjectType type = MapObjectType.valueOf(params.get("object-type"));
				switch (type){
				case TILE:
					processObjectUpload(type,params,files);
					break;
				case DECAL:
					processObjectUpload(type,params,files);
					break;
				default:
					logger.error("unsupported type:"+type);
					break;
				}
			}


			for (FileItem item : files.values()){
				item.delete();
			}

			out.println("Object Added Successfully");

		} catch (DuplicateImageFileException e){
			out.println("This image is a duplicate of an existing image.");
		} catch (Exception e) {
			if (Logger.getRootLogger().getLevel().equals(Level.DEBUG))e.printStackTrace(out);
			out.println(e.getMessage());
			logger.error(e,e);
		}



	}

	private void processObjectUpload(MapObjectType type, Map<String, String> params,
			Map<String, FileItem> files) throws Exception {
		
		//TODO need security

		FileItem tmpFile = files.get("objectImage");
		if (tmpFile!=null && tmpFile.getName().trim().length()>0){
			String fn = tmpFile.getName();
			String ext = fn.substring(fn.lastIndexOf("."));
			File tmpImage = new File(FILE_PATH+"/tmp/"+fn);
			logger.debug(tmpImage.getAbsolutePath());
			if (tmpImage.exists())tmpImage.delete();
			if (tmpImage.createNewFile()){
				//			logger.error("couldn't create temp image file "+tmpImage.getAbsolutePath());
				//			throw new RuntimeException("Unexpected failure writing image file.");
			}
			tmpFile.write(tmpImage);
			if (!Util.isImage(tmpImage)){
				logger.error(tmpImage.getAbsolutePath()+" isn't an image.");
			}
			String md5 = Util.getFileMD5(tmpImage.getAbsolutePath());
			Dao dao = new Dao();
			if (dao.isDuplicateImage(md5)){
				throw new DuplicateImageFileException();
			}

			String first = md5.substring(0,1);
			File dir = new File(FILE_PATH+"/"+first);
			logger.debug("using dir "+dir.getAbsolutePath());
			if (!dir.exists()){
				if (!dir.mkdir()){
					logger.error("couldn't create image dir "+dir.getAbsolutePath());
					throw new RuntimeException("Unexpected failure writing image file.");
				}
			}
			File dest = new File(dir.getAbsolutePath()+"/"+md5+ext);
			if (!tmpImage.renameTo(dest)) {
				logger.error("couldn't move image to final destination "+dest.getAbsolutePath());
				throw new RuntimeException("Unexpected failure writing image file.");
			}
			new Dao().insertObject(type, params, dest.getAbsolutePath(), md5);
		} else {
			new Dao().insertObject(type, params, null, null);	
		}

	}



}
