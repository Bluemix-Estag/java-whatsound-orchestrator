package wasdev.sample.websocket.planningPoker;

import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import io.advantageous.qbit.http.request.HttpResponseBuilder;
import io.advantageous.qbit.http.request.HttpTextResponse;
import io.advantageous.qbit.reactive.Callback;

@RequestMapping("/todo-service")
public class REST {

	@RequestMapping(method = RequestMethod.GET)
    public void ping2(Callback<HttpTextResponse> callback) {
		
		GoogleQuery query = new GoogleQuery("");

        callback.resolve(HttpResponseBuilder.httpResponseBuilder()
                .setBody(query.getCrude()).setContentType("query")
                .setCode(777)
                .buildTextResponse());
    }

}