import { ISlackUser } from 'app/shared/model//slack-user.model';

export interface IClub {
    id?: number;
    name?: string;
    domain?: string;
    president?: ISlackUser;
}

export class Club implements IClub {
    constructor(public id?: number, public name?: string, public domain?: string, public president?: ISlackUser) {}
}
