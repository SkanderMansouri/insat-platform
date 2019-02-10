import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, Principal, Account } from 'app/core';
import { LoginService } from 'app/core/login/login.service';
import { Router } from '@angular/router';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { FacebookService } from 'ngx-facebook';
import { LoginResponse } from 'ngx-facebook';
import { Subject } from 'rxjs/index';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    password: string;
    rememberMe: boolean;
    username: string;
    authenticationError: boolean;
    response: LoginResponse;
    refresh: Subject<any> = new Subject();

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private router: Router,
        private stateStorageService: StateStorageService,
        public activeModal: NgbActiveModal,
        private loginService: LoginService,
        private FB: FacebookService
    ) {}

    ngOnInit() {
        (window as any).fbAsyncInit = function() {
            this.FB.init({
                appId: '379044816243314',
                cookie: true,
                xfbml: true,
                version: 'v3.1'
            });
            this.FB.AppEvents.logPageView();
        };

        (function(d, s, id) {
            var js,
                fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {
                return;
            }
            js = d.createElement(s);
            js.id = id;
            js.src = 'https://connect.facebook.net/en_US/sdk.js';
            fjs.parentNode.insertBefore(js, fjs);
        })(document, 'script', 'facebook-jssdk');
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }
    login() {
        this.loginService
            .login({
                username: this.username,
                password: this.password,
                rememberMe: this.rememberMe
            })
            .then(() => {
                this.authenticationError = false;
                this.activeModal.dismiss('login success');
                if (this.router.url === '/register' || /^\/activate\//.test(this.router.url) || /^\/reset\//.test(this.router.url)) {
                    this.router.navigate(['']);
                }

                this.eventManager.broadcast({
                    name: 'authenticationSuccess',
                    content: 'Sending Authentication Success'
                });

                // previousState was set in the authExpiredInterceptor before being redirected to login modal.
                // since login is succesful, go to stored previousState and clear previousState
                const redirect = this.stateStorageService.getUrl();
                if (redirect) {
                    this.stateStorageService.storeUrl(null);
                    this.router.navigate([redirect]);
                }
            })
            .catch(() => {
                this.authenticationError = true;
            });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        console.log('is autho');
        return this.principal.isAuthenticated();
    }

    register() {
        this.activeModal.dismiss('to state register');
        this.router.navigate(['/register']);
    }
    loginWithFacebook(): void {
        console.log('submit login to facebook');
        // FB.login();
        this.FB.login().then(response => {
            console.log('submitLogin', response);
            if (response.authResponse) {
                this.loginService.facebooklogin(response.authResponse.accessToken).subscribe(res => {
                    this.loginService.loginWithToken(res.body.id_token, false);
                    console.log(res.body);
                    console.log(this.principal.isAuthenticated());
                    window.location.reload();
                    this.router.navigate(['/']);
                });
            } else {
                console.log('User login failed');
            }
        });
    }
}
