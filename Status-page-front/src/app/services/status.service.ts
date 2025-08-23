import { Injectable } from '@angular/core';
import { CompatClient, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { SwitchStatus } from '../models/switchStatus';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  constructor() { }

  stompClient?: CompatClient;

  status = new BehaviorSubject<SwitchStatus[] | undefined>(undefined)
  status$ = this.status.asObservable()

  initializeWebSocketConnection() {
    this.stompClient = Stomp.over(() => new SockJS('https://localhost:9090/ws'));
    const that = this;

    this.stompClient.connect({}, function(frame:IMessage) {
      that.stompClient?.subscribe('/message', (message:IMessage) => {
        if (message.body) {
          console.log(JSON.parse(message.body))
          that.status.next(JSON.parse(message.body))
        }
      });
    });
  }
}
