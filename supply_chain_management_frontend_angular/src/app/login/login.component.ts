import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/services/auth.service';
import { UserService } from 'src/services/user.service';
import { Router, ActivatedRoute } from '@angular/router';
import fetchFromAPI from 'src/services/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  form!: FormGroup;
  acc_not_found: boolean = false;
  admin_Status: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private router: Router) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
  }

  public login() {
    if (this.form.invalid) {
      return;
    }
    this.authService.login(
      this.form.get('username')!.value,
      this.form.get('password')!.value
      ).then((result) =>  result.subscribe({
        next: async (res) => {
          if(res.login_status == true){
            this.router.navigateByUrl('/home'); 
          }
          else{
            this.acc_not_found = true;
          }
        },
        error: (e) => {
          console.log(e)
          this.acc_not_found = true;
        }
      }))

  }
}
