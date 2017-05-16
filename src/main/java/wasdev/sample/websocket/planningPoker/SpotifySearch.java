package wasdev.sample.websocket.planningPoker;

import java.util.List;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.AlbumSearchRequest;
import com.wrapper.spotify.methods.ArtistSearchRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.Artist;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimpleAlbum;
import com.wrapper.spotify.models.Track;

public class SpotifySearch {
	Api api = Api.DEFAULT_API;
	
	String artistResult = "";
	int artistTotal = 0;
	
	String albumResult = "";
	int albumTotal = 0;
	
	String trackResult = "";
	int trackTotal = 0;
	
	String preview = "";
	
	public SpotifySearch (){
		
		
	}
	
	public String[] doQuery(String type, String query){
		
		if(type.equals("track")){
			String response[] = new String[2];
			final TrackSearchRequest request = api.searchTracks(query).market("BR").build();
			 
			try {
			   final Page<Track> trackSearchResult = request.get();
			   System.out.println("I got " + trackSearchResult.getTotal() + " results!");
			   this.trackTotal = trackSearchResult.getTotal();
			   this.trackResult = trackSearchResult.getItems().get(0).getName();
			   this.artistResult = trackSearchResult.getItems().get(0).getArtists().get(0).getName();
			   this.albumResult = trackSearchResult.getItems().get(0).getAlbum().getName();
			   this.preview = trackSearchResult.getItems().get(0).getUri();
			   
			   response[0] = "Eu encontrei a música: "+trackSearchResult.getItems().get(0).getName()
					   +" do artista "+ trackSearchResult.getItems().get(0).getArtists().get(0).getName();
			   
			   final TrackRequest arequest = api.getTrack(trackSearchResult.getItems().get(0).getId()).build();
			    
			   try {
			      final Track track = arequest.get();
			      this.preview = track.getUri();
			      
			      response[1] = track.getUri();
			      
			      return response;
			   } catch (Exception e) {
			      System.out.println("Something went wrong!" + e.getMessage());
			      response[0] = "";
			      response[1] = "";
			      return response;
			   }
			   
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			   response[0] = "";
			      response[1] = "";
			      return response;
			}
		}
		else if(type.equals("album")){
			String response[] = new String[2];
			final AlbumSearchRequest request = api.searchAlbums(query).market("BR").build();
			 
			try {
			   final Page<SimpleAlbum> albumSearchResult = request.get();
			   
			   this.albumTotal = albumSearchResult.getTotal();
			   this.albumResult = albumSearchResult.getItems().get(0).getName();
			   
			   final AlbumRequest arequest = api.getAlbum(albumSearchResult.getItems().get(0).getId()).build();
			    
			   try {
			      final Album album = arequest.get();
			      this.preview = album.getImages().get(0).getUrl();
			      this.artistResult = album.getArtists().get(0).getName();
			      
			      response[0] = "Eu encontrei o album: "+albumSearchResult.getItems().get(0).getName()
						   +" do artista "+ album.getArtists().get(0).getName();
			      
			      response[1] = album.getImages().get(0).getUrl();
			      
			      return response;
			   } catch (Exception e) {
			      System.out.println("Something went wrong!" + e.getMessage());
			      response[0] = "";
			      response[1] = "";
			      return response;
			   }
			   
			   
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			   response[0] = "";
			      response[1] = "";
			      return response;
			}
		}
		else if(type.equals("artist")){
			String response[] = new String[2];
			final ArtistSearchRequest request = api.searchArtists(query).market("BR").build();
			 
			try {
			   final Page<Artist> artistSearchResult = request.get();
			   
			   this.artistTotal = artistSearchResult.getTotal();
			   this.artistResult = artistSearchResult.getItems().get(0).getName();
			   this.albumResult = artistSearchResult.getItems().get(0).getGenres().get(0);
			   this.preview = artistSearchResult.getItems().get(0).getImages().get(0).getUrl();
			   
			   response[0] = "Encontrei o artista: "+artistSearchResult.getItems().get(0).getName()
					   +" do gênero "+artistSearchResult.getItems().get(0).getGenres().get(0);
			   
			   response[1] = artistSearchResult.getItems().get(0).getImages().get(0).getUrl();
			   
			   return response;
			   
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			   response[0] = "";
			      response[1] = "";
			      return response;
			}
		}
		else if(type.equals("tudo")){
			String response[] = new String[6];
			
			final TrackSearchRequest request = api.searchTracks(query).market("BR").build();
			 
			try {
			   final Page<Track> trackSearchResult = request.get();
			   System.out.println("I got " + trackSearchResult.getTotal() + " results!");
			   this.trackTotal = trackSearchResult.getTotal();
			   this.trackResult = trackSearchResult.getItems().get(0).getName();
			   this.artistResult = trackSearchResult.getItems().get(0).getArtists().get(0).getName();
			   this.albumResult = trackSearchResult.getItems().get(0).getAlbum().getName();
			   this.preview = trackSearchResult.getItems().get(0).getUri();
			   
			   response[0] = "Eu encontrei a música: "+trackSearchResult.getItems().get(0).getName()
					   +" do artista "+ trackSearchResult.getItems().get(0).getArtists().get(0).getName();
			   
			   final TrackRequest arequest = api.getTrack(trackSearchResult.getItems().get(0).getId()).build();
			    
			   try {
			      final Track track = arequest.get();
			      this.preview = track.getUri();
			      
			      response[1] = track.getUri();
			      
			      //return response;
			   } catch (Exception e) {
			      System.out.println("Something went wrong!" + e.getMessage());
			      response[1] = "";
			   }
			   
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			   response[0] = "";
			}
			
			final AlbumSearchRequest alrequest = api.searchAlbums(query).market("BR").build();
			 
			try {
			   final Page<SimpleAlbum> albumSearchResult = alrequest.get();
			   
			   this.albumTotal = albumSearchResult.getTotal();
			   this.albumResult = albumSearchResult.getItems().get(0).getName();
			   
			   final AlbumRequest arequest = api.getAlbum(albumSearchResult.getItems().get(0).getId()).build();
			    
			   try {
			      final Album album = arequest.get();
			      this.preview = album.getImages().get(0).getUrl();
			      this.artistResult = album.getArtists().get(0).getName();
			      
			      response[2] = "Eu encontrei o album: "+albumSearchResult.getItems().get(0).getName()
						   +" do artista "+ album.getArtists().get(0).getName();
			      
			      response[3] = album.getImages().get(0).getUrl();
			      
			      //return response;
			   } catch (Exception e) {
			      System.out.println("Something went wrong!" + e.getMessage());
			      response[3] = "";
			   }
			   
			   
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			   response[2] = "";
			   
			}
			
			final ArtistSearchRequest arrequest = api.searchArtists(query).market("BR").build();
			 
			try {
			   final Page<Artist> artistSearchResult = arrequest.get();
			   
			   this.artistTotal = artistSearchResult.getTotal();
			   this.artistResult = artistSearchResult.getItems().get(0).getName();
			   this.albumResult = artistSearchResult.getItems().get(0).getGenres().get(0);
			   this.preview = artistSearchResult.getItems().get(0).getImages().get(0).getUrl();
			   
			   response[4] = "Encontrei o artista: "+artistSearchResult.getItems().get(0).getName()
					   +" do gênero "+artistSearchResult.getItems().get(0).getGenres().get(0);
			   
			   response[5] = artistSearchResult.getItems().get(0).getImages().get(0).getUrl();
			   
			   //
			   
			} catch (Exception e) {
			   System.out.println("Something went wrong!" + e.getMessage());
			   response[4] = "";
			   response[5] = "";
			}
			
			return response;
		}
		else{
			String response[] = new String[2];
			System.out.println("errou");
			response[0] = null;
			response[1] = null;
			return response; 
		}
		
	}
	
	public String getArtistResult(){
		return this.artistResult;
	}
	public String getAlbumResult(){
		return this.albumResult;
	}
	public String getTrackResult(){
		return this.trackResult;
	}
	
}