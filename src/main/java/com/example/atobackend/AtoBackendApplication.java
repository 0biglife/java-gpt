package com.example.atobackend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AtoBackendApplication {

	private static final String API_URL = "https://api.openai.com/v1/chat/completions";
	private static final String OPENAI_KEY = "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"; // env 뺄 것
	private static final String MODEL = "gpt-4-0125-preview";
//	public static void main(String[] args) {
//		System.out.println(chatGPT2("who are you?"));
//	}

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("GPT와 대화를 시작합니다. (종료하려면 'exit' 입력)");
			while (true) {
				System.out.print("You: ");
				String input = scanner.nextLine();
				if ("exit".equalsIgnoreCase(input.trim())) {
					break;
				}
				String response = chatGPT(input);
				System.out.println("GPT: " + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String extractContentFromResponse(String jsonResponse) {
		JSONObject responseObj = new JSONObject(jsonResponse);
		JSONArray choices = responseObj.getJSONArray("choices");
		if (choices.length() > 0) {
			JSONObject firstChoice = choices.getJSONObject(0);
			JSONObject message = firstChoice.getJSONObject("message");
			return message.getString("content");
		} else {
			return "No response found";
		}
	}

	public static String chatGPT(String message) throws Exception {
		URL url = new URL(API_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Authorization", "Bearer " + OPENAI_KEY);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		String jsonInputString = String.format("{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}", MODEL, message);

		try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
			writer.write(jsonInputString);
			writer.flush();
		}

		StringBuilder response = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		}

		// 여기서 응답 JSON에서 content 값을 파싱합니다. 실제로는 JSON 파싱 라이브러리를 사용하는 것이 좋습니다.
		return extractContentFromResponse(response.toString());
	}
}
