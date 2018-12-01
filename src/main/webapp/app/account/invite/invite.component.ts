import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { InviteService } from 'app/account/invite/invite.service';

@Component({
    selector: 'jhi-invite',
    templateUrl: './invite.component.html',
    styles: []
})
export class InviteComponent implements OnInit, AfterViewInit {
    error: string;
    errorEmailExists: string;
    resetAccount: any;
    success: string;

    constructor(private inviteService: InviteService, private elementRef: ElementRef, private renderer: Renderer) {}

    ngOnInit() {
        this.resetAccount = {};
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#email'), 'focus', []);
    }

    requestReset() {
        this.error = null;
        this.errorEmailExists = null;

        this.inviteService.save(this.resetAccount.email).subscribe(
            () => {
                this.success = 'OK';
            },
            response => {
                this.success = null;
                if (response.status === 400) {
                    this.errorEmailExists = 'ERROR';
                } else {
                    this.error = 'ERROR';
                }
            }
        );
    }
}
