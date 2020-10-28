import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  getMyInfo() {
    throw new Error('Method not implemented.');
  }

  currentUser;

  constructor(
  ) {
  }

 

}
