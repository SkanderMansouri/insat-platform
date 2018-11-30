import { Component, OnInit } from '@angular/core';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-clubs-list',
    templateUrl: './clubs-list.component.html',
    styles: []
})
export class ClubsListComponent implements OnInit {
    clubs: IClub[];

    constructor(private clubService: ClubService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.getClubs();
    }

    getClubs() {
        this.clubService.query().subscribe((res: HttpResponse<IClub[]>) => (this.clubs = res.body));
    }
}
