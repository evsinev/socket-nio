# socket-nio

An opensource websocket and socket.io java/android implementation.


## How to connect via websocket
```java
WebSocketHandshakeRequest request = new WebSocketHandshakeRequest.Builder()
    .url("http://localhost:8080/socket.io/1/websocket")
    .build();
WebSocketSession session = client.connect(request);

// You can send messages in a separate thread.
// Message will be added to the queue and will be written to the socket in a writer thread.
// So we use only two threads: one for writing messages to a socket and second is for reading from a socket.
// It is very useful in Android to track only two threads.
session.send (...);

// we block a current thread until a connection is closed. 
// It can help you to use only one thread to accept messages from a server.
// Once it done you can rerun it in a loop.
session.startAndWait(new IWebSocketListener() {
    @Override
    public void onMessage(MutableWebSocketFrame aFrame, WebSocketContext aContext) {
        LOG.debug("Frame: {}", aFrame);
    }

    @Override
    public void onFailure(Throwable aError) {
        LOG.error("Failure", aError);
    }
});
```


## How to connect via socket.io
```java
SocketIoClient client = new SocketIoClient();
SocketIoSession session = client.connect(new URL("http://localhost:8080/socket.io/1/");

ISocketIoListener listener = new ISocketIoListener() {

    @Override
    public void onEvent(String aEventName, SocketIoContext aContext, Object... args) {
        LOG.debug("Event is {}", aEventName);
        aContext.ack();
    }

    @Override
    public void onFailure(Throwable aError) {
        LOG.error("Error is", aError);
    }
};

session.startAndWait(listener);

```

## Projects using socket-nio
* http://payber.com in an android app https://play.google.com/store/apps/details?id=com.payneteasy.payber

## How to start socket.io server
We use https://github.com/mrniko/netty-socketio to run socket.io server

## How to connect to your project
### maven
```xml
<dependency>
	<groupId>com.payneteasy.socket-nio</groupId>
	<artifactId>client</artifactId>
	<version>1.0-4</version>
</dependency>
```

### gradle
'com.payneteasy.socket-nio:client:1.0-4'


