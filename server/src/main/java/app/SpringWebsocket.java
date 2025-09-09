package app;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import tak.WebsocketConnection;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpringWebsocket implements WebsocketConnection {

    private final WebSocketSession session;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    private volatile boolean streamEnded = false;

    public SpringWebsocket(WebSocketSession session) {
        this.session = session;
    }

    // Called by Spring when a new message arrives
    public void enqueueMessage(String payload) {
        if (!messageQueue.offer(payload)) {
            System.err.println("Message queue full, dropping message");
        }
    }

    @Override
    public String receive(boolean blocking) {
        if (streamEnded) return null;
        try {
            return blocking ? messageQueue.take() : messageQueue.poll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(String msg) {
        if (streamEnded || !session.isOpen()) return;
        try {
            session.sendMessage(new BinaryMessage(msg.getBytes()));
        } catch (IOException e) {
            kill(1);
        }
    }

    @Override
    public void kill(int reason) {
        streamEnded = true;
        try {
            session.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public boolean isHeaderEnded() {
        return true;
    }

    @Override
    public boolean isStreamEnded() {
        return streamEnded;
    }
}