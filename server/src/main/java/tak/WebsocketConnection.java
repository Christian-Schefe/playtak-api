package tak;

public interface WebsocketConnection {
    /**
     * Blocking or non-blocking receive of the next message.
     *
     * @param blocking if true, waits for a message, otherwise returns immediately (null if none).
     * @return the message, or null if none available/connection closed.
     */
    String receive(boolean blocking);

    /**
     * Send a message to the client.
     *
     * @param msg the message string
     */
    void send(String msg);

    /**
     * Kill/close the connection.
     *
     * @param reason arbitrary int reason code
     */
    void kill(int reason);

    boolean isHeaderEnded();   // handshake complete

    boolean isStreamEnded();   // connection closed
}