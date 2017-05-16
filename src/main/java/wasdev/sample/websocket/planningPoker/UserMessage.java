package wasdev.sample.websocket.planningPoker;


public class UserMessage {
	
	private String text;
	private String user;

	
	public UserMessage(String message){
		//message = "{\"message\": \""+message+"\", \"user\": \"Anon\"}";
		String[] partes = message.split(":");
		String user = partes[2].split("\"")[1];
		String texto = partes[1].split("\"")[1];
		
		this.text = texto;
		this.user = user;

	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String texto){
		this.text = texto;
	}
	
	public String getUser(){
		return user;
	}
	
	public String buildPayload(){
		String message = "{\"message\": \""+text+"\", \"user\": \""+user+"\"}";
		return message;
	}
}