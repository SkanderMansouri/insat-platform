import { IUser } from 'app/core/user/user.model';

export interface IClub {
    id?: number;
    name?: string;
    domain?: string;
    president?: IUser;
    members?: IUser[];
}

export class Club implements IClub {
    constructor(public id?: number, public name?: string, public domain?: string, public president?: IUser, public members?: IUser[]) {}
}
