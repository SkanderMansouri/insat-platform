import { Component, OnInit } from '@angular/core';
import { ISlackUser } from 'app/shared/model/slack-user.model';
import { SlackUserService } from './slack-user.service';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-slack-users-list',
    templateUrl: './slack-users-list.component.html',
    styles: []
})
export class SlackUsersListComponent implements OnInit {
    slackUsers: ISlackUser[];

    constructor(private slackUserService: SlackUserService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.getSlackUsers();
    }

    getSlackUsers() {
        this.slackUserService.query().subscribe((res: HttpResponse<ISlackUser[]>) => (this.slackUsers = res.body));
    }
}
