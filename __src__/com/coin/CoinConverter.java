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
 * @author Rodrigo Santos Reis
 * @version 1.0
 * @since 31-08-2017
 *
 */
@CronapiMetaData(category = CategoryType.CONVERSION, categoryTags = { "Conversão", "Convert" })
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

	@CronapiMetaData(type = "function", name = "Conversor de moedas", nameTags = {
			"ConverterCoin" }, description = "Função que converte um valor em uma moeda para outra. Exemplo: Quanto vale 1 USD em BRL?", returnType = ObjectType.DOUBLE)
	public static Var converter(

			@ParamMetaData(type = ObjectType.STRING, description = "Moeda original", blockType = "util_dropdown", keys = {
					"USD", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD", "HRK", "HUF", "IDR",
					"ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD",
					"THB", "TRY", "ZAR", "EUR" }, values = { "USD", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK",
							"DKK", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK",
							"NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "ZAR", "EUR" }) Var base,
			@ParamMetaData(type = ObjectType.DOUBLE, description = "Valor") Var value,
			@ParamMetaData(type = ObjectType.STRING, description = "Valor obtido em", blockType = "util_dropdown", keys = {
					"USD", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD", "HRK", "HUF", "IDR",
					"ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD",
					"THB", "TRY", "ZAR", "EUR" }, values = { "USD", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK",
							"DKK", "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK",
							"NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD", "THB", "TRY", "ZAR",
							"EUR" }) Var baseToConvert)
			throws Exception {
		Var content = getContentFromAPI(base.getObjectAsString());
		Gson c = new Gson();
		LinkedTreeMap map = c.fromJson(content.getObjectAsString(), Map.class);
		LinkedTreeMap rates = c.fromJson(map.get("rates").toString(), Map.class);
		double result = Double.parseDouble(rates.get(baseToConvert.getObjectAsString()).toString())
				* value.getObjectAsDouble();
		return new Var(result);
	}
}
