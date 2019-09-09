/**

* The MIT License (MIT)

* Copyright (c) 2019 RAIDAR

* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
**/

package edu.mit.cxsci.raidar.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author Mizanul H. Chowdhuury, MIT
 * @version 1.0
 **/

public class Utilities {
	private Properties props;
	private static Utilities instance = new Utilities();

	public Utilities() {
		String classpath = System.getProperty("java.class.path");
	}

	public static Utilities getInstance() {
		return instance;
	}

	public Properties getPropertyObject() {
		return props;
	}

	public void printFile(File file) throws IOException {

		if (file == null)
			return;

		try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
	}

	public String loadJSONTest(String path) {
		String result = null;
		ClassLoader classLoader = getClass().getClassLoader();

		try (InputStream inputStream = classLoader.getResourceAsStream(path)) {

			result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			// System.out.println(result);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public File getFileFromResources(String fileName) {

		ClassLoader classLoader = getClass().getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}

	}

	private boolean compareDate(String strDate, long maxTime) throws ParseException {
		Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
		final Date d2 = new Date();
		final long diff = d2.getTime() - d1.getTime();
		System.out.println("diff = " + diff);
		if (diff > maxTime)
			return true;
		else
			return false;
	}

	public Hashtable ParseCsvData(String csvFile) {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Hashtable<String, String> ht = new Hashtable<String, String>();

		try {
			File file = getFileFromResources(csvFile);
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {

				String[] country = line.split(cvsSplitBy);

				ht.put(country[0], country[1]);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ht;
	}

	public String FindMarcKey(String value) {
		System.out.println("VALUE========="+value);
		String strKey="";
		Hashtable<String,String> htRaider=getRaidarInfo("data/raidar_col_description.csv");
		Set<String> keys = htRaider.keySet();
		for (String key : keys) {
			//System.out.println("Value of " + key + " is: " + htRaider.get(key));
		}
		ArrayList<Hashtable> htList = FindMarcInfo("data/marc_raidar.csv");
		String tag=htRaider.get(value.trim());
		System.out.println("TAG========="+tag);

		Hashtable ht= htList.get(1);

		if(tag!=null) {
			strKey=(String)ht.get(tag);
		}
//		Set<String> keys = ht.keySet();
//		for (String key : keys) {
//			//System.out.println("Value of " + key + " is: " + ht.get(0).get(key));
//			if(value.equals(ht.get(key))){
//				//System.out.println("Key of " + key);
//				strKey=key;
//				break;
//			}
//		}
		return strKey;
	}

	public ArrayList<Hashtable> FindMarcInfo(String csvFile) {
		ArrayList<Hashtable> alist=new ArrayList<Hashtable>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Hashtable<String, String> ht_raidar = new Hashtable<String, String>();
		Hashtable<String, String> ht_marc = new Hashtable<String, String>();
		Hashtable<String, String> ht_marc2 = new Hashtable<String, String>();
		String marc_pointer="";
		try {
			File file = getFileFromResources(csvFile);
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {

				String[] data = line.split(cvsSplitBy,5);

				try {
					marc_pointer=data[0]+","+ data[2]+","+ data[3]+","+ data[4];
					ht_raidar.put(marc_pointer, data[1]);
					marc_pointer=data[0]+","+ data[2]+","+ data[3]+","+ data[4];
					ht_marc.put(data[0], marc_pointer);
					ht_marc2.put(data[0], data[1]+","+ data[2]+","+ data[3]+","+ data[4]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		alist.add(ht_raidar);
		alist.add(ht_marc);
		alist.add(ht_marc2);

		return alist;
	}

	public Hashtable<String,String> getRaidarInfo(String csvFile) {
		Hashtable<String,String> ht=new Hashtable<String,String>();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {
			File file = getFileFromResources(csvFile);
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {

				String[] data = line.split(cvsSplitBy);
				try {
					ht.put(data[0], data[1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ht;
	}

    public static String generateString(String input, String algorithm, int minLength) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] bytes = messageDigest.digest(input.getBytes());
        BigInteger integer = new BigInteger(1, bytes);
        String result = integer.toString(16);
        while (result.length() < minLength) {
            result = "0" + result;
        }
        return result;
    }

}
