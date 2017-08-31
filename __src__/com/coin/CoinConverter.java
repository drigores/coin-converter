package com.coin;

import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import cronapi.CronapiMetaData;
import cronapi.ParamMetaData;
import cronapi.Var;
import cronapi.CronapiMetaData.CategoryType;
import cronapi.CronapiMetaData.ObjectType;

/**
 * Classe que representa ...
 * 
 * @author ${UserName}
 * @version 1.0
 * @since ${.now?string("yyyy-MM-dd")}
 *
 */
@CronapiMetaData(category = CategoryType.UTIL, categoryTags = { "Util" })
public class CoinConverter {

	public static Var getContentFromAPI(String base) throws Exception {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://api.fixer.io/latest?base=" + base);
		Var toReturn;
		HttpResponse httpResponse = httpClient.execute(httpGet);
		Scanner scanner = new Scanner(httpResponse.getEntity().getContent(), cronapi.CronapiConfigurator.ENCODING);
		String response = "";
		try {
			response = scanner.useDelimiter("\\A").next();
		} catch (Exception e) {
		}
		scanner.close();
		toReturn = new Var(response);
		httpGet.completed();
		return toReturn;
	}

	@CronapiMetaData(type = "function", name = "{{ConverterCoin}}", nameTags = {
			"ConverterCoin" }, description = "{{ConverterCoin}}", returnType = ObjectType.DOUBLE)
	public static Var converter(@ParamMetaData(type = ObjectType.STRING, description = "{{base}}") Var base,
			@ParamMetaData(type = ObjectType.DOUBLE, description = "{{value}}") Var value,
			@ParamMetaData(type = ObjectType.STRING, description = "{{baseTo}}") Var baseToConvert) throws Exception {
		Var content = getContentFromAPI(base.getObjectAsString());
		Gson c = new Gson();
		LinkedTreeMap map = c.fromJson(content.getObjectAsString(), Map.class);
		LinkedTreeMap rates = c.fromJson(map.get("rates").toString(), Map.class);
		return new Var(Double.parseDouble(rates.get(baseToConvert.getObjectAsString()).toString()));
	}
}
