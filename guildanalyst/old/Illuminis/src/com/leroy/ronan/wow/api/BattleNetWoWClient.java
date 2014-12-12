package com.leroy.ronan.wow.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class BattleNetWoWClient {
	
	private String cachePath;
	private String bnetServer;

	public BattleNetWoWClient(String cachePath, String bnetServer){
		this.cachePath = cachePath;
		this.bnetServer = bnetServer;
	}
	
	public String gather(boolean reload, String path) throws Exception{
		return gather(reload, path, "");
	}

	public String gather(boolean reload, String path, String options) throws Exception{

		String res = "";
		
		File f = new File(cachePath+path+".json");

		if (reload || !f.exists()){
			f.getParentFile().mkdirs();
			if (!f.exists()) {
				f.createNewFile();
			}

			String url = "http://"+bnetServer+"/api/wow/"+path;
			if (options.length() > 0){
				url += "?"+options;
			}
			URL dataUrl = new URL(url.replaceAll(" ", "%20"));
			URLConnection conn = dataUrl.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine);
			}
			bw.close();
			br.close();
		}

		DataInputStream in = new DataInputStream(new FileInputStream(f));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			String line = strLine.trim();
			res += line;
		}
		in.close();
		
		return res;
	}
}
