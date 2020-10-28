import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { UserService } from 'src/service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent  implements OnInit, OnDestroy {

  title = 'Login';
  githubLink = 'https://github.com/bfwg/angular-spring-starter';
  form: FormGroup;

  submitted = false;



  returnUrl: string;
  private ngUnsubscribe: Subject<void> = new Subject<void>();

  constructor(
    private userService: UserService,
    private router: Router
  ) {
  
   }

  ngOnDestroy(): void {
     this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  ngOnInit(): void {
  }

  onSubmit() {
    /**
     * Innocent until proven guilty
     */
   
  }

}
