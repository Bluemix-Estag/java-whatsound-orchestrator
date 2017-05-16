package wasdev.sample.websocket.planningPoker;

import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

public class GoogleQuery{
	
	private String crude;
	private String common;
	private String main;
	private String type;
	
	
	public GoogleQuery(String query){
		
		Customsearch customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), null);
		
		for(int i = 0; i <= 2; i++){
			try {
				com.google.api.services.customsearch.Customsearch.Cse.List list = customsearch.cse().list(query);
				//list.setKey("AIzaSyABoAZWGTEL3tX1Krjj9MKoPHxfkjOQ0eE");
				list.setKey("AIzaSyC-Tma5_gMM9DcmSY2swYnhPF89UEX6gyg");
				list.setCx("013580269037227782210:yarymksnrvm");
				//list.setCx("007233760118331528145:qcjc5x0yozw");
				list.setExactTerms("VAGALUME");
				//list.setExcludeTerms("tradução");
				list.setGl("br");
				list.setNum(Long.decode("1"));
				list.setAlt("json");
				Search results = list.execute();
				try{
					List<Result> items = results.getItems();
					for(Result result:items)
					{
						this.crude = result.getTitle();
						if(crude.contains(" ...")){
							this.type = "track";
							String arrumada = arrumarString(crude);
							String partes[] = arrumada.split(" - ");
							this.main = partes[0];
						}
						else{
							setOutput();
						}
						

					}
				}
				catch (NullPointerException e){
					try{	
						 String spelling = results.getSpelling().getCorrectedQuery();
						 System.out.println(spelling);
						 this.type = "spelling";
						 this.main = spelling.replaceAll(" \"VAGALUME\"", "");
					}
					catch (NullPointerException j){
						this.type = "error";
					}
				}
				
			} catch (IOException e) {
	        // TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("error");
				this.type = "error";
				
			}
		}
	}
	
	
	public String getCrude(){
		return crude;
	}
	
	public String getCommon(){
		return common;
	}
	
	public String getMain(){
		return main;
	}
	
	public String getType(){
		return type;
	}

	public void setOutput(){
		String arrumada = arrumarString(crude);
		String partes[] = arrumada.split(" - ");
		if(partes.length == 4){
			this.common = partes[0]+"+"+partes[1];
			this.main = partes[0];
			this.type = "album";
		}
		else if(partes.length == 3){
			this.common = partes[0]+"+"+partes[1];
			this.main = partes[0];
			this.type = "track";
		}
		else if(partes.length == 2){
			this.common = partes[0];
			this.main = partes[0];
			this.type = "artist";
		}
		else{
			this.type = "error";
			this.common = null;
			System.out.println("entrei aqui");
		}
	}
	
	public String arrumarString(String txt){

		String faseone = Normalizer.normalize(txt, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		faseone = faseone.replace("(traducao)", "").replace("(cifrada)", "");
		return faseone.toLowerCase();
	}
}