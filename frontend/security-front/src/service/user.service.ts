import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import { ApiService } from './api.service';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  

  currentUser;

  constructor(
    private apiService: ApiService,
    private config: ConfigService
  ) {
  }

  getMyInfo() {
    return this.apiService.get(this.config.whoamiUrl)
      .pipe(map(user => this.currentUser = user));
  }

  resetCredentials() {
    return this.apiService.get(this.config.resetCredentialsUrl);
  }
 

}
