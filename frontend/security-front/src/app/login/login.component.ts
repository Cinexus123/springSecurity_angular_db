import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { UserService } from 'src/service/user.service';
import { AuthService } from 'src/service/auth.service';
import { DisplayMessage } from 'src/shared/models/display-message';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent  implements OnInit, OnDestroy {

  title = 'Login';
  githubLink = 'https://github.com/Cinexus123/springSecurity_angular_db';
  form: FormGroup;

  submitted = false;

  
  notification: DisplayMessage;



  returnUrl: string;
  private ngUnsubscribe: Subject<void> = new Subject<void>();

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {
  
   }

  ngOnDestroy(): void {
     this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  ngOnInit(): void {
    this.route.params
    .pipe(takeUntil(this.ngUnsubscribe))
    .subscribe((params: DisplayMessage) => {
      this.notification = params;
    });
  // get return url from route parameters or default to '/'
  this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  this.form = this.formBuilder.group({
    username: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(64)])],
    password: ['', Validators.compose([Validators.required, Validators.minLength(3), Validators.maxLength(32)])]
  });
  }

  onSubmit() {
    this.notification = undefined;
    this.submitted = true;

    this.authService.login(this.form.value)
      .subscribe(data => {
          this.userService.getMyInfo().subscribe();
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.submitted = false;
          this.notification = {msgType: 'error', msgBody: 'Incorrect username or password.'};
        });
  }

  onResetCredentials() {
    this.userService.resetCredentials()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(res => {
        if (res.result === 'success') {
          alert('Password has been reset to 123 for all accounts');
        } else {
          alert('Server error');
        }
      });
  }

  repository() {
    window.location.href = this.githubLink;
  }

}
