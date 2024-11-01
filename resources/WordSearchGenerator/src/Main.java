import parser.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import generator.Generator;
import generator.Level;



public class Main {
	
	//Path to the file to levels.
	//if you wish to append to existing levels in the target folder, levels.txt must only contain the new levels to append,
	//delete the existing levels first from levels.txt. Or you will have duplicate levels.
	//Seviyeleri içeren dosya yolu.
	//Eğer mevcut seviyelere yenilerini eklemek istiyorsanız, yenileri farklı bir dosyaya yazıp okutun.
	private static String pathToLevelsFile = "C:\\Users\\umtgr\\Google Drive\\main\\lydia_games\\word_search_innovation\\levels_en.txt";
	
	//Path to the android/assets folder in the project
	//Android/assets dosya yolu
	private static String pathToAndroidAssetsFolder = "C:\\Users\\umtgr\\Documents\\WordSearchInnovation\\android\\assets";

	//The code of the language you will generate levels for.
	//Seviye üreteceğiniz dilin kodu.
	private static String languageCode = "en";
	
	
	//Change the alphabet letters according on your new language
	//Seviyelerini üreteceğiniz dilin alfabesi. Türkçe'ye dikkat.
	private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVYXZ";
	//private static String alphabet = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ";
	
	//Some levels offer coin bonus in one of the words that must be found. Specify the frequency of such levels below 
	//Bazı seviyelerde coin bonus mevcut. Bu tür seviyelerin sıklığını belirleyin.
	private static int offerCoinedWordEveryNLevel = 5;
	
	
	//The more this number is, the more words will be embedded in the board, but the board will tend
	//to have horizontal and vertical words rather than diagonal. Diagonal words obviously increases the 
	//difficulty of levels. Lower this number to get more diagonal words (Must be greater than 0).
	//Aşağıdaki sayı her bir leveli üretmek için deneme sayısıdıdır. Ne kadar büyük olursa tabloya
	//o kadar çok kelime girer fakat diyagonal kelime sayısı daha az olabilir. Diyagonal kelimeler olduğu
	//zaman o seviyenin zorluk derecesi de doğal olarak artar. Diyagonal kelimelerin fazla olmasını
	//istiyorsanız bu sayıyı düşürün (0'dan büyük olmalı).
	private static int numberOfAttemptsToGenerateALevel = 1;
	

	public static void main(String[] args) throws IOException {
		checkFolders();
		
		Parser.parse(pathToLevelsFile);
		if(Parser.foundError) {
			System.out.println("Please correct the listed errors, no levels have been generated");
			return;
		}else {
			Map<Integer, List<RawLevel>> mapping = Parser.getLevelMapping();
			
			for(Integer key : mapping.keySet()) {
				List<RawLevel> rawLevels = mapping.get(key);
				for(RawLevel rawLevel : rawLevels) {
					String[] myArray = new String[rawLevel.getWords().size()];
					rawLevel.getWords().toArray(myArray);
					Generator generator = new Generator(key, myArray, alphabet, numberOfAttemptsToGenerateALevel);
					generator.generate();
					Level level = generator.getLevel();
					level.category = rawLevel.getCategory();
					
						
					int index = Integer.parseInt(getNextLevelFileName());
					if((index + 1) % offerCoinedWordEveryNLevel == 0) level.setCoinedWordIndex();
					
					writeJsonToFile(constructJson(level));	
				}
			}
		}
	}
	
	
	
	private static File getTargetFolder() {
		String path = pathToAndroidAssetsFolder + "/data/" + languageCode + "/levels";
		return new File(path);
	}
	
	
	
	private static void checkFolders() {
		File levels = getTargetFolder();
		if(!levels.exists()) levels.mkdirs();
	}
	
	
	
	private static String getNextLevelFileName() {
		String[] folder = getTargetFolder().list();
		return folder.length + "";
		
	}
	
	
	
	private static String constructJson(Level level) {
		JSONObject jsonObject = new JSONObject(); 
		
		JSONArray words = new JSONArray(level.solution);
		jsonObject.put("w", words);
		
		jsonObject.put("b", new String(level.board));
		jsonObject.put("c", level.category);
		
		if(level.coinedWordIndex > -1) jsonObject.put("r", level.coinedWordIndex);
		
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
	
	
	
	
	private static void writeJsonToFile(String json) throws IOException {
		File file = new File(getTargetFolder(), getNextLevelFileName());
		
		FileWriter fw = new FileWriter(file);
		fw.write(json);
		fw.flush();
		fw.close();
	}
	
	
	

	
	
	
}
