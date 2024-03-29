package com.optus.candidateassessment.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class WordCountServiceImpl implements WordCountService {
	
	Map<String, Integer> wordCountMap;
	
	Map<String, Integer> sortedWordCountMap;
	
	// Post construct - Load sample text, remove commas and dots
	@PostConstruct
	public void searchWordsOccurence() {
		
		wordCountMap = new HashMap<String, Integer>();
		
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader(new FileReader("sample.txt"));
			
			String currentLine = reader.readLine();
			
			while(currentLine != null) {
				
				String[] words = currentLine.toUpperCase().split(" ");
				
				for (String word : words) {
					
					word = word.replace(",", "").replace(".", "");
					
					if (wordCountMap.containsKey(word)) {
						wordCountMap.put(word, wordCountMap.get(word)+1);
					}					
					else {
						wordCountMap.put(word, 1);
					}
					
				}
				
				currentLine = reader.readLine();
				
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				reader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String top(@PathVariable int num) {
		
		// use entrySet to sort in descending order, collect top sets into LinkedHashMap
		sortedWordCountMap = wordCountMap.entrySet()
				  .stream()
				  .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				  .limit(num)
				  .collect(Collectors.toMap(
				    Map.Entry::getKey, 
				    Map.Entry::getValue, 
				    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		// return a String, formatted like scv
		return sortedWordCountMap.keySet().stream()
				.map(key -> key + "|" + sortedWordCountMap.get(key))
				.collect(Collectors.joining("\n"));
		
	}

}
