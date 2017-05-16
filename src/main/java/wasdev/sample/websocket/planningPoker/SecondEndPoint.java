package wasdev.sample.websocket.planningPoker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

@ServerEndpoint(value = "/SecondEndpoint")
public class SecondEndPoint {
	private Session currentSession = null;
	
	private ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
	private MessageResponse currentResponse = new MessageResponse();
	private DialogInformation currentDialog = new DialogInformation();
	
	

	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		// Store the WebSocket session for later use.
		currentSession = session;
		service.setUsernameAndPassword("8447ba89-14f0-45d1-92c3-d46874eb543c", "00i6CSqon8KF");
		MessageRequest newMessage = new MessageRequest.Builder().inputText("Ola").build();
		//"9e2ff96c-4a5e-4520-8590-dc0d0cc1e9cf"
		MessageResponse response = service.message("38cd5e05-37f2-4fac-8b9d-1f65c0ab8ee4", newMessage).execute();
		currentResponse = response;
		currentDialog.updateCurrentResponse(response);
		
		try{
			currentSession.getBasicRemote().sendText(buildMessage(currentResponse.getText().get(0),"Watson"));
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
		
	}

	@OnMessage
	public void receiveMessage(String message) {
		if(message.equals("")){
			return;
		}
		else{
			UserMessage currentMessage = new UserMessage(message);
			try {
				
				currentSession.getBasicRemote().sendText(currentMessage.buildPayload());
				System.out.println(currentMessage.getText());
				
				getNewResponse(currentMessage);
				
				if(currentDialog.getCurrentResponse().getOutput().get("nodes_visited").toString().equals("[procurar musica]")
						|| currentDialog.getCurrentResponse().getOutput().get("nodes_visited").toString().equals("[x, procurar musica]")){
					
					GoogleQuery query = new GoogleQuery(currentMessage.getText());
					currentDialog.setSearchText(currentMessage.getText());
					System.out.println(query.getCrude());
					System.out.println(query.getCommon());
					SpotifySearch spotify = new SpotifySearch();
					String[] completeResponse;
					if(query.getType() == "spelling"){
						GoogleQuery newquery = new GoogleQuery(query.getMain());
						completeResponse = spotify.doQuery(newquery.getType(), newquery.getMain());
					}
					else if(query.getType() == "error"){
						completeResponse = null;
					}
					else{
						completeResponse = spotify.doQuery(query.getType(), query.getMain());
					}
					
					if(completeResponse[0] != ""){
						System.out.println(currentDialog.getSearchType());
						System.out.println(query.getType());
						if (query.getType().equals(currentDialog.getSearchType()) || query.getType().equals("spelling")){
							currentSession.getBasicRemote().sendText(buildMessage(completeResponse[0],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(completeResponse[1],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(currentDialog.getText(),"Watson"));
							currentMessage.setText("sim");
						}
						else if(query.getType().equals("error")){
							currentMessage.setText("nao");
						}
						else{
							currentSession.getBasicRemote().sendText(buildMessage("Hmm... Em princípio eu encontrei uma sugestão de " 
						+ tradutor(query.getType())+" relacionado ao que você buscou. "+completeResponse[0],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(completeResponse[1],"Watson"));
							currentMessage.setText("sim");
						}
						
						
					}
					else{
						currentMessage.setText("nao");
					}
					
					getNewResponse(currentMessage);
					currentSession.getBasicRemote().sendText(buildMessage(currentDialog.getText(),"Watson"));
					
				}
				else if(currentDialog.getCurrentResponse().getOutput().get("nodes_visited").toString().equals("[Nova Busca]")){
					System.out.println(currentDialog.getCurrentResponse().getEntities().get(0).getValue());
					System.out.println(currentDialog.getSearchText());
					SpotifySearch nspotify = new SpotifySearch();
					String[] newResponse;
					newResponse = nspotify.doQuery(currentDialog.getCurrentResponse().getEntities().get(0).getValue().toLowerCase(), currentDialog.getSearchText());
					
					if(currentDialog.getCurrentResponse().getEntities().get(0).getValue().equals("tudo")){
						if(newResponse[0] != ""){
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[0],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[1],"Watson"));
						}
						else{
							currentSession.getBasicRemote().sendText(buildMessage("Não encontrei uma música no Spotify...","Watson"));
						}
						
						if(newResponse[2] != ""){
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[2],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[3],"Watson"));
						}
						else{
							currentSession.getBasicRemote().sendText(buildMessage("Não encontrei um álbum no Spotify...","Watson"));
						}
						
						if(newResponse[4] != ""){
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[4],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[5],"Watson"));
						}
						else{
							currentSession.getBasicRemote().sendText(buildMessage("Não encontrei esse artista no Spotify...","Watson"));
						}
						currentSession.getBasicRemote().sendText(buildMessage(currentDialog.getText(),"Watson"));
						
					}
					else{
						if(newResponse[0] != ""){
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[0],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(newResponse[1],"Watson"));
							currentSession.getBasicRemote().sendText(buildMessage(currentDialog.getText(),"Watson"));
						}
						else{
							currentSession.getBasicRemote().sendText(buildMessage("Não pude encontrar essa busca específica por "
						+ tradutor(currentDialog.getCurrentResponse().getEntities().get(0).getValue()) + ", desculpe."
									+ " Por favor tente novamente ajustando a ortografia ou adicionando mais referências.","Watson"));
						}
					}
					
					
				}
				else{
					System.out.println(currentDialog.getText());
					currentSession.getBasicRemote().sendText(buildMessage(currentDialog.getText(),"Watson"));
				}
				
				
				
			} catch (IOException ioe){
				ioe.printStackTrace();
			}
		}
		
		
		
	}


	@OnClose
	public void onClose(Session session, CloseReason reason) {

	}

	@OnError
	public void onError(Throwable t) {
		// no error processing will be done for this sample
		t.printStackTrace();
	}
	
	public String buildMessage(String text, String user){
		String message = "{\"message\": \""+text+"\", \"user\": \""+user+"\"}";
		return message;
	}
	
	public void getNewResponse(UserMessage message){
		MessageRequest newMessage = new MessageRequest.Builder().inputText(message.getText()).context(currentDialog.getCurrentResponse().getContext()).build();
		MessageResponse response = service.message("38cd5e05-37f2-4fac-8b9d-1f65c0ab8ee4", newMessage).execute();
		currentResponse = response;
		currentDialog.updateCurrentResponse(response);
		System.out.print(currentDialog.getCurrentResponse().getOutput().get("nodes_visited").toString());
	}
	
	public String tradutor(String tipo){
		if(tipo.equals("track")){
			return "música";
		}
		else if(tipo.equals("album")){
			return "álbum";
		}
		else if(tipo.equals("artist")){
			return "artista";
		}
		else{
			return "error";
		}
	}
	

}