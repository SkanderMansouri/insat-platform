/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { InsatTestModule } from '../../../test.module';
import { ClubComponent } from 'app/entities/club/club.component';
import { ClubService } from 'app/entities/club/club.service';
import { Club } from 'app/shared/model/club.model';

describe('Component Tests', () => {
    describe('Club Management Component', () => {
        let comp: ClubComponent;
        let fixture: ComponentFixture<ClubComponent>;
        let service: ClubService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [InsatTestModule],
                declarations: [ClubComponent],
                providers: []
            })
                .overrideTemplate(ClubComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ClubComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ClubService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Club(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.clubs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
