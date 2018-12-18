import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { JhiLanguageHelper, User, UserService } from 'app/core';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/index';

@Component({
    selector: 'jhi-user-mgmt-update',
    templateUrl: './user-management-update.component.html'
})
export class UserMgmtUpdateComponent implements OnInit {
    user: User;
    club: IClub;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    clubsList: IClub[];
    constructor(
        private languageHelper: JhiLanguageHelper,
        private userService: UserService,
        private clubService: ClubService,
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
        });
        this.authorities = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.clubsList = [];
        this.clubService.clubsList().subscribe(clubsList => {
            this.clubsList = clubsList;
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.user.id == null) {
            if (this.club !== undefined && this.user.authorities.indexOf('ROLE_PRESIDENT') !== -1) {
                console.log('we will update this club = {}', this.club.id);
                this.club.president = this.user;
                this.userService
                    .createPresident(this.user, this.club.id)
                    .subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
            } else {
                this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
            }
        } else {
            if (this.club !== undefined && this.user.authorities.indexOf('ROLE_PRESIDENT') !== -1) {
                console.log('we will update this club {}', this.club.id);
                this.club.president = this.user;
                this.userService
                    .updatePresident(this.user, this.club.id)
                    .subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
            } else {
                this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
            }
        }
    }
    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    onSelect(club: IClub) {
        console.log(club.name);
        this.club = club;
    }
}
