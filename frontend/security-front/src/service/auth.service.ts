import {Injectable} from '@angular/core';
import {HttpHeaders} from '@angular/common/http';
import {map} from 'rxjs/operators';
import { UserService } from './user.service';
import { ApiService } from './api.service';
import { ConfigService } from './config.service';

@Injectable()
export class AuthService {

  constructor(
    private apiService: ApiService,
    private userService: UserService,
    private config: ConfigService,
  ) {
  }

  login(user) {
    const loginHeaders = new HttpHeaders({
        Accept: 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded'
      });
      const body = `username=${user.username}&password=${user.password}`;
      return this.apiService.post(this.config.loginUrl, body, loginHeaders)
        .pipe(map(() => {
          console.log('Login success');
          this.userService.getMyInfo().subscribe();
        }));
  }

  signup() {
  
  }

  logout() {
 
  }

  changePassowrd() {
    
  }

}
