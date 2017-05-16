package wasdev.sample.websocket.planningPoker;

import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

public class DialogInformation {
	private MessageResponse currentResponse;
	private String searchType;
	private String searchText;
	
	public DialogInformation(){
		
	}
	
	public void updateCurrentResponse(MessageResponse currentResponse){
		this.currentResponse = currentResponse;
		//currentResponse.getOutput().get("nodes_visited").toString().equals("[Busca]")
		if(currentResponse.getContext().get("action").toString().equals("google/spotify")){
			this.searchType = currentResponse.getContext().get("typeUser").toString();
			
		}
		
	}
	
	public MessageResponse getCurrentResponse(){
		return currentResponse;
	}
	
	public String getText(){
		return currentResponse.getText().get(0);
	}
	
	public String getSearchText(){
		return this.searchText;
	}
	
	public String getSearchType(){
		return this.searchType;
	}
	
	public void setSearchText(String text){
		this.searchText = text;
	}

}