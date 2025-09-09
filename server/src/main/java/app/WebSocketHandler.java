package app;

import org.springframework.lang.NonNull;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tak.Client;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler extends AbstractWebSocketHandler implements SubProtocolCapable {
    private final ConcurrentHashMap<String, SpringWebsocket> connections = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        SpringWebsocket ws = new SpringWebsocket(session);
        connections.put(session.getId(), ws);

        Client cc = new Client(ws);
        cc.start();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, @NonNull TextMessage message) {
        SpringWebsocket ws = connections.get(session.getId());
        if (ws != null) {
            ws.enqueueMessage(message.getPayload());
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, @NonNull BinaryMessage message) {
        SpringWebsocket ws = connections.get(session.getId());
        if (ws != null) {
            ws.enqueueMessage(String.valueOf(message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
        SpringWebsocket ws = connections.remove(session.getId());
        if (ws != null) {
            ws.kill(0);
        }
    }

    @Override
    @NonNull
    public List<String> getSubProtocols() {
        return List.of("binary");
    }
}
