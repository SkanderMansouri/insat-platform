import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IClub } from 'app/shared/model//club.model';

export const enum StatusEnumeration {
    PENDING = 'PENDING',
    ACCEPTED = 'ACCEPTED',
    REJECTED = 'REJECTED',
    DELETED = 'DELETED'
}

export interface IJoinClubRequest {
    id?: number;
    requestTime?: Moment;
    status?: StatusEnumeration;
    user?: IUser;
    club?: IClub;
}

export class JoinClubRequest implements IJoinClubRequest {
    constructor(
        public id?: number,
        public requestTime?: Moment,
        public status?: StatusEnumeration,
        public user?: IUser,
        public club?: IClub
    ) {}
}
