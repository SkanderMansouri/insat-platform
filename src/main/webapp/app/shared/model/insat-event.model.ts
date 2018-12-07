import { Moment } from 'moment';
import { IClub } from 'app/shared/model//club.model';
import { IUser } from 'app/core/user/user.model';

export interface IInsatEvent {
    id?: number;
    name?: string;
    date?: Moment;
    place?: string;
    description?: string;
    clubs?: IClub[];
    members?: IUser[];
    participants?: IUser[];
}

export class InsatEvent implements IInsatEvent {
    constructor(
        public id?: number,
        public name?: string,
        public date?: Moment,
        public place?: string,
        public description?: string,
        public clubs?: IClub[],
        public members?: IUser[],
        public participants?: IUser[]
    ) {}
}
