import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

class socketConnection {

  constructor(setup = () => {}) {
    this.isConnected = false;
    this.stompClient = null;
    this.setup = setup;
  }

  setConnected = (isConnected) => {
    this.isConnected = isConnected;
    // to do when connecting / disconnecting ... 
    this.setup(isConnected);
  }
  
  connect = (URL, callback) => {
    const socket = new SockJS('http://localhost:8080/similarity-finder-websocket');
    this.stompClient = Stomp.over(socket);
    this.stompClient.debug = null;
    this.stompClient.connect({}, (frame) => {
        this.setConnected(true);
        this.stompClient.subscribe(URL, (message) => {
            const msg = JSON.parse(message.body);
            callback(msg);
        });
    });
  }

  disconnect = () => {
    if (this.stompClient !== null) {
        this.stompClient.disconnect();
    }
    this.setConnected(false);
  }

  send = (URL, body = {}) => {
    if (this.stompClient) {
      this.stompClient.send(URL);
      // this.stompClient.send(URL, {}, JSON.stringify(body));
    }
  }

}

export default socketConnection;