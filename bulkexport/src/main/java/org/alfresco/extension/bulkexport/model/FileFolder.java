package org.alfresco.extension.bulkexport.model;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileFolder {
	
	private String basePath;
	private boolean escapeExported;
	
	public FileFolder(String basePath, boolean escapeExported){
		this.basePath = basePath;
		this.escapeExported = escapeExported;
	}
	
	public void createFolder(String path) throws Exception{
		path = this.basePath + path;
		String[] pastas = path.split("/");
		String raiz = pastas[0].toString() + "/";
		
		for(int i=0 ; i<pastas.length;i++){
			if(i>0){
				File dir = new File(raiz + pastas[i].toString());
				
				if(!dir.exists()){
					dir.mkdir();
				}
				raiz = raiz + pastas[i].toString() + "/";
			}
		}
	}
	
	private void createFile (String filePath) throws Exception {	
		File f=new File(filePath);
		
		try {  
			if(!f.exists()){
			  f.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String createXmlFile(String filePath) throws Exception {
		String fp = filePath + ".metadata.properties.xml";
		
		this.createFile(fp);
		
		return fp;
	}
	
	public void insertFileContent (ByteArrayOutputStream out, String filePath) throws Exception {
		filePath = this.basePath + filePath;
		
		if(this.isFileExist(filePath) && this.escapeExported){
			return;
		}
		
		this.createFile(filePath);
		
		try {
			FileOutputStream output = new FileOutputStream(filePath);
			output.write(out.toByteArray());
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertFileProperties(String type, List<String> aspects,Map<String, String> properties, String filePath) throws Exception{
		filePath = this.basePath + filePath;
		
		if(this.isFileExist(filePath) && this.isFileExist(filePath + ".metadata.properties.xml") && this.escapeExported){
			return;
		}
		
		
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n<properties>";
		String footer = "\n</properties>";
		
		String tType = "<entry key=\"type\">" + type + "</entry>";
		String tAspect = "<entry key=\"aspects\">" + this.formatAspects(aspects) + "</entry>";
		
		String text = "\n\t" + tType + "\n\t" + tAspect;
		
		Set<String> set = properties.keySet();
		
		for (String string : set) {
			String key = string;
			String value = properties.get(key);
			
			
			value = this.formatProperty(value);
			
			
			text += "\n\t<entry key=\"" + key +"\">" + value + "</entry>";
		}
		
		try {
			String fp = this.createXmlFile(filePath);
			File file = new File(fp);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
			
			StringBuilder builder = new StringBuilder();
			builder.append(header);
			builder.append(text);
			builder.append(footer);
			
			bw.write(builder.toString());
			bw.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String formatAspects(List<String> aspects){
		
		String dado = "";
		
		boolean flag = false;
		for (String string : aspects) {
			if(flag){
				dado += ",";
			}
			
			dado += string;
			flag = true;
		}
		
		return dado;
	}
	
	private String formatProperty(String value){		
		
		//format &
		value = value.replaceAll("&", "&amp;");
		//format < and >
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		
		return value;
	}
	
	private boolean isFileExist(String path){
		File f=new File(path);
		
		if(f.exists()){
		  return true;
		}

		return false;
	}
}
